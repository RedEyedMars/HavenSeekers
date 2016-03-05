package location.materials;

import java.util.ArrayList;
import java.util.List;

public class Resource {

	private String name;
	private List<String> properties;
	private String fullname;
	public Resource(ResourcePrototype type){
		this.name = type.getName();
		this.fullname = type.getName();
		this.properties = type.getProperties();
	}
	public boolean is(String string) {
		return properties.contains(string);
	}
	public void addSubtype(String type){
		fullname+=type;
	}
	public String getFullName() {
		return fullname;
	}
	public String getName(){
		return name;
	}

}
