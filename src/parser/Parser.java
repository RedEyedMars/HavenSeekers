package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.choice.Choice;
import entity.choice.action.AddIntValueAction;
import entity.choice.action.PreventChoiceAction;
import entity.trait.Trait;
import environment.items.Area;
import location.LocationPrototype;
import location.LocationType;
import location.materials.ResourcePrototype;
import loom.Processor;
import main.Hub;
import misc.Rule;
import misc.action.Action;
import misc.action.access.FloatWrapperAction;
import misc.condition.Condition;
import misc.condition.withable.WithCondition;
import misc.condition.withable.Withable;
import misc.wrappers.FloatWrapper;

@SuppressWarnings({"unchecked","rawtypes"})
public class Parser {
	protected String header = "";
	private static Map<String,ParserCommand> commandMap = new HashMap<String,ParserCommand>();
	static {
		commandMap.put("on", new ParserCommand<Object>(){
			{ type = "Command"; prototypes = new String[]{"String","Action","Condition"};}
			@Override
			public Object execute() {
				((ParserCommand<Trait>)params.get(3)).execute().addAddOn(
						new Rule<Choice,Choice>(((ParserCommand<Action<Choice>>)params.get(1)).execute(),
								((ParserCommand<Condition<Choice>>)params.get(2)).execute())
								.getAddOn("on"+((ParserCommand<String>)params.get(0)).execute())
						);
				return null;
			}
		});
		commandMap.put("with", new ParserCommand<Condition<Withable>>(){
			{type = "Condition"; prototypes = new String[]{"String"};}
			@Override
			public Condition<Withable> execute() {
				return new WithCondition(((ParserCommand<String>)params.get(0)).execute());
			}			
		});
		commandMap.put("prevent", new ParserCommand<Action<Choice>>(){
			{type = "Action"; prototypes = new String[]{};}
			@Override
			public Action<Choice> execute() {
				return new PreventChoiceAction();
			}			
		});
		commandMap.put("add", new ParserCommand<Action<FloatWrapper>>(){
			{type = "Action"; prototypes = new String[]{"Number"};}
			@Override
			public Action<FloatWrapper> execute() {
				return new AddIntValueAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute());
			}			
		});
		ParserCommandsAreas.setup(commandMap);
	}

	public static void parseAllConfigs(){

		Parser parser;
		parser = new TraitParser();
		File traitsDirectory = new File("res/traits/");
		for(String filename:traitsDirectory.list()){
			new ParseFileProcess(parser,"res/"+traitsDirectory.getName()+File.separator+filename);
		}
		parser = new ResourceParser();
		File recipesDirectory = new File("res/recipes/");
		for(String filename:recipesDirectory.list()){
			new ParseFileProcess(parser,"res/"+recipesDirectory.getName()+File.separator+filename);
		}
		Processor.joinAll();
		parser = new AreaParser();
		File areasDirectory = new File("res/areas/");
		for(String filename:areasDirectory.list()){
			new ParseFileProcess(parser,"res/"+areasDirectory.getName()+File.separator+filename);
		}
		parser = new LocationParser();
		File locationDirectory = new File("res/locations/");
		for(String filename:locationDirectory.list()){
			new ParseFileProcess(parser,"res/"+locationDirectory.getName()+File.separator+filename);
		}
		Processor.joinAll();
	}

	public void readAndParseFile(String filename){
		StringHeirachy input = new StringHeirachy();
		StringHeirachy root = input;
		String prefix = filename.substring(filename.lastIndexOf(File.separator)+1,filename.lastIndexOf('.'));
		String suffix = filename.substring(filename.lastIndexOf('.')+1);		
		BufferedReader reader;
		boolean rulesDone = false;
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String line = reader.readLine();
			while(line!=null){
				if("header".equals(suffix)){
					header=header+line+"\n";
				}
				else {
					if(!rulesDone){
						if(line.startsWith("=")){
							int firstSpace = line.indexOf(' ');
							int secondSpace = line.indexOf(' ', firstSpace+1);
							input.addRule(line.substring(firstSpace+1,secondSpace),line.substring(secondSpace+1));
							line = reader.readLine();
							continue;
						}
						
					}
					rulesDone = true;
					if(!line.matches("\\s*#.*")&&!line.matches("\\s*")){
						if(line.endsWith("\\\\")){
							StringBuilder longLine = new StringBuilder();
							longLine.append(line.replace("    ", "\t").replace("\\\\", ""));
							line = reader.readLine();
							line = line.replaceFirst("\\s+", "");
							while(line.endsWith("\\\\")){
								longLine.append(line.replace("    ", "\t").replace("\\\\", ""));
								line = reader.readLine();
								line = line.replaceFirst("\\s+", "");
							}
							longLine.append(line.replace("    ", "\t").replace("\\\\", ""));
							input = input.append(longLine.toString());
						}
						else {
							input = input.append(line.replace("    ", "\t"));
						}
					}

				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		parseInput(prefix, suffix,root);		
	}
	
	public void parseInput(String prefix, String suffix,StringHeirachy input){		
	}	

	protected Object[] parseCommand(String commandName, String line) {
		String orgLine = line;
		if(commandMap.containsKey(commandName)){
			ParserCommand command = commandMap.get(commandName);
			boolean fulfilled = true;
			List<ParserCommand> params = new ArrayList<ParserCommand>();
			boolean consumeRest = command.getExpectingParameters().length>0?command.getExpectingParameters()[0].endsWith("..."):false;
			for(int i=0;i<command.getExpectingParameters().length||consumeRest;){
				String parameter = command.getExpectingParameters()[i];				
				if(!consumeRest){
					consumeRest = parameter.endsWith("...");
				}
				if(consumeRest&&parameter.endsWith("...")){
					parameter = parameter.substring(0,parameter.length()-3);
				}
				int indexOfFirstSpace = line.indexOf(' ');
				String front = line;
				if(indexOfFirstSpace>-1){
					front = line.substring(0,indexOfFirstSpace);
					if(consumeRest&&front.endsWith(",")){
						front = front.substring(0,front.length()-1);
					}
				}
				line = line.substring(indexOfFirstSpace+1);
				if(indexOfFirstSpace==-1){
					line = "";
				}

				if(parameter.equals("Range")){
					params.add(ParserCommand.createRange(front));
				}
				else if(parameter.equals("String")&&!front.equals("\"")){
					params.add(ParserCommand.createString(front));
				}
				else if(parameter.equals("Trait")&&!front.equals("traits")){
					params.add(ParserCommand.createTrait(Hub.traits.get(front)));
				}
				else if(commandMap.containsKey(front)){
					Object[] ret = parseCommand(front,line);
					if(ret[0]!=null&&((ParserCommand)ret[0]).getType().equals(parameter)){
						params.add((ParserCommand) ret[0]);
						line = (String) ret[1];
					}
					else {
						fulfilled = false;
						break;
					}
				}
				else if(parameter.equals("Number")){
					try{
						ParserCommand<FloatWrapperAction> c = ParserCommand.createNumber(front);
						params.add(c);
					}
					catch(NumberFormatException e){
						fulfilled = false;
					}
				}
				if(!consumeRest){
					++i;
				}
				else if(consumeRest){
					if("".equals(line)){
						consumeRest = false;
						++i;
					}
					else if(line.startsWith("; ")){
						line = line.substring(2);
						consumeRest = false;
						++i;
					}
				}
			}
			if(fulfilled){
				return new Object[]{command.makeCommand(command.getType(),params),line};
			}
			else {
				System.err.println("Parameters unfulfilled for "+commandName+" on line "+commandName+" "+orgLine);
			}
		}
		else {
			System.err.println("Parser Command Name not found:"+commandName+" in line: "+commandName+" "+orgLine);
		}
		return new Object[]{null,line};
	}
}
