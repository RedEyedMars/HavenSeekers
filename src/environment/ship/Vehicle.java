package environment.ship;

import java.util.HashMap;
import java.util.Map;

import misc.condition.access.Propertiable;
import misc.wrappers.FloatWrapper;

public class Vehicle implements Propertiable{

	private Map<String,FloatWrapper> values = new HashMap<String,FloatWrapper>();
	public Vehicle(){
		values.put("efficiency", new FloatWrapper(1f));
	}
	public FloatWrapper getProperty(String property) {
		return values.containsKey(property)?values.get(property):new FloatWrapper(0f);
	}

	@Override
	public boolean addProperty(String property, Float value) {
		if(values.containsKey(property)){
			return false;
		}
		values.put(property, new FloatWrapper(value));
		return true;
	}
}
