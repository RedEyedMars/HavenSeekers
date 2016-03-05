package misc.action.access;

import misc.action.Action;
import entity.Entity;

public class SetAction implements Action<Entity>{

	private FloatWrapperAction<Entity> action;
	private String property;
	public SetAction(String property, FloatWrapperAction<Entity> action){
		this.action = action;
		this.property = property;
	}
	@Override
	public void act(Entity subject) {
		action.act(subject);
		subject.addProperty(property,action.get().get());
		subject.getProperty(property).set(action.get().get());
	}

}
