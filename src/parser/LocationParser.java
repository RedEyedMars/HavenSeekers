package parser;

import location.Location;
import location.LocationPrototype;
import location.LocationType;
import main.Hub;
import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.condition.Condition;
import time.action.OnStartAction;
import entity.Entity;
import entity.choice.ChoiceGenerator;
import entity.choice.ChoicePrototype;
import environment.items.Area;

public class LocationParser extends Parser{
	@Override
	public void parseInput(String prefix,String suffix,StringHeirachy input){
		if("bodies".equals(suffix)){
			LocationType type = new LocationType(prefix);
			for(StringHeirachy s:input){
				parse(s,type,null,null);
			}
			Hub.locationTypes.put(type.getName(),type);
		}
	}
	public void parse(StringHeirachy s,LocationType type, LocationPrototype proto, ActionAcceptor<Location> actionAcceptor){
		switch(s.getId()){
		case ':':{
			String value = s.getValue();
			LocationPrototype parent = proto;
			if(proto!=null){
				value = proto.getName()+"_"+value;
			}
			proto = new LocationPrototype(value, type);
			for(StringHeirachy subs:s){
				parse(subs,type,proto,proto);
			}
			if(parent!=null){
				parent.addSubType(proto);
			}
			type.add(proto);
			break;
		}
		case '.':{
			//proto.addProperty(s.getValue());
			break;
		}
		case '%':{
			String value = "implantMaterials "+s.getValue();
			int indexOfFirstSpace = value.indexOf(' ');
			ParserCommand<Action<Location>> command = 
					(ParserCommand<Action<Location>>) 
					parseCommand(value.substring(0,indexOfFirstSpace),
							value.substring(indexOfFirstSpace+1))[0];			
			if(command!=null){
				proto.add(command.execute());
			}
			break;
		}
		default:{
			String value = s.getValue()+s.getId();
			int indexOfFirstSpace = value.indexOf(' ');
			String currentPosition = null;
			String rest = null;
			if(indexOfFirstSpace>=0){
				currentPosition = value.substring(0,indexOfFirstSpace);
				rest = value.substring(indexOfFirstSpace+1);
			}
			else {
				currentPosition = value;
				rest = "";
			}
			Object[] ret = parseCommand(currentPosition,rest);
			if(ret[1]!=null&&!"".equals(ret[1])){
				System.err.println("Argument(s) "+ret[1]+" left unconsumed on line "+currentPosition+" "+rest);
			}
			ParserCommand command = (ParserCommand) ret[0];			
			if(command!=null){
				Object o = command.execute();
				if(o instanceof Action){
					if(proto!=null){
						Action<Location> a = (Action<Location>)o;
						proto.add(a);
						if(a instanceof ActionAcceptor){
							for(StringHeirachy subs:s){
								parse(subs,type,proto,(ActionAcceptor) a);
							}
						}
					}
					else {
						Action<LocationType> a = (Action<LocationType>)o;
						a.act(type);
					}
				}
				else if(o instanceof Condition){
					Condition<Location> c = (Condition<Location>)o;
					proto.addCondition(c);
				}
				else {
					System.err.println(o+" was not of an expected type");
				}
			}
		}
		}
	}
}
