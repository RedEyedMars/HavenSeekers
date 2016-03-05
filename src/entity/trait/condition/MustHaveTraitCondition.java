package entity.trait.condition;

import java.util.List;

import entity.Entity;
import entity.trait.Trait;
import entity.trait.TraitsList;
import misc.condition.ForcableCondition;

public class MustHaveTraitCondition implements ForcableCondition<Entity>{
	private Trait trait;
	public MustHaveTraitCondition(Trait trait, Trait child){
		this(trait);
		//this is used to enforce enheritance
		trait.addChild(child);
		child.addRequirement(this);
	}
	
	public MustHaveTraitCondition(Trait trait){
		//this is generic
		this.trait = trait;
	}

	public MustHaveTraitCondition(List<Trait> collection0) {
		this.trait = new TraitsList(collection0);
	}

	@Override
	public boolean satisfied(Entity o) {
		if(trait.getName().equals("$list")){
			for(Trait t:trait.getChildren()){
				if(o.hasTrait(t)){
					return true;
				}
			}
			return false;
		}
		else {
			return o.hasTrait(trait);
		}
	}

	@Override
	public void force(Entity o) {
		o.forceTrait(trait);
	}
	
}
