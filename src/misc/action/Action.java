package misc.action;

public interface Action <Subject extends Object> {
	public void act(Subject subject);
}
