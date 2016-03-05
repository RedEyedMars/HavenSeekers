package parser;

import java.util.List;
import java.util.ArrayList;

import misc.Range;
import misc.action.access.FloatWrapperAction;
import misc.wrappers.FloatWrapper;

import entity.trait.Trait;

@SuppressWarnings("rawtypes")
public abstract class ParserCommand <ReturnType> {

	protected String type;
	protected List<ParserCommand> params;
	protected String[] prototypes;
	
	public String[] getExpectingParameters() {
		return prototypes;
	}

	public String getType(){
		return type;
	}

	public abstract ReturnType execute();

	public void addParameter(ParserCommand param) {
		params.add(param);
	}

	@SuppressWarnings("unchecked")
	public ParserCommand makeCommand(String type, List<ParserCommand> params) {
		try {
			ParserCommand command = this.getClass().newInstance();

			command.type = type;
			command.params = new ArrayList<ParserCommand>();
			command.params.addAll(params);
			return command;
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ParserCommand<String> createString(final String front) {
		return new ParserCommand<String>(){{type = "String";}
			@Override
			public String execute() {
				return front;
			}};
	}

	public static ParserCommand<FloatWrapperAction> createNumber(String front) {
		final Float i = Float.parseFloat(front);
		return new ParserCommand<FloatWrapperAction>(){{type = "Number";}
		@Override
		public FloatWrapperAction execute() {
			return new FloatWrapperAction(new FloatWrapper(i)){
				@Override
				public void act(Object subject) {					
				}};
		}};
	}

	public static ParserCommand<Trait> createTrait(final Trait trait) {
		return new ParserCommand<Trait>(){{type = "Trait";}
		@Override
		public Trait execute() {
			return trait;
		}};
	}

	public static ParserCommand<Range> createRange(final String front) {
		return new ParserCommand<Range>(){{type = "Range";}
			@Override
			public Range execute() {
				return new Range(front);
			}};
	}


}
