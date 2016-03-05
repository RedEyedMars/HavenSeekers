package environment;

public class Position {
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
	}
	public void setY(int y) {
		this.y = y;
		
	}
	public void up() {
		++y;
	}
	public void down() {
		--y;
	}
	public void right() {
		++x;
	}
	public void left() {
		--x;
	}
	public Direction getDirectionTo(Position position) {
		float angle = (float) Math.atan2(position.getY()-getY(),position.getX()-getX());
		//System.out.println(this.toString()+"->"+position.toString()+"=="+(int)(angle*360/Math.PI/2)+"("+Direction.getDirection(angle)+")");
		return Direction.getDirection(angle);
	}
	@Override
	public int hashCode(){
		return (x+1)*10000+(y+1)*3;
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
