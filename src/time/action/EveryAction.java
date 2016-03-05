package time.action;

import entity.Entity;
import main.Hub;
import misc.Undoable;
import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.action.access.FloatWrapperAction;

public class EveryAction extends ActionAcceptor<Entity> implements Action<Entity>, Undoable<Entity>{
	private static final long serialVersionUID = -4433302790535901390L;

	@SuppressWarnings("unused")
	private String song = "EVERY MOVE YOU MAKE, EVERY BREATH YOU TAKE, I'LL BE WATCHING YOU!";

	private FloatWrapperAction<Entity> number;

	private boolean first;

	private int time;
	public EveryAction(FloatWrapperAction<Entity> number, String ticks){
		//ticks is just so i can write "ticks" after "every 10 >ticks<"
		this.number = number;
		this.first = true;
		this.time = 0;
	}
	
	@Override
	public void act(Entity subject) {
		if(first){
			Hub.ticker.addOnTick(this, subject);		
		}
		else if(time==0){
			for(Action<Entity> action:this){
				action.act(subject);
			}
		}
		++time;
		if(time==number.getInt()){
			time = 0;
		}
	}

	@Override
	public void undo(Entity subject) {
		Hub.ticker.removeOnTock(this, subject);
	}
	
	
}
