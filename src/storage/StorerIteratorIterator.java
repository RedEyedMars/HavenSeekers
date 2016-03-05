package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StorerIteratorIterator implements Iterable<Storer>{
	private List<Iterable<? extends Storable>> iterators = new ArrayList<Iterable<? extends Storable>>();
	private List<Map<Object,Storable>> maps = new ArrayList<Map<Object,Storable>>(); 
	public StorerIteratorIterator(
			Iterable<? extends Storable>[] itrs,
			Map<Object,Storable>[] maps){
		iterators.addAll(Arrays.asList(itrs));
		this.maps.addAll(Arrays.asList(maps));
	}
	@Override
	public Iterator<Storer> iterator() {
		return new Iterator<Storer>(){
			private Iterator<Iterable<? extends Storable>> itritr = iterators.iterator();
			private Iterator<Map<Object,Storable>> mapitr = maps.iterator();
			private Iterator<? extends Storable> itr = null;
			private Iterator<Object> keyitr = null;
			private Iterator<Map.Entry<Object,Storable>> entryitr = null;
			{
				if(itritr.hasNext()){
					itr = itritr.next().iterator();
				}
				if(mapitr.hasNext()){
					Map<Object,Storable> map = mapitr.next();
					entryitr = map.entrySet().iterator();
					keyitr = map.keySet().iterator();
				}
			}
			@Override
			public boolean hasNext() {
				if(itr==null)return false;
				else if(itr.hasNext()) return true;
				else {
					if(itritr.hasNext()){
						itr = itritr.next().iterator();
						return hasNext();
					}
					else {
						if(keyitr==null)return false;
						else if(keyitr.hasNext())return true;
						else if(mapitr.hasNext()){
							Map<Object,Storable> map = mapitr.next();
							entryitr = map.entrySet().iterator();
							keyitr = map.keySet().iterator();
							return hasNext();
						}
						else {
							return false;
						}
					}
				}
			}

			@Override
			public Storer next() {
				if(itr.hasNext()){
					return itr.next().getStorer();
				}
				else if(itritr.hasNext()){
					itr = itritr.next().iterator();
					return next();
				}
				else {
					if(keyitr.hasNext()){
						Storer store = entryitr.next().getValue().getStorer();
						store.setKey(keyitr.next());
						return store;
					}
					else if(mapitr.hasNext()){
						Map<Object,Storable> map = mapitr.next();
						entryitr = map.entrySet().iterator();
						keyitr = map.keySet().iterator();
						return next();
					}
					else {
						return null;
					}
				}
			}
			
		};
	}

}
