package time.action;

import main.Hub;
import misc.Undoable;
import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.action.access.FloatWrapperAction;
import misc.action.access.SetAction;
import entity.Entity;
import entity.choice.Choice;
import entity.state.State;

public class OnStartAction extends ActionAcceptor<Entity> implements Action<Entity>{
	private static final long serialVersionUID = 8121167381281307554L;
	private String name;
	private String desc;
	private Choice choice;
	protected FloatWrapperAction<Entity> start = null;
	private FloatWrapperAction<Entity> end;
	protected boolean first = true;
	private Integer firstStart;
	private Integer firstEnd;

	public OnStartAction(String actionName, String description){
		this.name = actionName;
		this.desc = description;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		if(first){
			new SetAction("tick",new NowAction<Entity>()).act(subject);
			if(start==null){
				start = new SinceAction("tick");
			}
			start.act(subject);
			end = (FloatWrapperAction<Entity>) get(0);
			for(int i = 0;i<this.size();++i){
				get(i).act(subject);
			}
			this.firstStart = (int)(float)start.get().get();
			this.firstEnd = (int)(float)end.get().get();
			Hub.ticker.addOnTick(this,subject);
			first = false;

		}
		start.act(subject);
		end.act(subject);
		subject.setCurrentState(State.performingAction,name,desc,firstStart,(int)(float)(start.get().get()),(int)(float)end.get().get(),firstEnd);
		//System.out.println(start.get().get()+"=="+end.get().get());
		if(start.get().get()>=end.get().get()) {
			Hub.ticker.removeOnTock(this,subject);
			choice.act(subject);
			for(int i = 0;i<this.size();++i){
				if(get(i) instanceof Undoable){
					((Undoable<Entity>)get(i)).undo(subject);
				}
			}
		}
	}

	public void setChoice(Choice choice) {
		this.choice = choice;
	}
}
