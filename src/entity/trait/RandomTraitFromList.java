package entity.trait;

import java.util.List;

import entity.Entity;

public class RandomTraitFromList extends TraitsList{

	
	public RandomTraitFromList(List<Trait> collection0) {
		super(collection0);
	}

	@Override
	public void gain(Entity entity){
		children.get((int) (Math.random()*children.size())).gain(entity);
	}

	@Override
	public void lose(Entity entity){
		children.get((int) (Math.random()*children.size())).lose(entity);
	}
}
