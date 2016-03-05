package environment.items.action;

import entity.Entity;
import location.materials.ResourcePrototype;
import misc.action.Action;
import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;

public class IncreaseWaresAction implements Action<Entity> {
	private FloatWrapperAction<Entity> by;
	private FloatWrapperAction<Entity> to;

	public IncreaseWaresAction(FloatWrapperAction<Entity> by, FloatWrapperAction<Entity> to){
		this.by = by;
		this.to = to;
	}

	@Override
	public void act(Entity subject) {
		by.act(subject);
		to.act(subject);
		String resource = ResourcePrototype.translateResourceId(to.getFloat());
		if(!subject.getShip().getArea("wares").addProperty(resource,by.getFloat())){
			FloatWrapper value = subject.getShip().getArea("wares").getProperty(resource);
			value.set(value.get()+by.getFloat());
		}
	}
}
