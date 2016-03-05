package gui.graphics.collections.hybrids;

import java.util.ArrayList;
import java.util.List;

import gui.graphics.GraphicView;
import gui.graphics.collections.ScrollableGraphicView;
import main.Hub;
import environment.Position;
import environment.Positionable;
import environment.ship.tile.Tile;
import environment.ship.tile.Tileable;

public class ScrollableGraphicGrid extends ScrollableGraphicView implements Tileable{

	private List<Positionable> elements = new ArrayList<Positionable>();
	private int gridWidth;
	private int gridHeight;
	public ScrollableGraphicGrid(int width, int height, float trueWidth, float trueHeight){
		super(trueWidth, trueHeight);
		this.gridHeight = height;
		this.gridWidth = width;
	}

	@Override
	public void addChild(GraphicView child){
		if(child instanceof Positionable){
			elements.add((Positionable)child);
			super.addChild(child);
		}
		else {
			super.addChild(child);
		}
	}
	@Override
	public void removeChild(GraphicView child){
		if(child instanceof Positionable){
			elements.remove((Positionable)child);
			super.removeChild(child);
		}
		else {
			super.removeChild(child);
		}
	}
	@Override
	public void adjust(float dx, float dy){
		super.adjust(dx, dy, trueWidth/this.gridWidth, trueHeight/this.gridHeight);
	}
	@Override
	public float offsetX(int index) {
		if(index>=elements.size()){
			return 0;
		}
		else {
			return ((float)elements.get(index).getPosition().getX())/(gridWidth);
		}
	}
	@Override
	public float offsetY(int index) {
		if(index>=elements.size()){
			return 0;
		}
		else {
			return ((float)elements.get(index).getPosition().getY())/gridHeight;
		}

	}

	@Override
	public void addTile(Tile tile) {
		addChild(tile);
		tile.onAddToShip(this);
	}
    @Override
	public void removeTile(Tile tile){
		removeChild(tile);
		tile.onRemoveFromShip(this);
	}	

	@Override
	public void clearTiles() {
		while(!elements.isEmpty()){
			removeTile((Tile) elements.get(0));
		}
		this.isUpToDate = false;
		Hub.updateView = true;
	}

	@Override
	public void adjustGridSize(int xSize, int ySize) {
		gridWidth = xSize;
		gridHeight = ySize;
		this.isUpToDate = false;
		Hub.updateView = true;
	}

	public Position getPositionFromFloats(float x, float y) {
		return new Position(x*gridWidth/trueWidth,y*gridHeight/trueHeight);
	}	

	@Override
	public void reputTile(Tile tile){
		removeTile(tile);
		addTile(tile);
	}
}
