package location.storage;

import location.Location;
import misc.condition.Condition;
import parser.StringHeirachy;
import storage.Storer;

public class LocationStorer extends Storer<Location>{

	public LocationStorer(Location self) {
		super(self);
	}

	@Override
	protected String storeSelf() {
		return "Location "+" ";
	}

	@Override
	protected Iterable<Condition<String>> loadRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadSelf(StringHeirachy input) {
		// TODO Auto-generated method stub
		
	}
	

}
