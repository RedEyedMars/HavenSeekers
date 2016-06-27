package environment.ship.tile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import entity.Entity;
import entity.choice.ChoicePrototype;
import environment.Direction;
import environment.Position;
import environment.Positionable;
import environment.items.Area;
import gui.graphics.GraphicEntity;
import gui.inputs.MotionEvent;
import main.Hub;
import main.Log;
import misc.condition.Condition;
import parser.StringHeirachy;
import storage.Storable;
import storage.Storer;
import storage.StorerageIterator;

public class Tile extends GraphicEntity implements Positionable, Storable{
	final private TravelPoint position;
	protected List<Area> areaThatOwnsThis = new ArrayList<Area>(){
		@Override
		public boolean add(Area area){
			if(isUnowned()&&!area.getPosition().equals(position)){
				setOwner(area,isPrimaryAreaTile);
				return true;
			}
			else {
				return super.add(area);
			}
		}
		@Override
		public Area remove(int i){
			Area area = get(i);
			setOwner(null,false);
			return area;
		}
	};

	protected List<Entity> entityOnTopOfThis = new ArrayList<Entity>();
	
	private boolean isPrimaryAreaTile = false;
	protected boolean isTravelPoint = false;

	private LinkedList<Direction> travelPointMap = new LinkedList<Direction>();
	private LinkedList<TravelPoint> travelPoints;
	
	public Tile(){
		super("floor_tile_2");
		this.position = new TravelPoint(0,0);
		this.travelPoints = this.position.getNexts();		
	}
	public Tile(int x, int y) {
		super("floor_tile_2");
		this.position = new TravelPoint(x,y);
		this.travelPoints = this.position.getNexts();
	}
	public Tile(Position position){
		this(position.getX(),position.getY());
	}

	public Entity getEntity() {
		return entityOnTopOfThis.get(0);
	}

	public void setEntity(Entity entity) {
		if(!this.entityOnTopOfThis.isEmpty()){
			this.removeChild(getEntity());
		}
		this.entityOnTopOfThis.add(entity);
		if(entity!=null){
			this.addChild(entity);
		}
		entity.setPosition(getPosition());
	}

	@Override
	public float offsetX(int index) {
		return 0f;//-getWidth()*0.5f;
	}

	@Override
	public float offsetY(int index) {
		return 0f;//getHeight()*0.4f;
	}
	public Position getPosition(){
		return position;
	}

	public TravelPoint getTravelPoint() {
		return position;
	}

	public boolean isEmpty() {
		return entityOnTopOfThis==null;
	}

	public boolean isUnowned() {
		return areaThatOwnsThis.isEmpty();
	}

	public void setOwner(Area area,boolean primary) {
		if(!isUnowned()&&isPrimaryAreaTile){
			removeChild(areaThatOwnsThis.get(0));
			areaThatOwnsThis.remove(areaThatOwnsThis.get(0));
		}
		if(area != null){
			this.isPrimaryAreaTile = primary;
			if(primary){
				area.setPosition(this.position);
				addChild(area);
				area.setTravelPoint(getTravelPoint());
				if(!isTravelPoint()){
					this.isTravelPoint = true;
					((Tileable)parent).reputTile(this);
				}
			}
			this.areaThatOwnsThis.add(area);
		}
	}
	public void removeOwner(){
		setOwner(null,false);
	}

	@Override
	public boolean onHover(MotionEvent event){
		if(super.onHover(event)){
			return true;
		}
		else {
			return false;
		}
	}

