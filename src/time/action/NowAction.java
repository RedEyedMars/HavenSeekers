package time.action;

import main.Hub;
import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;

public class NowAction<Subject> extends FloatWrapperAction<Subject> {

	public NowAction() {
		super(Hub.ticker);
	}

	@Override
	public void act(Subject subject) {
		set(new FloatWrapper(Hub.ticker.get()));
	}
	
}
