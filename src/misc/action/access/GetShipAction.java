package misc.action.access;

import misc.action.WrapperAction;
import entity.Entity;

public class GetShipAction extends WrapperAction<Entity>{

	public GetShipAction(){
		super(null);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		get().set(subject.getShip());
	}

}
