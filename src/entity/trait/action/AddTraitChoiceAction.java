package entity.trait.action;

import java.util.List;

import misc.action.Action;

import entity.Entity;
import entity.decision.TraitDecision;
import entity.trait.Trait;

public class AddTraitChoiceAction implements Action<Entity>{

	private List<Trait> traits;
	public AddTraitChoiceAction(List<Trait> traits){
		this.traits = traits;
	}
	@Override
	public void act(Entity subject) {
		TraitDecision decision = new TraitDecision(
				subject.filterText("NAME must chose between these traits."),traits);
		subject.setNextDecision(decision);
	}
	
}
