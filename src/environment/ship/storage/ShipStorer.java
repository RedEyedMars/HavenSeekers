package environment.ship.storage;

import environment.ship.Ship;
import misc.condition.Condition;
import parser.StringHeirachy;
import storage.Storer;
import storage.StorerIteratorIterator;

public class ShipStorer extends Storer<Ship> {

	public ShipStorer(Ship self) {
		super(self);
	}

	@Override
	protected String storeSelf() {
		return "SHIP "+self.getId()+" ";
	}

	@Override
	protected Iterable<Condition<String>> loadRules() {
		return null;
	}

	@Override
	protected void loadSelf(StringHeirachy input) {
	}

}
