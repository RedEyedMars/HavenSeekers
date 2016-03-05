package location;

import java.util.ArrayList;

import main.Hub;

public class LocationType extends ArrayList<LocationPrototype> {
	private static final long serialVersionUID = -7612891127230738486L;
	private String name;
	private String satelliteType;
	public LocationType(String name){
		this.name = name;
	}
	public Location create() {
		return this.get((int) (this.size()*Math.random())).create();
	}
	public LocationType getSatelliteType() {
		return Hub.locationTypes.get(satelliteType);
	}
	public void setSatelliteType(String typeName) {
		this.satelliteType = typeName;
	}
	public String getName() {
		return name;
	}
}
