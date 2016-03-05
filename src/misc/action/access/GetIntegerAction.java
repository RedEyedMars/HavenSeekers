package misc.action.access;

import misc.wrappers.FloatWrapper;
import entity.Entity;

public class GetIntegerAction extends FloatWrapperAction<Entity>{

	public GetIntegerAction(Float i){
		super(new FloatWrapper(i));
	}
	@Override
	public void act(Entity subject) {
	}

}
