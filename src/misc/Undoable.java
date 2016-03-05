package misc;

public interface Undoable<Subject> {
	public void undo(Subject subject);
}
