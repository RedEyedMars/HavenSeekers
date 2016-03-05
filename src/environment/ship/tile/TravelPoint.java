package environment.ship.tile;

import java.util.LinkedList;

import environment.Position;

public class TravelPoint extends Position {

	private LinkedList<TravelPoint> nexts = new LinkedList<TravelPoint>();
	public TravelPoint(int x, int y) {
		super(x, y);
	}
	public void addNext(TravelPoint point){
		nexts.add(point);
	}
	public boolean containsNext(TravelPoint point) {
		return nexts.contains(point);
	}
	public LinkedList<TravelPoint> getNexts(){
		return nexts;
	}
	public void adjust(Position offset) {
		setX(offset.getX()+getX());
		setY(offset.getY()+getY());
	}
}
