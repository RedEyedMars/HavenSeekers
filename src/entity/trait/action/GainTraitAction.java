package entity.trait.action;

import misc.action.Action;
import entity.Entity;
import entity.trait.Trait;

public class GainTraitAction implements Action<Entity>{

	private Trait trait;
	public GainTraitAction(Trait trait){
		this.trait = trait;
	}
	@Override
	public void act(Entity subject) {
		trait.gain(subject);
	}

}
