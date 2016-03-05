package entity.choice.action;

import entity.Entity;
import misc.action.Action;
import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;

public class AddIntValueAction implements Action<FloatWrapper> {

	private FloatWrapper value;

	public AddIntValueAction(FloatWrapperAction<Entity> intWrapper){
		this.value = intWrapper.get();
	}
	
	@Override
	public void act(FloatWrapper subject) {
		subject.set(subject.get()+value.get());
	}

}
