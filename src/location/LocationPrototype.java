package location;

import java.util.ArrayList;
import java.util.List;

import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.condition.Condition;

public class LocationPrototype extends ActionAcceptor<Location>{
	private static final long serialVersionUID = 4988334153136193301L;
	private String name;
	private List<Condition<Location>> conditions = new ArrayList<Condition<Location>>();
	private List<LocationPrototype> subTypes = new ArrayList<LocationPrototype>();
	private LocationType type;
	public LocationPrototype(String value, LocationType type) {
		this.name = value;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void addCondition(Condition<Location> c) {
		this.conditions.add(c);
	}
	public void addSubType(LocationPrototype type){
		this.subTypes.add(type);
	}

	public Location create(){
		if(subTypes.isEmpty()){
			Location location = new Location(type);
			for(Action<Location> action:this){
				action.act(location);
			}
			return location;
		}
		else {
			return subTypes.get((int) (subTypes.size()*Math.random())).create();
		}
	}

}
