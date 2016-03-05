package time.action;

import entity.Entity;
import main.Hub;
import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;

public class SinceAction extends FloatWrapperAction<Entity> {

	private String property;

	public SinceAction(String property) {
		super(null);
		this.property = property;
	}

	@Override
	public void act(Entity subject) {
		set(new FloatWrapper(Hub.ticker.get()-subject.getProperty(property).get()));
	}

}
