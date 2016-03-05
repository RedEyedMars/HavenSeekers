package environment.ship.tile.storage;

import environment.ship.tile.Tile;
import misc.condition.Condition;
import parser.StringHeirachy;
import storage.Storer;

public class TileStorer extends Storer<Tile>{


	public TileStorer(Tile self) {
		super(self);
	}

	@Override
	protected String storeSelf() {
		// TODO Auto-generated method stub
		return null;
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
