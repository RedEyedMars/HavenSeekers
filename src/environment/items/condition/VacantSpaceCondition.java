package environment.items.condition;

import entity.Entity;
import environment.items.Area;
import misc.condition.Condition;

public class VacantSpaceCondition implements Condition<Entity>{
	
	private Area area;

	public VacantSpaceCondition(Area area){
		this.area = area;
	}

	@Override
	public boolean satisfied(Entity o) {
		return area.hasVacant();
	}

	
}
