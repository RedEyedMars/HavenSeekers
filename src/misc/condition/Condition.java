package misc.condition;

public interface Condition <Subject extends Object>{
	public boolean satisfied(Subject o);
}
