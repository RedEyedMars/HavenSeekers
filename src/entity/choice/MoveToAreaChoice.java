package entity.choice;

import environment.items.Area;
import environment.items.action.DecisionInAreaAction;
import environment.items.action.MoveToAreaAction;


public class MoveToAreaChoice extends Choice{

	//private Area area;

	public MoveToAreaChoice(Area area) {
		super("moveTo"+area.getName(),null);
		//this.area = area;
		addOnStart(new MoveToAreaAction(area.getName()));
		addAction(new DecisionInAreaAction(area.getName()));
	}

}
