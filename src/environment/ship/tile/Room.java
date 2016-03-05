package environment.ship.tile;

import java.util.ArrayList;
import java.util.List;

import environment.Direction;
import environment.Position;
import gui.graphics.collections.GraphicGrid;
import gui.menu.MenuItem;
import gui.menu.Modifiable;
import gui.menu.RotaterMenuOption;
import gui.menu.ScrollerMenuOption;

public class Room extends Tile implements Modifiable{

	private List<Tile> interior = new ArrayList<Tile>();
	private Direction direction;
	private int size;
	private float offset;
	public Room(float offset, int size, Direction direction) {
		super((int)(direction.getY()==0?
				direction.getInverse().getX()*size/2f+size/2f:
				offset*size
				),
			  (int)(direction.getX()==0?
						direction.getInverse().getY()*size/2f+size/2f:
						offset*size));
		this.offset = offset;
		this.size = size;
		this.direction = direction.clone();
		this.isTravelPoint = true;
		setup(size,direction);
	}
	private void setup(int size, Direction direction){
		redoPosition();
		int xOffset = direction.getX()>=0?direction.getX():0;
		int yOffset = direction.getY()>=0?direction.getY():0;
		Position directionPoint = direction.move(getPosition());
		for(int i=xOffset;i<size+xOffset;++i){
			for(int j=yOffset;j<size+yOffset;++j){
				Tile newTile = new Tile(new Position(i,j));
				if(directionPoint.getX()==i&&directionPoint.getY()==j){
					//newTile.addDirection(this.getTravelPoint(),direction.getInverse());					
				}
				else {
					//newTile.addDirection(this.getTravelPoint(),newTile.getPosition().getDirectionTo(directionPoint));
				}
				interior.add(newTile);
			}
		}
	}

	
	@Override
	public void onAddToShip(Tileable ship){
		for(Tile tile:interior){
			ship.addTile(tile);
		}
	}
	@Override
	public void modify(String operation, Object... arguments) {
		if(operation.equals("size")){
			size = (int) arguments[0];
			Tileable parent = (Tileable) this.parent;
			interior.clear();
			if(direction.getX()==0)getPosition().setX((size-1)%2==0?(size/2):(size/2-1));
			if(direction.getY()==0)getPosition().setY((size-1)%2==0?(size/2):(size/2-1));
			setup(size,this.direction);
			parent.adjustGridSize(size+Math.abs(direction.getX()),size+Math.abs(direction.getY()));
			parent.clearTiles();
			parent.addTile(this);
		}
		else if(operation.equals("direction")){
			float angle = (float)arguments[0];
			Tileable parent = (Tileable) this.parent;
			interior.clear();
			direction = Direction.getDirection(angle).getInverse();
			if(direction.equals(Direction.right)||direction.equals(Direction.down)){
				offset = (1f-Direction.percentOfPie(angle));
			}
			else {
				offset = Direction.percentOfPie(angle);
			}
			setup(size,this.direction);
			parent.adjustGridSize(size+Math.abs(direction.getX()),size+Math.abs(direction.getY()));
			parent.clearTiles();
			parent.addTile(this);
		}
	}
	
	@Override
	public void update(){
		
	}
	
	private void redoPosition(){
		getPosition().setX(((int)(direction.getY()==0?
				direction.getInverse().getX()*size/2f+size/2f:
				offset*size
				)));
		getPosition().setY(
			  (int)(direction.getX()==0?
					direction.getInverse().getY()*size/2f+size/2f:
						offset*size));
	}


	public static MenuItem makeMenuItem(int min, int max){
		int size = (max+min)/2;
		GraphicGrid parent = new GraphicGrid(size+1,size);
		Room room = new Room(0.5f,size,Direction.right);
		parent.addTile(room);
		return new MenuItem(
				room,
				new float[]{0.65f,0.1f,0.25f},
				parent,
				new RotaterMenuOption("direction"),
				new ScrollerMenuOption(true,"size",min,max));
	}
	@Override
	public void adjustPosition(Position offset) {
		offset = 
				new Position(offset.getX()-getPosition().getX(),
		                     offset.getY()-getPosition().getY());
		super.adjustPosition(offset);
		for(Tile tile:interior){
			tile.adjustPosition(offset);
		}
	}
	@Override
	public Room clone(){
		return new Room(offset,size,direction);
	}
}
