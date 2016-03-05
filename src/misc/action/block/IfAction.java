package misc.action.block;

import java.util.List;

import misc.Undoable;
import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.condition.Condition;
import misc.condition.logic.AndCondition;
import entity.Entity;
import entity.choice.Choice;

public class IfAction extends ActionAcceptor<Entity> implements Action<Entity> , Undoable<Entity>{

	private static final long serialVersionUID = -305949296198655484L;
	private AndCondition cond;
	private boolean lastResult = false;
	public IfAction(List<Condition> collection0){
		super();
		this.cond = new AndCondition();
		this.cond.addAll(collection0);
	}
	@Override
	public void act(Entity subject) {
		lastResult = false;
		if(cond.satisfied(subject)){
			lastResult = true;
			for(Action<Entity> action:this){
				action.act(subject);
			}
		}
	}
	@Override
	public void undo(Entity subject) {
		if(lastResult==true){
			for(Action<Entity> action:this){
				if(action instanceof Undoable){
					((Undoable<Entity>)action).undo(subject);
				}
			}
		}
	}

}
