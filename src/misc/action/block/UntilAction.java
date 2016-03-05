package misc.action.block;

import main.Hub;
import misc.Undoable;
import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.action.access.FloatWrapperAction;
import misc.condition.Condition;
import entity.Entity;
import entity.choice.Choice;

public class UntilAction extends ActionAcceptor<Entity> implements Action<Entity> {

	private static final long serialVersionUID = 8074363012441835513L;
	private FloatWrapperAction<Entity> n2;
	private FloatWrapperAction<Entity> n1;
	private boolean first = true;
	public UntilAction(FloatWrapperAction<Entity> number1, FloatWrapperAction<Entity> number2){
		super();
		this.n1 = number1;
		this.n2 = number2;
	}
	@Override
	public void act(Entity subject) {
		if(first){
			for(Action<Entity> action:this){
				action.act(subject);
			}
		}
		n1.act(subject);
		n2.act(subject);
		if(first&&n1.get().get()!=n2.get().get()){
			Hub.ticker.addOnTick(this,subject);
			first = false;
		}
		else {
			Hub.ticker.removeOnTock(this,subject);
			for(Action<Entity> action:this){
				if(action instanceof Undoable){
					((Undoable<Entity>)action).undo(subject);
				}
			}
		}
	}

}
