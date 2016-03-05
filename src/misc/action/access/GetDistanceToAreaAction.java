package misc.action.access;

import misc.wrappers.FloatWrapper;
import entity.Entity;
import environment.items.Area;

public class GetDistanceToAreaAction extends FloatWrapperAction<Entity> {

	private Area area;

	public GetDistanceToAreaAction(Area area) {
		super(null);
		this.area = area;
	}

	@Override
	public void act(Entity subject) {
		set(new FloatWrapper(subject.getDistanceToArea(area)));
	}

}
