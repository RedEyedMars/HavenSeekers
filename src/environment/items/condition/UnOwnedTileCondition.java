package environment.items.condition;

import misc.condition.Condition;
import environment.ship.tile.Tile;

public class UnOwnedTileCondition implements Condition<environment.ship.tile.Tile>{

	@Override
	public boolean satisfied(Tile t) {
		return t.isUnowned();
	}

}
