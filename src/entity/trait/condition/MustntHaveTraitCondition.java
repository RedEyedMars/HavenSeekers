package entity.trait.condition;

import java.util.List;

import entity.Entity;
import entity.trait.Trait;
import entity.trait.TraitsList;
import misc.condition.ForcableCondition;

public class MustntHaveTraitCondition implements ForcableCondition<Entity>{
	private Trait trait;
	public MustntHaveTraitCondition(Trait trait, Trait child){
		this(trait);
		//this is used to enforce enheritance
		trait.addChild(child);
		child.addRequirement(this);
	}
	
	public MustntHaveTraitCondition(Trait trait){
		//this is generic
		this.trait = trait;
	}

	public MustntHaveTraitCondition(List<Trait> collection0) {
		this.trait = new TraitsList(collection0);
	}

	@Override
	public boolean satisfied(Entity o) {
		return !o.hasTrait(trait);
	}

	@Override
	public void force(Entity o) {
		trait.lose(o);
	}
	
}
