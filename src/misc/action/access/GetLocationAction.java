package misc.action.access;

import misc.action.WrapperAction;
import entity.Entity;

public class GetLocationAction extends WrapperAction<Entity>{

	public GetLocationAction(){
		super(null);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		get().set(subject.getShip().getLocation());
	}

}
