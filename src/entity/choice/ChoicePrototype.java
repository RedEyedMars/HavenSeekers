package entity.choice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.Entity;
import entity.choice.graphics.IconChoice;
import entity.choice.graphics.IconTextChoice;
import entity.choice.graphics.Iconable;
import entity.choice.storage.ChoicePrototypeStorer;
import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.condition.Condition;
import storage.Storable;
import storage.Storer;
import storage.StorerIteratorIterator;
import time.action.OnStartAction;

public class ChoicePrototype extends ActionAcceptor<Entity> implements Iconable, Storable{

	private static final long serialVersionUID = 8010863452839085820L;
	private String name;
	private String role;
	private List<String> properties = new ArrayList<String>();
	private List<Condition<Entity>> conditions = new ArrayList<Condition<Entity>>();
	private List<Action<Entity>> onStarts = new ArrayList<Action<Entity>>();

	private ChoicePrototypeStorer storer = new ChoicePrototypeStorer(this);
	public ChoicePrototype(String name) {
		int index = name.indexOf('(');
		if(index>=0){
			this.name = name.substring(0,index);
			properties.add(name.substring(0,index));
			this.role = name.substring(index+1,name.indexOf(')'));
		}
		else {
			this.name = name;
			properties.add(name);
			role = null;
		}
	}

	public void addProperty(String value) {
		properties.add(value);
	}

	public void addCondition(Condition<Entity> cond) {
		conditions.add(cond);
	}
	

	public boolean can(Entity e){
		for(Condition<Entity> condition:conditions){
			if(condition.satisfied(e)==false){
				return false;
			}
		}
		return true;
	}
	
	public Choice make(){
		Choice choice = new Choice(name,role);
		for(String property:properties){
			choice.addProperty(property);
		}
		for(Action<Entity> action:this){
			choice.addAction(action);
		}
		for(Action<Entity> action:onStarts){
			choice.addOnStart((OnStartAction) action);
		}
		return choice;
	}

	public void addOnStart(Action<Entity> a) {
		this.onStarts .add(a);
	}

	public IconChoice<ChoicePrototype> getIcon() {
		return new IconTextChoice<ChoicePrototype>(name,this);
	}
	
	public ChoicePrototype clone(){
		ChoicePrototype molly = new ChoicePrototype(name);
		molly.role = role;
		for(String property:properties){
			molly.properties.add(property);
		}
		for(Condition<Entity> condition:conditions){
			molly.conditions.add(condition);
		}
		for(Action<Entity> onStart:onStarts){
			molly.onStarts.add(onStart);
		}
		return molly;
	}

	@Override
	public Storer getStorer() {
		return storer;
	}

	@Override
	public Iterable<Storer> getStorerIterator() {
		return new StorerIteratorIterator(
				new List[]{this,properties,conditions,onStarts},
				new Map[]{});
		
	}

}
