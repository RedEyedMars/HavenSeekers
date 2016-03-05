package loom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import gui.Gui;
import misc.action.Action;

public class Processor extends Thread implements Comparable<Processor>{
	private static List<Processor> processors = new ArrayList<Processor>();
	private static Iterator<Processor> readyProcessor;
	private static int coreSize = 0;
	static {
		coreSize = Runtime.getRuntime().availableProcessors();
		for(int i=0;i<coreSize;++i){
			Processor toAdd = new Processor();
			toAdd.start();
			processors.add(toAdd);
		}
		readyProcessor = processors.iterator();
	}
	private static int sid = 0;
	private List<Process> procList = new ArrayList<Process>();
	private int id;
	public Processor(){
		super();
		this.id = sid++;
	}
	@Override
	public void run(){
		while(Gui.running){
			while(!procList.isEmpty()){
					procList.remove(0).act();
			}
			System.out.println("release"+ id);
			try {
				synchronized(procList){
					procList.notifyAll();
					procList.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void add(Process action){
		System.out.println("process added to "+id);
		synchronized(procList){
			procList.add(action);
			if(procList.size()==1){
				procList.notifyAll();
			}
		}
	}

	public synchronized static void addProcess(Process action){
		readyProcessor.next().add(action);
		if(!readyProcessor.hasNext()){
			readyProcessor = processors.iterator();
		}
	}

	public static void close(){
		for(Processor processor:processors){
			synchronized(processor.procList){
				processor.procList.clear();
				processor.procList.notifyAll();
			}
		}
	}

	public synchronized static void joinAll(){
		for(Processor processor:processors){
			synchronized(processor.procList){
				while(!processor.procList.isEmpty()){
					try {
						System.out.println("wait for!"+processor.id);
						processor.procList.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public int compareTo(Processor other) {
		if(other.procList.size()==procList.size()){
			if(other==this)return 0;
			else return -1;
		}
		return procList.size()-other.procList.size();
	}
}
