package location.materials;

import java.util.ArrayList;
import java.util.List;

import main.Hub;

public class ResourcePrototype {

	private String name;
	private List<String> properties = new ArrayList<String>();
	private List<String> subtypes = new ArrayList<String>();
	private List<Integer> subtypeIds = new ArrayList<Integer>();
	private static int ids = 0;
	private int id = 0;
	private int index = 0;
	public ResourcePrototype(String value) {
		name = value;
		index = ids++;
		id = (int) Math.pow(2,index);
	}
	public String getName() {
		return name;
	}
	public List<String> getSubtypes() {
		return subtypes;
	}
	public void addSubType(String string) {
		subtypes.add(string);
		subtypeIds.add((int) Math.pow(2,index+subtypes.size()));
	}

	public List<String> getProperties() {
		return properties;
	}
	public void addProperty(String value) {
		properties.add(value);
	}
	public int id(String name){
		int i = id;
		for(int j=0;j<subtypes.size();++j){
			if(name.contains(subtypes.get(j))){
				i+=subtypeIds.get(j);
			}
		}
		return i;
	}
	public static int translateResourceName(String name){
		for(String resourceName:Hub.resourceTypes.keySet()){
			if(name.contains(resourceName)){
				return Hub.resourceTypes.get(resourceName).id(name);
			}
		}
		return -1;
	}
	public static String translateResourceId(Float mid) {
		
		int id = (int)(float)mid;
		StringBuilder builder = new StringBuilder();
		List<Boolean> ids = new ArrayList<Boolean>();
		int factor = 2;
		while(id>0){
			if(id%factor>0){
				ids.add(true);
				id-=id%factor;
			}
			else ids.add(false);
			factor*=2;
		}
		while(ids.size()<7){
			ids.add(false);
		}
		ResourcePrototype rp = null;
		int i=0;
		for(String name:Hub.resourceTypes.keySet()){
			if(ids.get(i)){
				builder.append(name);
				rp = Hub.resourceTypes.get(name);
				break;
			}
			++i;
		}
		++i;
		if(rp==null)return "not found";
		for(String subtype:rp.getSubtypes()){
			if(ids.get(i)){
				builder.append(subtype);
			}
			++i;
		}
		return builder.toString();
	}
	public Resource create(){
		Resource resource = new Resource(this);
		for(int i=0;i<subtypes.size();++i){
			if(Math.random()>=0.5f){
				resource.addSubtype(subtypes.get(i));
			}
		}
		return resource;
	}
}