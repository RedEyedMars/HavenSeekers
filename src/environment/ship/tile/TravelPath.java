package environment.ship.tile;

import java.util.LinkedList;
import java.util.TreeSet;

public class TravelPath extends LinkedList<TravelPoint> implements Comparable<TravelPath>{

	private static final long serialVersionUID = 7225233479208063797L;
	private float distance = 0;
	private TravelPoint previous = null;
	@Override
	public boolean add(TravelPoint point){
		if(previous!=null){
			distance += previous.distanceTo(point);
		}
		previous = point;
		return super.add(point);
	}
	@Override
	public int compareTo(TravelPath path) {
		if(path.hashCode()==this.hashCode())return 0;
		int ret = (int) (this.distance*1000-path.distance*1000);
		if(ret!=0){
			return ret;
		}
		ret = this.size()-path.size();
		if(ret!=0){
			return ret;
		}
		else return -1;
	}
	public static TravelPath findQuickest(TravelPoint a, TravelPoint b){
		TreeSet<TravelPath> paths = new TreeSet<TravelPath>();
		TravelPath path = new TravelPath();
		path.add(a);
		paths.add(path);
		while(!paths.isEmpty()){
			TravelPath first = paths.first();
			for(TravelPoint next:first.getLast().getNexts()){
				TravelPath temp = new TravelPath();
				for(TravelPoint points:first){
					temp.add(points);
				}
				temp.add(next);
				paths.add(temp);
			}
			if(paths.first().getLast().equals(b)){
				break;
			}
			paths.remove(first);
		}
		//System.out.println(paths.first());
		return paths.first();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(TravelPoint point:this){
			builder.append(point);
			if(!point.equals(getLast())){
				builder.append("->");
			}
		}
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		return toString().equals(obj.toString());
	}
	public float getDistance() {
		return distance;
	}
}
