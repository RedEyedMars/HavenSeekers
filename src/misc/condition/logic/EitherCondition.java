package misc.condition.logic;


import entity.Entity;
import misc.condition.Condition;


public class EitherCondition implements Condition<Entity>{
	private Condition<Entity> cond1;
	private Condition<Entity> cond2;

	public EitherCondition(Condition<Entity> condition1, Condition<Entity> condition2) {
		this.cond1 = condition1;
		this.cond2 = condition2;
	}

	@Override
	public boolean satisfied(Entity o) {
		return cond1.satisfied(o)||cond2.satisfied(o);
	}

}
