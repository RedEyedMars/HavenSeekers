package misc.condition;


public interface ForcableCondition <Subject> extends Condition<Subject>{
	public void force(Subject o);
}
