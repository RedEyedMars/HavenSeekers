package misc.wrappers;

import java.util.Iterator;

import misc.condition.Condition;
import parser.StringHeirachy;
import storage.Storable;
import storage.Storer;

public class StorableFloatWrapper extends FloatWrapper implements Storable{

	private FloatWrapper wrapper;
	private FloatStorer storer;
	public StorableFloatWrapper(FloatWrapper f){
		super(f.get());
		wrapper = f;
		storer = new FloatStorer(this);
	}

	@Override
	public Float get(){
		return wrapper.get();
	}
	public void set(Float o){
		wrapper.set(o);
	}

	@Override
	public Storer getStorer() {
		return null;
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

	private class FloatStorer extends Storer {
		public FloatStorer(Storable self) {
			super(self);
		}

		@Override
		protected String storeSelf() {
			return ""+wrapper.get();
		}

		@Override
		protected Iterable<Condition<String>> loadRules() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void loadSelf(StringHeirachy input) {
			// TODO Auto-generated method stub

		}

	}

}
