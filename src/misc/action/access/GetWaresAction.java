package misc.action.access;

import misc.action.WrapperAction;
import entity.Entity;

public class GetWaresAction extends WrapperAction<Entity>{

	public GetWaresAction(){
		super(null);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		get().set(subject.getShip().getArea("Wares"));
	}

}
