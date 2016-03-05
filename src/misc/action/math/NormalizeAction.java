package misc.action.math;

import misc.action.access.FloatWrapperAction;
import misc.condition.Condition;
import misc.wrappers.FloatWrapper;
import entity.Entity;

public class NormalizeAction extends FloatWrapperAction<Entity>{

	private FloatWrapperAction<Entity> number;
	private Condition<Entity> condition;
	public NormalizeAction(FloatWrapperAction<Entity> number, Condition<Entity> condition){
		super(null);
		this.number = number;
		this.condition = condition;
	}
	@Override
	public void act(Entity subject) {
		if(condition.satisfied(subject)){
			number.act(subject);
			set(new FloatWrapper(number.get().get()));
		}
		else {
			set(new FloatWrapper(0f));
		}
	}

}
