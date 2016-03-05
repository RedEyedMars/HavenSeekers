package entity.trait;

import java.util.ArrayList;
import java.util.List;
import main.Hub;
import misc.condition.Condition;
import misc.condition.ForcableCondition;
import entity.Entity;
import entity.choice.graphics.IconChoice;
import entity.choice.graphics.Iconable;
import entity.trait.condition.MustHaveTraitCondition;

public class Trait implements Iconable{
	private String name;
	protected List<EntityAddOn> addOns = new ArrayList<EntityAddOn>();
	protected List<ForcableCondition<Entity>> requirements = new ArrayList<ForcableCondition<Entity>>();
	protected List<Trait> children = new ArrayList<Trait>();
	private List<Trait> parents = new ArrayList<Trait>();
	private int imageIndex;
	private String imageName;

	public Trait(String traitName_ImageName_ImageIndex){
		String[] split = traitName_ImageName_ImageIndex.split(" ");
		if(split.length==3){
			this.name = split[0];
			this.imageName = split[1];
			this.imageIndex = Integer.parseInt(split[2]);
		}
		else {
			this.name = traitName_ImageName_ImageIndex;
			this.imageName = "UNKNOWN";
			this.imageIndex = 0;
		}
		Hub.traits.put(name,this);
	}

	public String getName() {
		return name;
	}

	public boolean canAdd(Entity e){
		for(Condition<Entity> condition:requirements){
			if(!condition.satisfied(e)){
				return false;
			}
		}
		return false;
	}

	public List<Trait> getChildren(){
		return children ;
	}


	public void force(Entity entity) {
		for(ForcableCondition<Entity> condition:requirements){
			condition.force(entity);
		}
		gain(entity);
	}

	public void gain(Entity e){
		for(EntityAddOn addOn:addOns){
			addOn.addToEntity(e);
		}
		e.addTrait(this);
	}

	public void lose(Entity e){
		e.removeTrait(this);
		for(EntityAddOn addOn:addOns){
			addOn.removeFromEntity(e);
		}
	}

	public void addChild(Trait child) {
		children.add(child);
	}

	public void addRequirement(ForcableCondition<Entity> condition) {
		this.requirements.add(condition);
	}

	public void addParent(Trait parent) {
		new MustHaveTraitCondition(parent,this);
		parents.add(parent);
	}

	public void addAddOn(EntityAddOn addOn) {
		addOns.add(addOn);
	}

	public boolean ancestoryContains(String string) {
		if(name.equals(string))return true;
		for(Trait parent:parents){
			if(parent.ancestoryContains(string))return true;
		}
		return false;
	}

	public IconChoice<Trait> getIcon() {
		IconChoice<Trait> choice = new IconChoice<Trait>("traits_"+getImageName(),this);
		//choice.setFrame(getImageIndex());
		return choice;
	}

	public int getImageIndex() {
		return imageIndex;
	}

	private String getImageName() {
		return imageName;
	}



}
