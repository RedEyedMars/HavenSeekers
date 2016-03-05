package location.action;

import location.materials.ResourcePrototype;
import misc.action.Action;
import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;
import entity.Entity;

public class DecreaseResourceAction implements Action<Entity> {
	private FloatWrapperAction by;
	private FloatWrapperAction to;

	public DecreaseResourceAction(FloatWrapperAction by, FloatWrapperAction to){
		this.by = by;
		this.to = to;
	}

	@Override
	public void act(Entity subject) {
		by.act(subject);
		to.act(subject);
		FloatWrapper value = subject.getShip().getLocation().getProperty(ResourcePrototype.translateResourceId(to.getFloat()));
		value.set(value.get()-by.getFloat());
	}
}
