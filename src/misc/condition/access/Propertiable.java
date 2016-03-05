package misc.condition.access;

import misc.wrappers.FloatWrapper;

public interface Propertiable {
	public FloatWrapper getProperty(String property);
	public boolean addProperty(String property, Float value);
}
