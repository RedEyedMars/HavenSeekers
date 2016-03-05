package misc.action;

import misc.wrappers.ObjectWrapper;

@SuppressWarnings("rawtypes")
public abstract class WrapperAction<Subject> extends ObjectWrapper<ObjectWrapper> implements Action<Subject>{

	public WrapperAction(ObjectWrapper o) {
		super(o);
	}

}
