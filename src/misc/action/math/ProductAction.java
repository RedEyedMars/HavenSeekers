package misc.action.math;

import java.util.List;

import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;
import entity.Entity;

@SuppressWarnings("rawtypes")
public class ProductAction extends FloatWrapperAction<Entity>{

	private List<FloatWrapperAction> list;
	public ProductAction(List<FloatWrapperAction> collection){
		super(null);
		this.list = collection;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		FloatWrapper product = new FloatWrapper(1f);
		for(FloatWrapperAction<Entity> action:list){
			action.act(subject);
			product.set(product.get()*action.get().get());

		}
		set(product);
	}

}
