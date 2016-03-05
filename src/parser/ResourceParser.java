package parser;

import location.LocationType;
import location.materials.ResourcePrototype;
import main.Hub;

public class ResourceParser extends Parser{
	@Override
	public void parseInput(String prefix,String suffix,StringHeirachy input){
		if("materials".equals(suffix)){
			for(StringHeirachy s:input){
				parseMaterial(s,null);
			}
		}
	}
	public void parseMaterial(StringHeirachy s, ResourcePrototype rp){
		if(rp==null){
			rp = new ResourcePrototype(s.getValue());
			for(StringHeirachy subs:s){
				parseMaterial(subs,rp);
			}
			Hub.resourceTypes.put(rp.getName(), rp);
		}
		else {
			if('.'==s.getId()){
				rp.addProperty(s.getValue());
			}
			else {
				rp.addSubType(s.getValue()+s.getId());
			}
		}
	}
}
