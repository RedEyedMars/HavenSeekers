package storage;

public interface Storable {
	public Storer getStorer();
	public Iterable<Storer>  getStorerIterator();
}
