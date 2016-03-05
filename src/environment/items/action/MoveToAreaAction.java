package environment.items.action;

import time.action.OnStartAction;
import main.Hub;
import misc.Undoable;
import misc.action.access.GetDistanceToAreaAction;
import misc.action.access.GetIntegerAction;
import entity.Entity;
import entity.choice.Choice;
import entity.state.State;
import environment.items.Area;

public class MoveToAreaAction extends OnStartAction{
	private static final long serialVersionUID = 4453577396525215602L;
	private String area;
	public MoveToAreaAction(String areaName) {
		super("moving", "NAME is moving to "+areaName);
		this.area = areaName;
	}


	@Override
	public void act(Entity subject) {
		Area area = subject.getShip().getArea(this.area);
		if(first){
			add(new GetDistanceToAreaAction(area)); 
			start = new GetIntegerAction(0f);
			subject.setDirection(subject.getPosition().getDirectionTo(area.getPosition()));
		}
		super.act(subject);
		if(Hub.ticker.get()%100==0){
			subject.moveToward(area);		
		}
	}

}
