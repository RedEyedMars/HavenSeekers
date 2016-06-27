package environment.items.action;

import misc.action.Action;
import entity.Entity;
import environment.items.condition.UnOwnedTileCondition;

public class AddSpaceAction implements Action<Entity>{

	private String name;
	public AddSpaceAction(String name){
		this.name = name;
	}
	@Override
	public void act(Entity subject) {
		subject.getArea().addSlot(
				subject.getShip().getTileFromCondition(
						subject.getArea(),
						new UnOwnedTileCondition()));
	}

}