	public void onAddToShip(Tileable ship){
	}
	public void onRemoveFromShip(Tileable ship){
	}
	public void addDirection(TravelPoint travelPoint, Direction directionTo) {
		travelPointMap.add(directionTo);
		travelPoints.add(travelPoint);
	}
	public void animate(){
		if(!travelPointMap.isEmpty()&&Log.tiles){
			this.entity.setFrame(travelPointMap.getLast().getIndex()+1);
		}
	}
	public void adjustPosition(Position offset) {
		position.adjust(offset);
	}
	public void adjustPostionTo(Position offset) {
		position.setX(0);
		position.setY(0);
		position.adjust(offset);
	}
	public void clearDirections(){
		travelPointMap.clear();
		travelPoints.clear();
	}
	public void mergeWith(Tile tile){
		//mergeTravelPointsWith(tile);
		tile.parent = this.parent;
	}
	public void learnPointsFromTouchingTile(Tile tile){
		List<Integer> indicesUsed = new ArrayList<Integer>();
		int size = tile.travelPoints.size();
		for(int i=0;i<size;++i){
			int index = learnTravelPoint(tile,tile.travelPoints.get(i),tile.travelPointMap.get(i));
			if(index!=-1){
				indicesUsed.add(index);
			}
		}
		for(int i=0;i<travelPoints.size();++i){
			if(indicesUsed.contains(i))continue;
			else {
				TravelPoint point = travelPoints.get(i);
				Direction direction = tile.getPosition().getDirectionTo(getPosition());
				tile.travelPointMap.add(direction);
				tile.travelPoints.add(point);

			}
		}
	}
	public int learnTravelPoint(Tile from, TravelPoint target, Direction directionOfTarget){
		int index = travelPoints.indexOf(target);
		if(index==-1){
			if(from!=null){
				directionOfTarget = getPosition().getDirectionTo(from.getPosition());
			}
			travelPointMap.add(directionOfTarget);
			travelPoints.add(target);
		}
		else {
			Direction myDirection = travelPointMap.get(index); 
			if(myDirection.equals(directionOfTarget)){
				return index;
			}
			if(from==null){
				travelPointMap.set(index,directionOfTarget);
				return index;
			}			
			TravelPath myPath = TravelPath.findQuickest(getTravelPoint(), target);
			TravelPath theirPath = TravelPath.findQuickest(from.getTravelPoint(), target);
			if(myPath.getDistance()<theirPath.getDistance()){
				from.travelPointMap.set(from.travelPoints.indexOf(target),myDirection);
			}
			else {
				travelPointMap.set(index,directionOfTarget);
			}
		}
		return index;
	}
	public void mergeTravelPointsWith(Tile tile) {
		for(int i=0;i<tile.travelPoints.size();++i){
			if(!travelPoints.contains(tile.travelPoints.get(i))){
				travelPointMap.add(tile.travelPointMap.get(i));
				travelPoints.add(tile.travelPoints.get(i));
			}
			else {
				TravelPoint target = travelPoints.get(i);
				int index = travelPoints.indexOf(target);
				if(travelPointMap.get(index).equals(tile.travelPointMap.get(i)))
					continue;
				TravelPath myPath = TravelPath.findQuickest(getTravelPoint(), target);
				TravelPath theirPath = TravelPath.findQuickest(tile.getTravelPoint(), target);
				if(myPath.getDistance()>theirPath.getDistance()){

					travelPointMap.set(index,tile.travelPointMap.get(i));
				}
			}
		}
	}

	public boolean isTravelPoint(){
		return isTravelPoint;
	}
	public Direction getTravelDirection(TravelPoint toPointTo) {
		int index = travelPoints.indexOf(toPointTo);
		if(index==-1){
			return null;
		}
		else return travelPointMap.get(index);
	}
	public void unlearnTravelPoint(TravelPoint travelPoint) {
		int indexOf = travelPoints.indexOf(travelPoint);
		if(indexOf!=-1){
			travelPoints.remove(indexOf);
			travelPointMap.remove(indexOf);
		}
	}

	private Storer<Tile> storer = new Storer<Tile>(this,"T"){
		{
			integers = 2;
			booleans = 2;
		}
		@Override
		protected Object[] storeCharacteristics() {
			return o(self.getPosition().getX(),self.getPosition().getY(),
					self.isPrimaryAreaTile,self.isTravelPoint);
		}
		@Override
		protected void adjust(Object... objs) {
			int x = (int) objs[0];
			int y = (int) objs[1];
			if(x!=self.position.getX()||y!=self.position.getY()){
				self.position.setX(x);
				self.position.setY(y);
				//reAdd = true;
			} 
			self.isPrimaryAreaTile = (boolean) objs[2];
			self.isTravelPoint = (boolean) objs[3];
		}
	};
	@Override
	public Storer<Tile> getStorer() {
		return storer;
	}

	public Iterable<Storer> getStorerIterator() {
		return new StorerageIterator(
				this,"areaThatOwnsThis"
				/*,entityOnTopOfThis,travelPointMap,travelPoints*/){
			@Override
			protected Object getField(Field field, Object target) throws IllegalArgumentException, IllegalAccessException{
				return field.get(target);
			}
		};
	}
	public Area getArea() {
		return areaThatOwnsThis.get(0);
	}
}
