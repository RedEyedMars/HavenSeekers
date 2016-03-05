package parser;

import misc.action.Action;
import misc.action.ActionAcceptor;
import misc.condition.Condition;
import time.action.OnStartAction;
import entity.Entity;
import entity.choice.ChoiceGenerator;
import entity.choice.ChoicePrototype;
import environment.items.Area;
import environment.items.AreaPrototype;
import location.LocationType;
import main.Hub;

public class AreaParser extends Parser{
	@Override
	public void parseInput(String prefix,String suffix, StringHeirachy input){
		if("area".equals(suffix)){
			AreaPrototype area = new AreaPrototype(prefix);
			Hub.areas.put(area.getName(),area);
			for(StringHeirachy s:input){
				parse(s,null,null,area,area);
			}
		}
	}
	public void parse(StringHeirachy s,ChoicePrototype proto, ActionAcceptor<Entity> actionAcceptor, ChoiceGenerator generator, AreaPrototype area){
		switch(s.getId()){
		case ':':{
			proto = new ChoicePrototype(s.getValue());
			generator.addChoicePrototype(proto);
			for(StringHeirachy subs:s){
				parse(subs,proto,proto,generator,area);
			}
			break;
		}
		case ';':{
			if(s.getValue().contains("=")){
				area.addProperty(
						s.getValue().substring(0,s.getValue().indexOf('=')).trim(),
						Float.parseFloat(s.getValue().substring(s.getValue().indexOf('=')+1).trim()));
			}
			else {
				area.addProperty(s.getValue(), 1f);
			}
			break;
		}
		case '.':{
			proto.addProperty(s.getValue());
			break;
		}
		case '?':{
			String value = "? "+s.getValue();
			int indexOfFirstSpace = value.indexOf(' ');
			ParserCommand<Condition<Entity>> command = 
					(ParserCommand<Condition<Entity>>) 
					parseCommand(value.substring(0,indexOfFirstSpace),
							value.substring(indexOfFirstSpace+1))[0];			
			if(command!=null){
				proto.addCondition(command.execute());
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
					Action<Entity> a = (Action<Entity>)o;

					if(a instanceof OnStartAction){
						proto.addOnStart(a);
					}
					else {
						actionAcceptor.add(a);
					}
					if(a instanceof ActionAcceptor){
						for(StringHeirachy subs:s){
							parse(subs,proto,(ActionAcceptor) a,generator,area);
						}
					}
				}
				else if(o instanceof Condition){
					Condition<Entity> c = (Condition<Entity>)o;
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
