package misc.condition;

import entity.Entity;
import misc.action.WrapperAction;
import misc.wrappers.ObjectWrapper;



@SuppressWarnings("rawtypes")
public class ObjectCondition implements Condition<Entity>{

	private WrapperAction<Entity> action;
	private Condition<ObjectWrapper> condition;
	private ObjectWrapper objectWrapper;

	@SuppressWarnings("unchecked")
	public ObjectCondition(
			ObjectWrapper objectWrapper, 
			Condition<ObjectWrapper> condition){
		this.objectWrapper = objectWrapper;
		if(objectWrapper instanceof WrapperAction<?>){
			this.action = (WrapperAction<Entity>) objectWrapper;
		}
		this.condition = condition;
	}

	@Override
	public boolean satisfied(Entity e) {
		action.set(objectWrapper);
		action.act(e);
		return condition.satisfied(objectWrapper);
	}

}
