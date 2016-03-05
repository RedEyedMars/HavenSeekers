package entity.choice;

import entity.trait.Trait;
import entity.trait.action.GainTraitAction;

public class TraitChoice extends Choice {

	//private Trait trait;

	public TraitChoice(Trait trait) {
		super(trait.getName()+"Choice",null);
		//this.trait = trait;
		addAction(new GainTraitAction(trait));
	}

}
