package entity.choice;

import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import entity.choice.action.AddRoleAction;
import entity.choice.action.RemoveRoleAction;
import misc.action.Action;
import misc.condition.withable.Withable;
import misc.wrappers.FloatWrapper;
import time.action.OnStartAction;

public class Choice extends FloatWrapper implements Withable{
	private List<String> properties = new ArrayList<String>();
	private List<Action<Entity>>  actions = new ArrayList<Action<Entity>>();
	private String name;
	private String role;
	private ArrayList<OnStartAction> onStarts = new ArrayList<OnStartAction>();
	public Choice(String name, String role) {
		super(0f);
		this.properties.add(name);
		this.name = name;
		this.role = role;
		if(role!=null){
			actions.add(new AddRoleAction(role));
		}
	}
	public void addProperty(String property){
		properties.add(property);
	}
	public boolean with(String property){
		return properties.contains(property);
	}
	public void addAction(Action<Entity> action) {
		this.actions .add(action);
	}
	public void act(Entity e){
		for(Action<Entity> action:actions){
			action.act(e);
		}
		if(role!=null){
			new RemoveRoleAction(role).act(e);
		}
		e.needsNewChoice();
	}
	public void addOnStart(OnStartAction action) {
		onStarts.add(action);
	}
	public void start(Entity e){
		e.setActing(true);
		//System.out.println("start"+name);
		for(OnStartAction action:onStarts){
			action.setChoice(this);
			action.act(e);			
		}
	}
	public String getName(){
		return name;
	}
}
