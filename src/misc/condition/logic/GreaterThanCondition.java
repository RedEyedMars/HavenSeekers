package misc.condition.logic;

import misc.action.access.FloatWrapperAction;
import misc.condition.Condition;
import entity.Entity;

public class GreaterThanCondition implements Condition<Entity>{

	private FloatWrapperAction<Entity> first;
	private FloatWrapperAction<Entity> second;

	public GreaterThanCondition( FloatWrapperAction<Entity> first,FloatWrapperAction<Entity> second){
		this.first = first;
		this.second = second;
	}


	@Override
	public boolean satisfied(Entity o) {
		first.act(o);
		second.act(o);
		return first.get().get() > second.get().get();
	}

}
