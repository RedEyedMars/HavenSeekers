package misc.wrappers;

public class ObjectWrapper <ReturnType> {
	protected ReturnType o;
	public ObjectWrapper(ReturnType o){
		set(o);
	}
	public ReturnType get(){
		return o;
	}
	public void set(ReturnType o){
		this.o = o;
	}
}
