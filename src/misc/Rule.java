package misc;

import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import entity.trait.EntityAddOn;

import misc.action.Action;
import misc.condition.Condition;


public class Rule <Modifier extends Object,Subject extends Object>{
	private Action<Modifier> action;
	private List<Condition<Subject>> conditions = new ArrayList<Condition<Subject>>();
	@SuppressWarnings("unchecked")
	public Rule(Action<Modifier> action, Condition<Subject>... conds){
		this.action = action;
		for(Condition<Subject> condition:conds){
			this.conditions.add(condition);
		}
	}

	public void rate(Subject object, Modifier i){
		if(conditions.size()==1){
			if(conditions.get(0).satisfied(object)){
				action.act(i);
			}
		}
		else if(conditions.size()==0){
			action.act(i);
		}
		else {
			boolean doom = true;
			for(Condition<Subject> condition:conditions){
				if(!condition.satisfied(object)){
					doom = false;
					break;
				}
			}
			if(doom){
				action.act(i);
			}
		}
	}

	public EntityAddOn getAddOn(final String on) {
		final Rule<Modifier,Subject> self = this;
		return new EntityAddOn(){
			@Override
			public void addToEntity(Entity e) {
				e.addRule(on,self);
			}

			@Override
			public void removeFromEntity(Entity e) {
				e.removeRule(on,self);				
			}
			
		};
	}
}
