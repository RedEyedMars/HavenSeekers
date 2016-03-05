package storage;

import java.util.Iterator;

import misc.condition.Condition;
import parser.StringHeirachy;

public abstract class Storer <StoreType extends Storable>{
	private int id = -1;
	protected Object key;
	protected StoreType self;
	private Iterable<Storer> iterator;
	public Storer(StoreType self){
		this.self = self;
		this.iterator = self.getStorerIterator();
	}
	protected abstract String storeSelf();
	public void store(StringHeirachy database){
		if(id==-1){
			StringHeirachy section = database.append(storeKey()+storeSelf()+"\n");
			for(Storer storer:iterator){
				storer.store(section);
			}
		}
		else {
			StringHeirachy section = database.get(id);
			section.setValue(storeSelf());
			for(Storer storer:iterator){
				storer.store(section);
			}
		}
	}
	private String storeKey(){
		return (key!=null?(key.toString()+"->>"):"");
	}

	protected abstract Iterable<Condition<String>> loadRules();
	protected abstract void loadSelf(StringHeirachy input);
	protected boolean load(StringHeirachy database){
		Iterable<Condition<String>> rules = loadRules();
		int i=0;
		for(StringHeirachy section:database){
			boolean acceptable = true;
			String value = section.getValue()+section.getId();
			for(Condition<String> rule:rules){
				if(!rule.satisfied(value)){
					acceptable = false;
					break;
				}
			}
			if(acceptable){
				loadSelf(section);
				id = i;
				return true;
			}
			++i;
		}
		return false;
	}
	public void setKey(Object key) {
		this.key = key;
	}
}
