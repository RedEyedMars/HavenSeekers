package misc.action.math;

import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;
import entity.Entity;

public class RandomAction extends FloatWrapperAction<Entity>{

	public RandomAction(){
		super(new FloatWrapper(0f));
	}
	@Override
	public void act(Entity subject) {
		set(new FloatWrapper((float)Math.random()));
	}

}
