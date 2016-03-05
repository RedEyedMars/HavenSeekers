package loom;

import misc.action.Action;

public abstract class Process implements Action{
	private Object subject;
	public Process(Object subject){
		this.subject = subject;
		Processor.addProcess(this);//NOTE: this is done before all the construct has set it's variables, if there is weirdness with threads, it might be from here
	}
	public void act(){
		act(subject);
	}
}
