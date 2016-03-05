package misc.action.block;

import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.condition.Condition;
import entity.Entity;
import entity.choice.Choice;

public class WhileAction extends ActionAcceptor<Entity> implements Action<Entity> {

	private static final long serialVersionUID = 8074363012441835513L;
	private Condition<Entity> cond;
	public WhileAction(Condition<Entity> cond){
		super();
		this.cond = cond;
	}
	@Override
	public void act(Entity subject) {
		while(cond.satisfied(subject)){
			for(Action<Entity> action:this){
				action.act(subject);
			}
		}
	}

}
