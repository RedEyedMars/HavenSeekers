package location.action;

import location.Location;
import misc.Range;
import misc.action.Action;

public class SetMassAction implements Action<Location>{

	private Range range;
	public SetMassAction(Range range){
		this.range = range;
	}
	@Override
	public void act(Location subject) {
		subject.setMass(range.get());
	}

}
