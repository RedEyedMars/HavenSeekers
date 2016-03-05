package misc.action.access;

import misc.action.WrapperAction;
import misc.condition.access.Propertiable;
import misc.wrappers.FloatWrapper;
import misc.wrappers.ObjectWrapper;
import entity.Entity;

public class FromAction extends FloatWrapperAction<Entity>{

	private WrapperAction<Entity> action;
	private String property;
	private ObjectWrapper<Propertiable> wrapper;
	public FromAction(WrapperAction<Entity> action,String property){
		super(null);
		this.action = action;
		this.property = property;
		this.wrapper = new ObjectWrapper<Propertiable>(null);
	}
	@Override
	public void act(Entity subject) {
		action.set(wrapper);
		action.act(subject);
		FloatWrapper i = wrapper.get().getProperty(property);
		if(i!=null){
			set(i);
		}
		else {
			set(new FloatWrapper(0f));
		}
	}

}
