package location.action;

import location.Location;
import location.LocationPrototype;
import misc.Range;
import misc.action.Action;

public class SetTemperatureAction implements Action<Location>{

	private Range range;
	public SetTemperatureAction(Range range){
		this.range = range;
	}
	@Override
	public void act(Location subject) {
		subject.setTemperature(range.get());
	}

}
