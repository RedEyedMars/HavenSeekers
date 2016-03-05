package entity.decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import misc.action.Action;
import entity.Entity;
import entity.choice.Choice;
import entity.choice.graphics.PopupChoice;

public class Decision<T> extends ArrayList<Choice>{
	private static final long serialVersionUID = -8241073947447660682L;
	public final static int EASY = 50;
	public final static int MEDIUM = 100;
	public final static int HARD = 200;

	private PopupChoice<T> display;
	private int difficulty = 100;
	private int progress = 0;
	private Map<Choice,List<Action<Entity>>> actions = new HashMap<Choice,List<Action<Entity>>>();
	public Decision(PopupChoice<T> onDisplay){
		super();
		this.display = onDisplay;
	}

	public boolean step(Entity e){
		++progress;
		if(progress>=difficulty){
			decide(e);
			return true;
		}
		return false;
	}

	private void decide(Entity e) {
		int max = -10000;
		Choice best = get(0);
		if(display.getSelected()==null){
			for(Choice choice:this){
				int value = e.getPreferenceFor(choice);
				if(value>max){
					max = value;
					best = choice;
				}
			}
		}
		else {
			best = get(display.getIndex());
		}
		if(actions.containsKey(best)){
			for(Action<Entity> action:actions.get(best)){
				action.act(e);
			}
		}
		best.start(e);
	}

	public void onDecide(Choice choice, Action<Entity> action) {
		if(!actions.containsKey(choice)){
			actions.put(choice,new ArrayList<Action<Entity>>());
		}
		actions.get(choice).add(action);
	}

	public void setDifficulty(int d){
		this.difficulty = d;
	}
}
