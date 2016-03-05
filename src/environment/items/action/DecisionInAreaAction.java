package environment.items.action;

import misc.action.Action;
import entity.Entity;
import entity.choice.ChoicePrototype;
import entity.choice.graphics.PopupChoice;
import entity.decision.Decision;
import environment.items.AreaPrototype;

public class DecisionInAreaAction implements Action<Entity>{

	private String name;
	public DecisionInAreaAction(String name){
		this.name = name;
	}
	@Override
	public void act(Entity entity) {
		AreaPrototype area = entity.getShip().getArea(name).getPrototype();
		Decision<ChoicePrototype> decision = new Decision<ChoicePrototype>(
				PopupChoice.createPopup(entity.filterText("NAME must chose between areas to go to."),area));
		for(int i = 0;i<area.size();++i){
			decision.add(area.getChoicePrototype(i).make());
		}
		entity.setNextDecision(decision);
	}

}
