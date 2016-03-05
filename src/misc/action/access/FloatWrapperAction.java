package misc.action.access;

import misc.action.Action;
import misc.wrappers.FloatWrapper;
import misc.wrappers.ObjectWrapper;

public abstract class FloatWrapperAction<Subject> extends ObjectWrapper<FloatWrapper> implements Action<Subject>{

	public FloatWrapperAction(FloatWrapper o) {
		super(o);
	}

	public int getInt() {
		return (int)(float)get().get();
	}
	
	public Float getFloat(){
		return get().get();
	}

}
