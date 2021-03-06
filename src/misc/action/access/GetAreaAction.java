package misc.action.access;

import misc.action.WrapperAction;
import entity.Entity;

public class GetAreaAction extends WrapperAction<Entity>{

	public GetAreaAction(){
		super(null);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		get().set(subject.getArea());
	}

}
