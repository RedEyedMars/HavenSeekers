package environment;

public class Direction {

	public static final Direction right = new Direction(1,0);
	public static final Direction up = new Direction(0,1);
	public static final Direction left = new Direction(-1,0);
	public static final Direction down = new Direction(0,-1);
	public static final Direction[] directions = new Direction[]{right,up,down,left};
	
	private int x = 0;
	private int y = 0;
	public Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public boolean contains(Direction dir) {
		return y==dir.y||y==dir.y;
	}
	public Direction add(Direction dir){
		Direction d = new Direction(x,y);
		if(dir.x!=0){
			d.x = dir.x;
		}
		if(dir.y!=0){
			d.y= dir.y;
		}
		return d;
	}
	
	public Position move(Position position){
		return new Position(position.getX()+x,position.getY()+y);
	}
	
	@Override
	public String toString(){
		return (x==1?"right":x==-1?"left":"")+(y==1?"up":y==-1?"down":"");
	}
	public boolean equals(Object o){
		if(o instanceof Direction){
			Direction d = (Direction)o;
			return d.x==x&&d.y==y;
		}
		else return false;
	}
	public Direction getPerpendicular(int factor) {
		int[] pos = {x,y};
		for(int i=0;i<pos.length;++i){
			if(pos[i]==0){
				pos[i]=factor;
			}
			else {
				pos[i]=0;
			}
		}
		return new Direction(pos[0],pos[1]);
	}
	public int getX() {
		return x;
	}
	public int getY(){
		return y;
	}
	public Direction clone(){
		return new Direction(x,y);
	}
	public static Direction getDirection(float angle) {
		if(angle<0){
			angle=(float) (2*Math.PI-Math.abs(angle));
		}
		angle-=Math.PI/4f;
		if(angle<0){
			return Direction.right.clone();
		}
		if(angle<Math.PI/2f){
			return Direction.up.clone();
		}
		if(angle<Math.PI){
			return Direction.left.clone();
		}
		if(angle<Math.PI*3f/2f){
			return Direction.down.clone();
		}
		return Direction.right.clone();
	}
	public static float percentOfPie(float angle) {
		angle-=Math.PI/4f;
		if(angle<0){
			angle +=Math.PI/2f;
		}
		else if(angle<Math.PI/2f){
		}
		else if(angle<Math.PI){
			angle-=Math.PI/2f;
		}
		else if(angle<Math.PI*3f/2f){
			angle-=Math.PI;
		}
		else {
			angle-=Math.PI*3f/2f;
		}
		return (float) (angle/(Math.PI/2f));
	}
	public Direction getInverse() {
		return new Direction(-x,-y);
	}

	public int getIndex(){
		if(getX()==1&&getY()==0){
			return 0;
		}
		else if(getX()==0&&getY()==1){
			return 1;
		}
		else if(getX()==-1&&getY()==0){
			return 2;
		}
		else if(getX()==0&&getY()==-1){
			return 3;
		}
		else return -1;
	}
	public Direction[] getAllButInverse() {
		return new Direction[]{this,getPerpendicular(1),getPerpendicular(-1)};
	}
}
