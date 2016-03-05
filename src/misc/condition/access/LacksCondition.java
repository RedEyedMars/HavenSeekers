package misc.condition.access;

import misc.action.WrapperAction;
import misc.condition.Condition;
import misc.wrappers.ObjectWrapper;

public class LacksCondition extends WrapperAction<Propertiable> implements Condition<ObjectWrapper<Propertiable>>{
	private String property;

	public LacksCondition(String property){
		super(null);
		this.property = property;
	}


	@Override
	public boolean satisfied(ObjectWrapper<Propertiable> o) {
		return o.get().getProperty(property).get()==0;
	}


	@Override
	public void act(Propertiable subject) {
		
	}
	
}
