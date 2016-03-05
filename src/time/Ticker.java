package time;

import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import gui.Gui;
import misc.Updatable;
import misc.action.Action;
import misc.wrappers.FloatWrapper;

public class Ticker extends FloatWrapper{

	private List<Object[]> onTicks = new ArrayList<Object[]>();
	private List<Updatable> updaters = new ArrayList<Updatable>();

	public Ticker() {
		super(0f);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void tick(){
		set(get()+1);
		for(int i=0;i<onTicks.size();++i){
			((Action)onTicks.get(i)[0]).act(onTicks.get(i)[1]);
		}
		while(!updaters.isEmpty()){
			updaters.get(0).update();
			updaters.remove(0);
		}
	}

	@SuppressWarnings("rawtypes")
	public void addOnTick(Action action, Entity subject) {
		onTicks.add(new Object[]{action,subject});
	}
	
	public void addOnTick(Updatable updatable) {
		updaters .add(updatable);
	}

	@SuppressWarnings("rawtypes")
	public synchronized void removeOnTock(Action action, Entity subject) {
		for(int i=0;i<onTicks.size()&&!onTicks.isEmpty();++i){
			if(onTicks.get(i)[0]==action&&onTicks.get(i)[1]==subject){
				onTicks.remove(i);
				--i;
			}
		}
	}

	public void start() {
		new Thread(){
			@Override
			public void run(){
				while(Gui.running){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					tick();
				}
			}
		}.start();
	}
}
