package location.action;

import location.Location;
import location.LocationPrototype;
import misc.Range;
import misc.action.Action;

public class SetMoonsAction implements Action<Location>{

	private Range range;
	public SetMoonsAction(Range range){
		this.range = range;
	}
	@Override
	public void act(Location subject) {
		int numberOfSats = (int)(float)range.get();
		for(int i=0;i<numberOfSats;++i){
			subject.addSatellite(subject.generateSatellite());
		}
	}

}
