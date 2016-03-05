package location.action;

import location.Location;
import misc.Range;
import misc.action.Action;

public class SetDensityAction implements Action<Location>{

	private Range range;
	public SetDensityAction(Range range){
		this.range = range;
	}
	@Override
	public void act(Location subject) {
		subject.setDensity((int)(float)range.get());
	}

}
