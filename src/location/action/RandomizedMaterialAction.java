package location.action;

import java.util.List;

import location.Location;
import location.materials.ResourcePrototype;
import main.Hub;
import main.Log;
import misc.action.WrapperAction;
import misc.wrappers.ObjectWrapper;
import entity.Entity;

public class RandomizedMaterialAction extends WrapperAction<Location>{
	private ResourcePrototype rp; 
	public RandomizedMaterialAction(List<String> materials) {
		super(null);
		rp = Hub.resourceTypes.get(materials.get(0));
		Log.d("location","randomizedMaterialAction.construct",rp==null?"null":rp.toString()+"="+materials.get(0));
	}

	@Override
	public void act(Location subject) {
		//TODO full implementation includes selection of subtypes to be included (right now the prototype can return any of it's possible subtypes)

		get().set(rp.create());
	}

}
