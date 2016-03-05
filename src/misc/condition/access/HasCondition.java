package misc.condition.access;

import misc.action.WrapperAction;
import misc.action.access.FloatWrapperAction;
import misc.condition.Condition;
import misc.wrappers.ObjectWrapper;

public class HasCondition extends WrapperAction<Propertiable> implements Condition<ObjectWrapper<Propertiable>>{
	private String property;
	private FloatWrapperAction<ObjectWrapper<Propertiable>> value;

	public HasCondition( FloatWrapperAction<ObjectWrapper<Propertiable>> intWrapperAction,String property){
		super(null);
		this.property = property;
		this.value = intWrapperAction;
	}


	@Override
	public boolean satisfied(ObjectWrapper<Propertiable> o) {
		value.act(o);
		return o.get().getProperty(property).get()>=value.get().get();
	}


	@Override
	public void act(Propertiable subject) {
		
	}
	
}
