package environment.items.action;

import misc.action.Action;
import entity.Entity;
import entity.choice.ChoicePrototype;
import entity.choice.graphics.PopupChoice;
import entity.decision.Decision;
import environment.items.Area;
import environment.items.AreaPrototype;

public class DecisionInAreaAction implements Action<Entity>{

	private Area area;
	public DecisionInAreaAction(Area area){
		this.area = area;
	}
	@Override
	public void act(Entity entity) {
		AreaPrototype proto = area.getPrototype();
		Decision<ChoicePrototype> decision = new Decision<ChoicePrototype>(
				PopupChoice.createPopup(entity.filterText("NAME must chose between areas to go to."),proto));
		for(int i = 0;i<area.size();++i){
			decision.add(proto.getChoicePrototype(i).make());
		}
		entity.setNextDecision(decision);
	}

}
