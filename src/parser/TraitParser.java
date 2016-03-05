package parser;

import java.util.ArrayList;
import java.util.List;

import main.Hub;
import misc.Rule;
import entity.Entity;
import entity.trait.Trait;
import entity.trait.action.LoseTraitAction;
import location.LocationType;

public class TraitParser extends Parser {

	public Trait nullTrait = new Trait("ROOT");
	@Override
	public void parseInput(String prefix,String suffix,StringHeirachy input){
		if("traits".equals(suffix)){
			for(StringHeirachy s:input){
				parse(s,nullTrait);
			}
		}
	}
	public void parse(StringHeirachy s,Trait parent){
		switch(s.getId()){
		case ':':{
			Trait trait = null;
			if(Hub.traits.containsKey(s.getValue())){
				trait = Hub.traits.get(s.getValue());
			}
			else {
				trait = new Trait(s.getValue());
			}
			if(parent!=nullTrait){
				trait.addParent(parent);
			}
			for(StringHeirachy subs:s){
				parse(subs,trait);
			}
			Hub.traits.put(trait.getName(), trait);
			break;
		}
		case '/':{
			List<Trait> traits = new ArrayList<Trait>();
			String[] split = s.getValue().split("/");
			for(int i=0;i<split.length;++i){
				String name = split[i];
				Trait trait = null;
				if(Hub.traits.containsKey(name)){
					trait = Hub.traits.get(name);
				}
				else {
					trait = new Trait(name);
					Hub.traits.put(name, trait);
				}
				if(i==0){
					Hub.defaultTraits.add(trait);
				}
				traits.add(trait);
			}
			for(int i=0;i<traits.size();++i){
				for(int j=0;j<traits.size();++j){
					if(i==j)continue;
					traits.get(i).addAddOn(new Rule<Entity,Entity>(new LoseTraitAction(traits.get(j))).getAddOn("onGain"));
				}
			}
			break;
		}
		case '?':{
			String value = "? "+s.getValue();
			int indexOfFirstSpace = value.indexOf(' ');
			ParserCommand command = (ParserCommand) parseCommand(value.substring(0,indexOfFirstSpace),
					value.substring(indexOfFirstSpace+1))[0];			
			if(command!=null){
				command.addParameter(ParserCommand.createTrait(parent));
				command.execute();
			}
		}
		default:{
			String value = s.getValue()+s.getId();
			int indexOfFirstSpace = value.indexOf(' ');
			ParserCommand command = (ParserCommand) parseCommand(value.substring(0,indexOfFirstSpace),
					value.substring(indexOfFirstSpace+1))[0];			
			if(command!=null){
				command.addParameter(ParserCommand.createTrait(parent));
				command.execute();
			}
		}
		}
	}
}
