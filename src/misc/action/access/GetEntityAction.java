package misc.action.access;

import misc.action.WrapperAction;
import entity.Entity;

public class GetEntityAction extends WrapperAction<Entity>{

	public GetEntityAction(){
		super(null);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		get().set(subject);
	}

}
