package gui.graphics.collections;

import gui.graphics.GraphicView;
import main.Hub;
import environment.Positionable;
import environment.ship.tile.Tile;
import environment.ship.tile.Tileable;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GraphicGrid extends GraphicView implements Tileable, Observer{
	private List<Positionable> elements = new ArrayList<Positionable>();
	private int gridWidth;
	private int gridHeight;
	public GraphicGrid(int width, int height){
		super();
		this.gridHeight = height;
		this.gridWidth = width;
	}

	@Override
	public void addChild(GraphicView child){
		if(child instanceof Positionable){
			elements.add((Positionable)child);
			((Positionable)child).getPosition().addObserver(this);
			super.addChild(child);
		}
		else {
			System.err.println("GraphicGrid Error: Couldn't add child, didn't inherit interface Positionable");
		}
	}
	@Override
	public void removeChild(GraphicView child){
		if(child instanceof Positionable){
			elements.remove((Positionable)child);
			((Positionable)child).getPosition().deleteObserver(this);
			super.removeChild(child);
		}
		else {
			System.err.println("GraphicGrid Error: Couldn't remove child, didn't inherit interface Positionable");
		}
	}
	@Override
	public void adjust(float dx, float dy){
		super.adjust(dx, dy, dx/this.gridWidth, dy/this.gridHeight);
	}
	@Override
	public float offsetX(int index) {
		return getWidth()*((float)elements.get(index).getPosition().getX())/gridWidth;
	}
	@Override
	public float offsetY(int index) {
		return getHeight()*((float)elements.get(index).getPosition().getY())/gridHeight;

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
		elements.clear();
		while(!children.isEmpty()){
			removeChild(0);
		}
	}

	@Override
	public void adjustGridSize(int xSize, int ySize) {
		gridWidth = xSize;
		gridHeight = ySize;
		this.isUpToDate = false;
		Hub.updateView = true;
	}
	
	@Override
	public void reputTile(Tile tile){
		removeTile(tile);
		addTile(tile);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.isUpToDate = false;
		Hub.updateView = true;
	}
}
