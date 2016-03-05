package entity.trait.action;

import misc.action.Action;
import entity.Entity;
import entity.trait.Trait;

public class LoseTraitAction implements Action<Entity>{

	private Trait trait;
	public LoseTraitAction(Trait trait){
		this.trait = trait;
	}
	@Override
	public void act(Entity subject) {
		trait.lose(subject);
	}

}
