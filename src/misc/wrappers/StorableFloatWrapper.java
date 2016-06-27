package misc.wrappers;

import java.util.Iterator;

import entity.choice.ChoicePrototype;
import misc.condition.Condition;
import parser.StringHeirachy;
import storage.Storable;
import storage.Storer;

public class StorableFloatWrapper extends FloatWrapper implements Storable{

	private FloatWrapper wrapper;
	private FloatStorer storer;
	public StorableFloatWrapper(FloatWrapper f){
		super(0f);
		wrapper = f;
		storer = new FloatStorer(this);
	}
	public StorableFloatWrapper(){
		this(null);
	}

	@Override
	public Float get(){
		return wrapper.get();
	}
	public void set(Float o){
		if(wrapper!=null){
			wrapper.set(o);
		}
	}

	@Override
	public Storer getStorer() {
		return storer;
	}

	@Override
	public Iterable<Storer> getStorerIterator() {
		return new Iterable<Storer>(){
			@Override
			public Iterator<Storer> iterator() {
				return new Iterator<Storer>(){
					@Override
					public boolean hasNext() {
						return false;
					}

					@Override
					public Storer next() {
						return null;
					}};
			}
		

	};}

	private class FloatStorer extends Storer<StorableFloatWrapper> {

		public FloatStorer(StorableFloatWrapper self) {
			super(self, "F");
			floats = 1;
		}
		@Override
		protected Object[] storeCharacteristics() {
			return o(wrapper.get());
		}
		@Override
		protected void adjust(Object... args) {
			if(wrapper==null){
				wrapper = new FloatWrapper(0f);
			}
			wrapper.set((Float) args[0]);
		}

	}

}
