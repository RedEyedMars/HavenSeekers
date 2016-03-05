package environment.ship.tile;

import java.util.ArrayList;
import java.util.List;

import environment.Direction;
import environment.Position;
import gui.Gui;
import gui.graphics.collections.GraphicGrid;
import gui.inputs.MotionEvent;
import gui.inputs.MouseListener;
import gui.menu.BlankOption;
import gui.menu.ButtonOption;
import gui.menu.DropObjectListener;
import gui.menu.MenuItem;
import gui.menu.Modifiable;

public class Corridor extends Tile implements Modifiable{

	private List<Tile> interior = new ArrayList<Tile>();
	private Direction direction1;
	private Direction direction2;
	private Tile endPoint;
	public Corridor(int xi, int yi, int xf, int yf) {
		super(xi,
				yi);
		this.isTravelPoint = true;
		this.endPoint = new Tile(xf,yf);
		this.endPoint.isTravelPoint = true; 
		this.direction1 = getPosition().getDirectionTo(new Position(xf,yi));
		this.direction2 = getPosition().getDirectionTo(new Position(xi,yf));
		//setup(xf-xi,yf-yi,direction1,direction2);
	}
	private void setup(int size1, int size2, Direction d1, Direction d2){
		this.clearDirections();
		endPoint.clearDirections();
		Position cur = getPosition();
		if(Math.abs(size1)>0){
			cur = d1.move(cur);
			for(int i=0;i<Math.abs(size1)-1;++i,cur=d1.move(cur)){
				Tile newTile = new Tile(cur);
				interior.add(newTile);
			}
			Tile newTile = new Tile(cur);
			interior.add(newTile);
		}
		if(Math.abs(size2)>0){
			cur=d2.move(cur);
			for(int i=0;i<Math.abs(size2)-1;++i,cur=d2.move(cur)){
				Tile newTile = new Tile(cur);
				interior.add(newTile);			
			}
		}
		this.endPoint.adjustPostionTo(cur);
		this.interior.add(endPoint);
	}

	@Override
	public void onAddToShip(Tileable ship){
		for(Tile tile:interior){
			ship.addTile(tile);
		}
	}
	@Override
	public void onRemoveFromShip(Tileable ship){
		for(Tile tile:interior){
			ship.removeTile(tile);
		}
	}
	@Override
	public void modify(String operation, Object... arguments) {
		if(operation.equals("adjustEndPoint")){
			int xSize = (int) arguments[0];
			int ySize = (int) arguments[1];
			if(xSize==0&&ySize==0)return;
			interior.clear();
			this.direction1 = getPosition().getDirectionTo(new Position(getPosition().getX()+xSize,getPosition().getY()));
			this.direction2 = getPosition().getDirectionTo(new Position(getPosition().getX(),getPosition().getY()+ySize));
			setup(xSize,ySize,direction1,direction2);
		}
		else if(operation.equals("direction")){
		}
	}

	@Override
	public void update(){

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
	public Corridor clone(){
		return new Corridor(getPosition().getX(),getPosition().getY(),endPoint.getPosition().getX(),endPoint.getPosition().getY());
	}

	private MouseListener placer = null;
	public static MenuItem makeMenuItem(){
		GraphicGrid parent = new GraphicGrid(2,2);
		final Corridor corridor = new Corridor(0,0,1,1);
		parent.addChild(corridor.clone());
		return new MenuItem(
				corridor,
				new float[]{0.8f,0.1f,0.10f},
				parent,
				new BlankOption(),
				new ButtonOption("Place","place"){
					private Corridor clone=corridor.clone();
					@Override
					public void onPress(){
						if(corridor.placer==null){							
							clone.placer = clone.createPlacer(
									corridor,
									(DropObjectListener) 
									getParentWithCondition(DropObjectListener.conditions.getInstanceCondition()));
							corridor.placer=clone.placer;
							Gui.giveOnClick(clone.placer);
							clone = corridor.clone();
						}
					}
				}){
			@Override
			public boolean onClick(MotionEvent event){
				return superOnClick(event);
			}
		};
	}
	protected MouseListener createPlacer(final Corridor original,final DropObjectListener graphicView) {
		final Corridor self = this;
		return new MouseListener(){
			private boolean down = false;
			@Override
			public boolean onClick(MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_UP&&event.getX()<0.75f){
					if(!down){
						if(graphicView.drop(event.getX(), event.getY(), self)){
							x = getPosition().getX();
							y = getPosition().getY();
							down = true;
						}
					}
					else {
						Gui.removeOnClick(this);
						self.placer = null;
						original.placer = null;
					}
					return true;
				}
				else {
					return false;
				}
			}

			@Override
			public boolean onHover(MotionEvent event) {
				if(down&&event.getX()<0.75f){
					Position endPoint = new Position(0,0);
					Tileable parent = (Tileable) self.parent;
					parent.removeTile(self);
					if(graphicView.drop(event.getX(), event.getY(), endPoint)){
						modify("adjustEndPoint",endPoint.getX()-getPosition().getX(),endPoint.getY()-getPosition().getY());
					}
					parent.addTile(self);
					return true;
				}
				else return false;
			}

			@Override
			public void onMouseScroll(int distance) {				
			}

		};
	}
}
