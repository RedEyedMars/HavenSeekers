package environment.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import entity.Entity;
import entity.choice.Choice;
import entity.choice.MoveToAreaChoice;
import entity.choice.graphics.IconChoice;
import entity.choice.graphics.PopupChoice;
import entity.decision.Decision;
import environment.Direction;
import environment.Position;
import environment.Positionable;
import environment.items.Area;
import environment.ship.storage.ShipStorer;
import environment.ship.tile.Tile;
import environment.ship.tile.TravelPoint;
import environment.ship.tile.Wall;
import gui.graphics.GraphicView;
import gui.graphics.collections.hybrids.ScrollableGraphicGrid;
import location.Location;
import misc.condition.Condition;
import misc.condition.access.Propertiable;
import misc.wrappers.FloatWrapper;
import misc.wrappers.StorableFloatWrapper;
import storage.Storable;
import storage.Storer;
import storage.StorerIteratorIterator;

public class Ship implements Propertiable,Storable{
	private static int shipId = 0;
	private int id = shipId++;
	private final List<Tile> tiles = new ArrayList<Tile>();
	private final Map<Integer,Tile> tileMap = new HashMap<Integer,Tile>();
	private final Map<Integer,Wall> walls = new HashMap<Integer,Wall>();
	private final Map<String, Area> areas = new LinkedHashMap<String,Area>();
	private final Map<String,StorableFloatWrapper> values = new HashMap<String,StorableFloatWrapper>();
	private final List<Location> locations = new ArrayList<Location>();//0 = current location, 1=destination
	private final Map<String, Vehicle> vehicles = new HashMap<String, Vehicle>();
	private ScrollableGraphicGrid insideView = new ScrollableGraphicGrid(40,40,0.75f,1f){
		@Override
		public void addTile(Tile tile){
			addTileToShip(tile);
		}
		@Override
		public void removeTile(Tile tile){
			removeTileFromShip(tile);
		}
	};
	//private GraphicView outsideView = new GraphicEntity("blank");
	private ShipStorer storer = new ShipStorer(this);
	public Ship(){
		values.put("energy", FloatWrapper.s(100f));
		values.put("materials", FloatWrapper.s(0f));
		values.put("destination", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return hasDestination()?1f:0f;
			}
		}));
		setupTiles();
	}
	public void setupTiles(){
		for(int i=0;i<40;++i){
			for(int j=0;j<40;++j){
				Wall wall = new Wall(i,j);
				walls.put(wall.getPosition().hashCode(),wall);
				insideView.addChild(wall);
			}
		}
	}
	protected boolean hasDestination() {
		return locations.size()>1;
	}
	public Location getLocation(){
		return locations.get(0);
	}
	public FloatWrapper getProperty(String property) {
		return values.containsKey(property)?values.get(property):new FloatWrapper(0f);
	}

	@Override
	public boolean addProperty(String property, Float value) {
		if(values.containsKey(property)){
			return false;
		}
		values.put(property, FloatWrapper.s(value));
		return true;
	}
	public Area getArea(String area) {
		return areas.get(area);
	}
	public void addArea(Area area) {
		this.areas.put(area.getName(),area);
	}
	public Tile getTile(int x, int y){
		return tileMap.get(new Position(x,y).hashCode());
	}
	public Tile getTile(Position position){
		return tileMap.get(position.hashCode());
	}
	public void addTileToShip(Tile tile){
		if(!tileMap.containsKey(tile.getPosition().hashCode())){
			tileMap.put(tile.getPosition().hashCode(), tile);
			tiles.add(tile);
			insideView.addChild(tile);
		}
		else {
			tileMap.get(tile.getPosition().hashCode()).mergeWith(tile);
		}
		tile.onAddToShip(insideView);
		if(tile.isTravelPoint()){
			placeTravelPoint(tile);
		}
		for(Direction direction:Direction.directions){
			Position temp = direction.move(tile.getPosition());
			if(walls.containsKey(temp.hashCode())){
				Wall wall = walls.get(temp.hashCode());
				wall.addSideTiled(direction.getInverse());
			}
		}
		if(walls.containsKey(tile.getPosition().hashCode())){
			walls.get(tile.getPosition().hashCode()).tileCover(true);
		}
	}
	private void placeTravelPoint(Tile tile){
		for(Direction direction:Direction.directions){
			Position lookingAt = direction.move(tile.getPosition());
			if(tileMap.containsKey(lookingAt.hashCode())){
				Tile otherTile = tileMap.get(lookingAt.hashCode());
				if(!otherTile.isTravelPoint()){
					giveTilesTravelPoint(otherTile.getPosition(),tile.getTravelPoint(),direction,null,null,new ArrayList<Integer>());
				}
				else {
					tile.learnPointsFromTouchingTile(otherTile);
				}
			}
		}
	}
	private void giveTilesTravelPoint(Position position, TravelPoint toPointTo,Direction directionOfTravel,Position spawnLeft, Position spawnRight,List<Integer> alreadyUsed){
		if(!tileMap.containsKey(position.hashCode()))return;
		Tile nextTile = tileMap.get(position.hashCode());
		nextTile.learnTravelPoint(null,toPointTo, directionOfTravel.getInverse());
		alreadyUsed.add(position.hashCode());
		if(nextTile.isTravelPoint())return;
		Position next = directionOfTravel.move(position);
		Position left = directionOfTravel.getPerpendicular(1).move(position);
		Position right = directionOfTravel.getPerpendicular(-1).move(position);
		Position sendLeft = null;
		Position sendRight = null;
		if(spawnLeft==null&&tileMap.containsKey(left.hashCode())&&!alreadyUsed.contains(left.hashCode())){
			spawnLeft = left;
		}
		else if(spawnLeft!=null&&(!tileMap.containsKey(left.hashCode())||alreadyUsed.contains(left.hashCode()))){
			sendLeft = spawnLeft;
			spawnLeft = null;
		}
		if(spawnRight==null&&tileMap.containsKey(right.hashCode())&&!alreadyUsed.contains(right.hashCode())){
			spawnRight = right;
		}
		else if(spawnRight!=null&&(!tileMap.containsKey(right.hashCode())||alreadyUsed.contains(right.hashCode()))){
			sendRight = spawnRight;
			spawnRight = null;
		}
		if(tileMap.containsKey(next.hashCode())&&!alreadyUsed.contains(next.hashCode())&&!toPointTo.equals(next)){
			giveTilesTravelPoint(next,toPointTo,directionOfTravel,spawnLeft,spawnRight,alreadyUsed);				
		}
		else {
			if(spawnLeft!=null){
				sendLeft = spawnLeft;
			}
			if(spawnRight!=null){
				sendRight = spawnRight;
			}
		}
		if(sendLeft!=null){//&&!alreadyUsed.contains(sendLeft.hashCode())){
			giveTilesTravelPoint(sendLeft,toPointTo,directionOfTravel.getPerpendicular(1),null,null,alreadyUsed);
		}
		if(sendRight!=null){//&&!alreadyUsed.contains(sendRight.hashCode())) {
			giveTilesTravelPoint(sendRight,toPointTo,directionOfTravel.getPerpendicular(-1),null,null,alreadyUsed);
		}


	}
	public void removeTileFromShip(Tile tile){
		tileMap.remove(tile.getPosition().hashCode(), tile);
		tiles.remove(tile);
		insideView.removeChild(tile);
		tile.onRemoveFromShip(insideView);
		if(tile.isTravelPoint()){
			for(Tile t:tiles){
				t.unlearnTravelPoint(tile.getTravelPoint());
			}
		}
		for(Direction direction:Direction.directions){
			Position temp = direction.move(tile.getPosition());
			if(walls.containsKey(temp.hashCode())){
				Wall wall = walls.get(temp.hashCode());
				wall.removeTile(direction.getInverse());
			}
		}
		if(walls.containsKey(tile.getPosition().hashCode())){
			walls.get(tile.getPosition().hashCode()).tileCover(false);
		}
	}
	@SuppressWarnings("unchecked")
	public void fillWithChoices(Entity entity) {
		if(areas.isEmpty())return;
		IconChoice<Area>[] icons = new IconChoice[areas.size()];
		int i=0;
		for(String name:areas.keySet()){
			icons[i] = areas.get(name).getIcon();
			++i;
		}
		Decision<Area> moveToDecision = new Decision<Area>(new PopupChoice<Area>(entity.getName()+" must chose between areas to go to.",icons));
		for(String areaName:areas.keySet()){
			Area area = areas.get(areaName);
			Choice choice = new MoveToAreaChoice(area);
			moveToDecision.add(choice);
		}
		entity.setNextDecision(moveToDecision);
	}

	public Vehicle getVehicle(String name) {
		return vehicles .get(name);
	}
	public GraphicView getInsideView(){
		return insideView;
	}

	public Tile getTileFromCondition(Positionable target,Condition<Tile> condition) {
		Position targetPositionLeft = target.getPosition().clone();
		targetPositionLeft.left();
		if(condition.satisfied(getTile(targetPositionLeft))){
			return getTile(targetPositionLeft);
		}
		Position targetPositionRight = target.getPosition().clone();
		targetPositionRight.right();
		if(condition.satisfied(getTile(targetPositionRight))){
			return getTile(targetPositionRight);
		}
		Position targetPositionDown = target.getPosition().clone();
		targetPositionDown.down();
		if(condition.satisfied(getTile(targetPositionDown))){
			return getTile(targetPositionDown);
		}
		Position targetPositionUp = target.getPosition().clone();
		targetPositionUp.up();
		if(condition.satisfied(getTile(targetPositionUp))){
			return getTile(targetPositionUp);
		}
		targetPositionLeft.up();
		if(condition.satisfied(getTile(targetPositionLeft))){
			return getTile(targetPositionLeft);
		}
		targetPositionRight.down();
		if(condition.satisfied(getTile(targetPositionRight))){
			return getTile(targetPositionRight);
		}
		targetPositionDown.left();
		if(condition.satisfied(getTile(targetPositionDown))){
			return getTile(targetPositionDown);
		}
		targetPositionUp.right();
		if(condition.satisfied(getTile(targetPositionUp))){
			return getTile(targetPositionUp);
		}
		return null;
	}
	public Position getPositionFromFloats(float x, float y) {
		return insideView.getPositionFromFloats(x,y);
	}
	public boolean isWalled(Position receivedPosition) {
		return walls.get(receivedPosition.hashCode()).isVisible();
	}
	@Override
	public Storer getStorer(){
		return storer;
	}
	@Override
	public Iterable<Storer> getStorerIterator() {
		return new StorerIteratorIterator(
				new Iterable[]{tiles,locations},
				new Map[]{walls,areas,values});//,vehicles});
	}
	public int getId(){
		return id;
	}
	public void setId(int i){
		id = i;
	}
}
