package environment.items.action;

import entity.Entity;
import location.materials.ResourcePrototype;
import misc.action.Action;
import misc.action.WrapperAction;
import misc.action.access.FloatWrapperAction;
import misc.condition.access.Propertiable;
import misc.wrappers.FloatWrapper;
import misc.wrappers.ObjectWrapper;

public class TransferResourceAction implements Action<Entity>{
	private FloatWrapperAction by;
	private WrapperAction<Entity> to;
	private WrapperAction<Entity> from;
	private FloatWrapperAction id;
	private ObjectWrapper<Propertiable> from_retriever;
	private ObjectWrapper<Propertiable> to_retriever;

	public TransferResourceAction(FloatWrapperAction id, FloatWrapperAction by, WrapperAction<Entity> from, String syntax, WrapperAction<Entity> to){
		this.by = by;
		this.from = from;
		this.to = to;
		this.id = id;
		this.to_retriever = new ObjectWrapper<Propertiable>(null);
		this.from_retriever = new ObjectWrapper<Propertiable>(null);
	}

	@Override
	public void act(Entity subject) {
		from.set(from_retriever);
		from.act(subject);
		to.set(to_retriever);
		to.act(subject);
		by.act(subject);
		id.act(subject);
		String resourceType = ResourcePrototype.translateResourceId(id.getFloat());
		FloatWrapper from_value = from_retriever.get().getProperty(resourceType);
		if(!to_retriever.get().addProperty(resourceType, by.getFloat())){
			FloatWrapper to_value = to_retriever.get().getProperty(resourceType);
			to_value.set(to_value.get()+by.getFloat());
		}
		from_value.set(from_value.get()-by.getFloat());
	}
}
