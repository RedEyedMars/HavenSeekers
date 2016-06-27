package loom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
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
			synchronized(toAdd.processes){
				try {
					toAdd.start();
					toAdd.processes.wait();
					processors.add(toAdd);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		readyProcessor = processors.iterator();
	}
	private static int sid = 0;
	private LinkedList<Process> processes = new LinkedList<Process>();
	private int id;
	public Processor(){
		super();
		this.id = sid++;
	}
	@Override
	public void run(){
		while(Gui.running){
			try {
				synchronized(processes){
					processes.notifyAll();
				}
				synchronized(processes){
					if(processes.isEmpty()){
						processes.wait();
					}
				}
				while(!processes.isEmpty()){
					processes.removeFirst().act();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void add(Process action){
		synchronized(processes){
			processes.add(action);
			processes.notifyAll();
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
			processor.processes.clear();
		}
		for(Processor processor:processors){
			synchronized(processor.processes){
				processor.processes.notifyAll();
			}
		}
	}

	public synchronized static void joinAll(){
		for(Processor processor:processors){
			while(!processor.processes.isEmpty()){
				try {
					synchronized(processor.processes){
						//System.out.println("wait for!"+processor.id);
						processor.processes.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("checked:"+processor.id);
	}


	@Override
	public int compareTo(Processor other) {
		if(other.processes.size()==processes.size()){
			if(other==this)return 0;
			else return -1;
		}
		return processes.size()-other.processes.size();
	}
}
