package entity.trait;

import java.util.List;

import entity.Entity;

public class TraitsList extends Trait{

	public TraitsList(List<Trait> collection0) {
		super("$list");
		for(Trait trait:collection0){
			children.add(trait);
		}
	}
	
	@Override
	public void gain(Entity entity){
		for(Trait trait:children){
			trait.gain(entity);
		}
	}

	@Override
	public void lose(Entity entity){
		for(Trait trait:children){
			trait.lose(entity);
		}
	}
}
