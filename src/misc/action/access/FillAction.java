package misc.action.access;

import misc.Undoable;
import misc.action.Action;
import misc.wrappers.FloatWrapper;
import entity.Entity;
public class FillAction implements Action<Entity>, Undoable<Entity>{

	private FloatWrapperAction<Entity> first;
	private FloatWrapperAction<Entity> second;
	public FillAction(FloatWrapperAction<Entity> first, FloatWrapperAction<Entity> second){
		this.first = first;
		this.second = second;
	}
	@Override
	public void act(Entity subject) {
		first.act(subject);
		FloatWrapper i = first.get();
		second.act(subject);
		FloatWrapper j = second.get();
		j.set(j.get()+i.get());
	}
	@Override
	public void undo(Entity subject) {
		first.act(subject);
		FloatWrapper i = first.get();
		second.act(subject);
		FloatWrapper j = second.get();
		j.set(j.get()-i.get());
	}

}
