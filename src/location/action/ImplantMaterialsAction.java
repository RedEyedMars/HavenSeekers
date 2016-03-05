package location.action;

import location.Location;
import location.materials.Resource;
import location.materials.ResourcePrototype;
import misc.Range;
import misc.action.Action;
import misc.action.WrapperAction;
import misc.condition.access.Propertiable;
import misc.wrappers.ObjectWrapper;

public class ImplantMaterialsAction implements Action<Location>{

	private WrapperAction getter;
	private Range range;
	private ObjectWrapper<Resource> wrapper;

	public ImplantMaterialsAction(WrapperAction getter, Range range){
		this.getter = getter;
		this.range = range;
		this.wrapper = new ObjectWrapper<Resource>(null);
	}

	@Override
	public void act(Location subject) {
		Float precent = range.get()*subject.getMass();
		for(int i=0;i<precent;i+=10){
			getter.set(wrapper);
			getter.act(subject);
			Resource r = wrapper.get();
			subject.addResource(r,10);
		}

	}

}
