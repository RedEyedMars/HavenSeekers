package misc.action.math;

import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;
import entity.Entity;

public class InverseAction extends FloatWrapperAction<Entity>{


	private FloatWrapperAction<Entity> action;
	public InverseAction(FloatWrapperAction<Entity> action){
		super(null);
		this.action = action;
	}
	@Override
	public void act(Entity subject) {
		FloatWrapper product = new FloatWrapper(1f);
		action.act(subject);
		product.set(1/action.get().get());
		set(product);
	}
	

}
