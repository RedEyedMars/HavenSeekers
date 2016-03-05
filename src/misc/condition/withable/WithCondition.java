package misc.condition.withable;

import misc.condition.Condition;

public class WithCondition implements Condition<Withable>{

	private String mustHave;

	public WithCondition(String mustHave){
		this.mustHave = mustHave;
	}

	@Override
	public boolean satisfied(Withable o) {
		return o.with(mustHave);
	}

}
