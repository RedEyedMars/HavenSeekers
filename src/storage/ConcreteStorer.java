package storage;

public class ConcreteStorer <T extends Storable> extends Storer<T> {
	public ConcreteStorer(T self, String id) {
		super(self, id);
	}

	@Override
	protected Object[] storeCharacteristics() {
		return o();
	}

	@Override
	protected void adjust(Object... args) {		
	}

}
