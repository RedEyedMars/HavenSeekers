package environment;

import java.util.Observable;

public class Position extends Observable{
	private int x;
	private int y;
	public Position(float x, float y){
		this.x = (int)x;
		this.y = (int)y;
	}
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX(){ return x; }
	public int getY(){ return y; }
	public float distanceTo(Position p){
		return (float) Math.sqrt(Math.pow(x-p.x,2)+Math.pow(y-p.y,2));
	}
	public void setX(int i) {
		x = i;
		this.setChanged();
		this.notifyObservers();
	}
	public void setY(int y) {
		this.y = y;
		this.setChanged();
		this.notifyObservers();		
	}
	public void up() {
		this.setY(y+1);
	}
	public void down() {
		this.setY(y-1);
	}
	public void right() {
		this.setX(y+1);
	}
	public void left() {
		this.setX(y-1);
	}
	public Direction getDirectionTo(Position position) {
		float angle = (float) Math.atan2(position.getY()-getY(),position.getX()-getX());
		//System.out.println(this.toString()+"->"+position.toString()+"=="+(int)(angle*360/Math.PI/2)+"("+Direction.getDirection(angle)+")");
		return Direction.getDirection(angle);
	}
	@Override
	public int hashCode(){
		return (x+1)*10000+(y+1);
	}
	public static int[] dehash(int hash){
		return new int[]{hash/10000-1,hash%10000-1};
	}
	@Override
	public Position clone(){
		return new Position(x,y);
	}
	
	@Override
	public String toString(){
		return "("+x+","+y+")";
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Position){
			Position p = (Position)o;
			return p.x==x&&p.y==y;
		}
		else return false;
	}
	public void adjust(int x, int y){
		setX(getX()+x);
		setY(getY()+y);
	}
	public void adjust(Position position){
		setX(getX()+position.getX());
		setY(getY()+position.getY());
	}
}
