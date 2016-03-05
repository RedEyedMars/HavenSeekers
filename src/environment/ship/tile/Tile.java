package environment.ship.tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import entity.Entity;
import environment.Direction;
import environment.Position;
import environment.Positionable;
import environment.items.Area;
import environment.ship.tile.storage.TileStorer;
import gui.graphics.GraphicEntity;
import gui.inputs.MotionEvent;
import main.Log;
import storage.Storable;
import storage.Storer;
import storage.StorerIteratorIterator;

public class Tile extends GraphicEntity implements Positionable, Storable{
	final private TravelPoint position;
	private List<Area> areaThatOwnsThis = new ArrayList<Area>();

	private List<Entity> entityOnTopOfThis = new ArrayList<Entity>();
	
	private boolean isPrimaryAreaTile = false;
	protected boolean isTravelPoint = false;

	private LinkedList<Direction> travelPointMap = new LinkedList<Direction>();
	private LinkedList<TravelPoint> travelPoints;
	
	private TileStorer storer = new TileStorer(this);
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
		return areaThatOwnsThis==null;
	}

	public void setOwner(Area area,boolean primary) {
		if(!areaThatOwnsThis.isEmpty()&&isPrimaryAreaTile){
			removeChild(areaThatOwnsThis.get(0));
		}
		this.areaThatOwnsThis.add(area);
		if(area != null){
			this.isPrimaryAreaTile = primary;
			if(primary){
				addChild(area);
				area.setTravelPoint(getTravelPoint());
				if(!isTravelPoint()){
					this.isTravelPoint = true;
					((Tileable)parent).reputTile(this);
				}
			}
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
		for(int i=0;i<tile.travelPoints.size();++i){
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
	@Override
	public Storer getStorer() {
		return storer;
	}

	public Iterable<Storer> getStorerIterator() {
		return new StorerIteratorIterator(
				new Iterable[]{areaThatOwnsThis,entityOnTopOfThis,travelPointMap,travelPoints},
				new Map[]{});
	}
}
