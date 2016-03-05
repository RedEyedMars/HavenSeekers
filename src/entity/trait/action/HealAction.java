package entity.trait.action;

import main.Hub;
import misc.action.Action;
import entity.Entity;
import entity.trait.Trait;

public class HealAction implements Action<Entity>{

	public HealAction(){
	}
	@Override
	public void act(Entity subject) {
		if(subject.hasTrait(Hub.traits.get("Wounded"))){
			heal(Hub.traits.get("Wounded"),subject);
		}
	}

	private void heal(Trait trait, Entity subject){
		for(Trait sub:trait.getChildren()){
			if(subject.hasTrait(sub)&&!sub.getName().endsWith("Dead")){
				heal(sub,subject);
				return;
			}
		}
		if(trait.getName().endsWith("Wound")||"Wounded".equals(trait.getName())){
			trait.lose(subject);
		}
	}
}
