package misc.action.access;

import entity.Entity;
import misc.action.Action;

public class PrintAction implements Action<Entity> {
	private FloatWrapperAction<Entity> action;

	public PrintAction(FloatWrapperAction<Entity> action){
		this.action = action;
	}

	@Override
	public void act(Entity subject) {
		action.act(subject);
		System.out.println("we print here?");
		System.out.println(action.get().get());
	}
}
