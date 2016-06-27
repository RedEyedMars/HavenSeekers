package storage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import entity.choice.ChoicePrototype;
import parser.StringHeirachy;

public abstract class StorerageIterator implements Iterable<Storer>{
	private List<SingleStorer> singles = new ArrayList<SingleStorer>();
	private List<ListStorer> lists = new ArrayList<ListStorer>();
	private List<MapStorer> maps = new ArrayList<MapStorer>(); 
	public StorerageIterator(
			Storable target,String... colNames){
		try {
			for(int i=0;i<colNames.length;++i){
				Class<?> targetClass = target.getClass();
				Field field = null;
				do {
					try {
						field = targetClass.getDeclaredField(colNames[i]);
						break;
					}
					catch(NoSuchFieldException nsfe){
						targetClass = targetClass.getSuperclass();
					}
				} while(targetClass!=Object.class);
				ParameterizedType type = null;
				boolean single = false;
				if(field.getGenericType() instanceof Class<?>){
					single = true;
				}
				else {
					type = (ParameterizedType)field.getGenericType();
				}
				if(single){
					//Single
					this.singles.add(
							new SingleStorer(
									(Storable) getField(field,target),
									(Constructor<? extends Storable>) field.getType().getConstructor(new Class[]{}),
									field));
				}
				else if(type.getActualTypeArguments().length==1){
					//List
					this.lists.add(
							new ListStorer(
									(List<Storable>) getField(field,target),
									((Class<? extends Storable>)type.getActualTypeArguments()[0]).getConstructor(new Class[]{}),
									i));
				}
				else {
					//Map
					this.maps.add(
							new MapStorer(
									(Map<Object,Storable>) getField(field,target),
									((Class<? extends Object>)type.getActualTypeArguments()[0]),
									((Class<? extends Storable>)type.getActualTypeArguments()[1]).getConstructor(new Class[]{}),
									i));
				}
			}

		} catch ( SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	protected abstract Object getField(Field field, Object target) throws IllegalArgumentException, IllegalAccessException;

	@Override
	public Iterator<Storer> iterator() {
		return new Iterator<Storer>(){
			private Iterator<SingleStorer> singleItr = singles.iterator();
			private Iterator<ListStorer> listItr = lists.iterator();
			private Iterator<MapStorer> mapItr = maps.iterator();
			@Override
			public boolean hasNext() {
				return singleItr.hasNext()||listItr.hasNext()||mapItr.hasNext();
			}

			@Override
			public Storer next() {
				if(singleItr.hasNext()){
					return singleItr.next();
				}
				else if(listItr.hasNext()){
					return listItr.next();
				}
				else if(mapItr.hasNext()){
					return mapItr.next();
				}
				else {
					return null;
				}
			}

		};
	}

	private class SingleStorer extends Storer implements Storable, Iterable<Storer> {
		private Storable single;
		private Constructor<? extends Storable> creator;
		private Field field;
		public SingleStorer(Storable self,Constructor<? extends Storable> creator, Field field) {
			super(null, "SINGLE");
			this.single = self;
			this.self = this;
			this.creator = creator;
			this.field = field;
		}

		@Override
		protected Object[] storeCharacteristics() {
			return null;
		}

		@Override
		public void store(List build, Map strings){
			if(single!=null){
				single.getStorer().store(build, strings);
			}
			else {
				new RuntimeException("ERROR:"+field.getName()+" CANNOT BE NULL");
			}
		}

		@Override
		public Storer getStorer() {
			return this;
		}

		@Override
		public Iterable<Storer> getStorerIterator() {
			return this;
		}

		@Override
		protected void adjust(Object... args) {
		}
		@Override
		public int load(Byte[] cs, int index, Map strings){
			if(single!=null){
				return single.getStorer().load(cs,index,strings);
			}
			return -1;
		}
		@Override
		public Iterator<Storer> iterator() {
			return new Iterator<Storer>(){
				private boolean hasSent = false;
				@Override
				public boolean hasNext() {
					return single!=null&&!hasSent;
				}

				@Override
				public Storer next() {
					hasSent = true;
					return single.getStorer();					
				}

			};
		}
	}

	private class ListStorer extends Storer implements Storable, Iterable<Storer>{
		private List<Storable> list;
		private Constructor<? extends Storable> creator;
		private int id;
		public ListStorer(List<Storable> self,Constructor<? extends Storable> creator, int id) {
			super(null, ""+id);
			this.id = id;
			this.list = self;
			this.self = this;
			this.creator = creator;
		}

		@Override
		protected Object[] storeCharacteristics() {
			return null;
		}


		@Override
		protected void encode(Map strings, List build){
			encodeInteger(id,build);
			encodeInteger(list.size(),build);
		}

		@Override
		public void store(List build, Map strings){
			if(!list.isEmpty()){
				super.store(build,strings);
			}
		}

		@Override
		public Storer getStorer() {
			return this;
		}

		@Override
		public Iterable<Storer> getStorerIterator() {
			return this;
		}

		@Override
		protected void adjust(Object... args) {
		}
		@Override
		public int load(Byte[] cs, int index,Map strings){
			Integer[] result = decodeInteger(cs,index);
			if(decodeInteger(cs,index)[0]!=id){
				return index;
			}
			index=result[1];
			result = decodeInteger(cs,index);
			int size = result[0];
			index = result[1];
			while(list.size()>size){
				list.remove(list.size()-1);
			}
			while(list.size()<size){
				try {
					Storable target = this.creator.newInstance(new Object[]{});
					index = target.getStorer().load(cs,index,strings);
					list.add(target);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return index;
		}
		@Override
		public Iterator<Storer> iterator() {
			return new Iterator<Storer>(){
				private Iterator<? extends Storable> itr = list.iterator();
				@Override
				public boolean hasNext() {
					return itr.hasNext();
				}

				@Override
				public Storer next() {
					return itr.next().getStorer();					
				}

			};
		}

	}

	private class MapStorer extends Storer implements Storable, Iterable<Storer>{
		private Map<Object,Storable> map;
		private Constructor<? extends Storable> creator;
		private Method keyMaker;
		private List<Object> keys;
		private int id;
		public MapStorer(Map<Object,Storable> self,
				Class<? extends Object> keyClass,
				Constructor<? extends Storable> creator,
				int id) {
			super(null, ""+id);
			this.id = id;
			this.map = self;
			this.self = this;
			this.creator = creator;
			try {
				this.keyMaker = this.getClass().getMethod("parse"+keyClass.getSimpleName()+"AsKeyType", new Class[]{String.class});
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			keys = new ArrayList<Object>(map.size());
			for(Object key:map.keySet()){
				keys.add(key);
			}
		}

		@Override
		protected Object[] storeCharacteristics() {
			this.strings = map.size();
			return map.keySet().toArray();
		}

		@Override
		public void store(List build, Map strings){
			if(!map.isEmpty()){
				keys.clear();
				for(Object key:map.keySet()){
					keys.add(key);
				}
				super.store(build,strings);
			}
		}
		@Override
		protected void encode(Map strings, List build){
			encodeInteger(id,build);
			encodeInteger(map.size(),build);
			super.encode(strings,build);
		}

		@Override
		public Storer getStorer() {
			return this;
		}

		@Override
		public Iterable<Storer> getStorerIterator() {
			return this;
		}

		@Override
		protected void adjust(Object... args) {
			String[] keyStrings = new String[args.length];
			for(int i=0;i<args.length;++i){
				keyStrings[i] = (String)args[i];
			}
			keys = new ArrayList<Object>(keyStrings.length);
			try {
				for(int i=0;i<keyStrings.length;++i){
					keys.add(keyMaker.invoke(this, keyStrings[i]));
				}
				List<Object> noLongerKeys = new ArrayList<Object>();
				for(Object key:map.keySet()){
					if(!keys.contains(key)){
						noLongerKeys.add(key);
					}
				}
				for(Object toRemove:noLongerKeys){
					map.remove(toRemove);
				}
				for(Object key:keys){
					if(!map.containsKey(key)){						
						Storable target = this.creator.newInstance(new Object[]{});
						map.put(key,target);
					}					
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		@Override
		public int load(Byte[] cs, int index, Map strings){
			Integer[] result = decodeInteger(cs,index);
			if(result[0]!=id){
				return index;
			}
			index=result[1];
			result = decodeInteger(cs,index);
			this.strings = result[0];
			index = result[1];
			return super.load(cs, index, strings);
		}

		@Override
		public Iterator<Storer> iterator() {
			return new Iterator<Storer>(){
				private Iterator<Object> itr = keys.iterator();
				@Override
				public boolean hasNext() {
					return itr.hasNext();
				}

				@Override
				public Storer next() {
					return map.get(itr.next()).getStorer();
				}

			};
		}

		public Object parseIntegerAsKeyType(String key){
			return Integer.parseInt(key);
		}

		public Object parseStringAsKeyType(String key){
			return key;
		}

		public Object parseFloatAsKeyType(String key){
			return Float.parseFloat(key);
		}
	}
}
