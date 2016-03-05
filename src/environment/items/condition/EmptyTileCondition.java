package environment.items.condition;

import misc.condition.Condition;
import environment.ship.tile.Tile;

public class EmptyTileCondition implements Condition<Tile>{

	@Override
	public boolean satisfied(Tile t) {
		return t.isEmpty();
	}

}
