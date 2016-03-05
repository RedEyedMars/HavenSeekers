package misc.action.math;

import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;

public class NegativeNumberAction<Subject> extends FloatWrapperAction<Subject>{

	private FloatWrapperAction<Subject> initial;
	public NegativeNumberAction(FloatWrapperAction<Subject> initial){
		super(null);
		this.initial = initial;
	}
	@Override
	public void act(Subject subject) {
		initial.act(subject);
		FloatWrapper i = initial.get();
		set(new NegativeIntegerWrapper(i));
	}

	private class NegativeIntegerWrapper extends FloatWrapper{
		private FloatWrapper i;
		public NegativeIntegerWrapper(FloatWrapper i){
			super(null);
			this.i = i;
		}

		@Override
		public Float get(){
			return -i.get();
		}

		@Override
		public void set(Float i){
			if(i!=null){
				System.err.println("NegativeIntegerWrapper within NegativeIntegerWrapper, set is being called, this should not happen!");
				this.i.set(i);
			}
		}
	}

}
