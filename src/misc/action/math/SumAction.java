package misc.action.math;

import java.util.List;

import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;
import entity.Entity;

@SuppressWarnings("rawtypes")
public class SumAction extends FloatWrapperAction<Entity>{

	private List<FloatWrapperAction> list;
	public SumAction(List<FloatWrapperAction> collection0){
		super(null);
		this.list = collection0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		FloatWrapper sum = new FloatWrapper(0f);
		for(FloatWrapperAction<Entity> action:list){
			action.act(subject);
			sum.set(sum.get()+action.get().get());
		}
		set(sum);
	}

}
