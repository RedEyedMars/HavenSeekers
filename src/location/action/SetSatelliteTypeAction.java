package location.action;

import location.Location;
import location.LocationPrototype;
import location.LocationType;
import misc.Range;
import misc.action.Action;

public class SetSatelliteTypeAction implements Action<LocationType>{

	private String typeName;
	public SetSatelliteTypeAction(String syntax, String typeName){
		this.typeName= typeName;
	}
	@Override
	public void act(LocationType subject) {
		subject.setSatelliteType(typeName);
	}

}
