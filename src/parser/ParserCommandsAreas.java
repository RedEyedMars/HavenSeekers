package parser;


import java.util.*;
import environment.ship.*;
import environment.items.*;
import environment.items.action.*;
import time.action.*;
import main.*;
import misc.*;
import misc.action.*;
import misc.action.access.*;
import misc.action.block.*;
import misc.action.math.*;
import misc.condition.*;
import misc.condition.access.*;
import misc.condition.logic.*;
import misc.condition.withable.*;
import misc.wrappers.*;
import entity.*;
import entity.choice.*;
import entity.trait.*;
import entity.trait.action.*;
import entity.trait.condition.*;
import location.action.*;

public class ParserCommandsAreas {
	static void setup(Map<String,ParserCommand> commandMap){
		commandMap.put("is",new ParserCommand<MustHaveTraitCondition>(){
			{type="Condition"; prototypes = new String[]{"Trait..."};}
			@Override
			public MustHaveTraitCondition execute(){
				List<Trait> collection0 = new ArrayList<Trait>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<Trait>)params.get(i)).execute());
				}
				return new MustHaveTraitCondition(collection0);
			}
		});
		commandMap.put("isn't",new ParserCommand<MustntHaveTraitCondition>(){
			{type="Condition"; prototypes = new String[]{"Trait..."};}
			@Override
			public MustntHaveTraitCondition execute(){
				List<Trait> collection0 = new ArrayList<Trait>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<Trait>)params.get(i)).execute());
				}
				return new MustntHaveTraitCondition(collection0);
			}
		});
		commandMap.put("either",new ParserCommand<EitherCondition>(){
			{type="Condition"; prototypes = new String[]{"Condition", "Condition"};}
			@Override
			public EitherCondition execute(){
				return new EitherCondition(((ParserCommand<Condition>)params.get(0)).execute(),
						((ParserCommand<Condition>)params.get(1)).execute());
			}
		});
		commandMap.put("and",new ParserCommand<Condition>(){
			{type="Condition"; prototypes = new String[]{"Condition"};}
			@Override
			public Condition execute(){
				return ((ParserCommand<Condition>)params.get(0)).execute();
			}
		});
		commandMap.put("or",new ParserCommand<Condition>(){
			{type="Condition"; prototypes = new String[]{"Condition"};}
			@Override
			public Condition execute(){
				return ((ParserCommand<Condition>)params.get(0)).execute();
			}
		});
		commandMap.put("has",new ParserCommand<HasCondition>(){
			{type="Condition"; prototypes = new String[]{"Number", "String"};}
			@Override
			public HasCondition execute(){
				return new HasCondition(((ParserCommand<FloatWrapperAction>)params.get(0)).execute(),
						((ParserCommand<String>)params.get(1)).execute());
			}
		});
		commandMap.put("lacks",new ParserCommand<LacksCondition>(){
			{type="Condition"; prototypes = new String[]{"String"};}
			@Override
			public LacksCondition execute(){
				return new LacksCondition(((ParserCommand<String>)params.get(0)).execute());
			}
		});
		commandMap.put("?",new ParserCommand<ObjectCondition>(){
			{type="Condition"; prototypes = new String[]{"Object", "Condition"};}
			@Override
			public ObjectCondition execute(){
				return new ObjectCondition(((ParserCommand<WrapperAction>)params.get(0)).execute(),
						((ParserCommand<Condition>)params.get(1)).execute());
			}
		});
		commandMap.put(">",new ParserCommand<GreaterThanCondition>(){
			{type="Condition"; prototypes = new String[]{"Number", "Number"};}
			@Override
			public GreaterThanCondition execute(){
				return new GreaterThanCondition(((ParserCommand<FloatWrapperAction>)params.get(0)).execute(),
						((ParserCommand<FloatWrapperAction>)params.get(1)).execute());
			}
		});
		commandMap.put("gain",new ParserCommand<GainTraitAction>(){
			{type="Action"; prototypes = new String[]{"Trait"};}
			@Override
			public GainTraitAction execute(){
				return new GainTraitAction(((ParserCommand<Trait>)params.get(0)).execute());
			}
		});
		commandMap.put("lose",new ParserCommand<LoseTraitAction>(){
			{type="Action"; prototypes = new String[]{"Trait"};}
			@Override
			public LoseTraitAction execute(){
				return new LoseTraitAction(((ParserCommand<Trait>)params.get(0)).execute());
			}
		});
		commandMap.put("drain",new ParserCommand<DrainAction>(){
			{type="Action"; prototypes = new String[]{"Number", "Number"};}
			@Override
			public DrainAction execute(){
				return new DrainAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute(),
						((ParserCommand<FloatWrapperAction>)params.get(1)).execute());
			}
		});
		commandMap.put("fill",new ParserCommand<FillAction>(){
			{type="Action"; prototypes = new String[]{"Number", "Number"};}
			@Override
			public FillAction execute(){
				return new FillAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute(),
						((ParserCommand<FloatWrapperAction>)params.get(1)).execute());
			}
		});
		commandMap.put("heal",new ParserCommand<HealAction>(){
			{type="Action"; prototypes = new String[]{};}
			@Override
			public HealAction execute(){
				return new HealAction();
			}
		});
		commandMap.put("addSpace",new ParserCommand<AddSpaceAction>(){
			{type="Action"; prototypes = new String[]{"String"};}
			@Override
			public AddSpaceAction execute(){
				return new AddSpaceAction(((ParserCommand<String>)params.get(0)).execute());
			}
		});
		commandMap.put("set",new ParserCommand<SetAction>(){
			{type="Action"; prototypes = new String[]{"String", "Number"};}
			@Override
			public SetAction execute(){
				return new SetAction(((ParserCommand<String>)params.get(0)).execute(),
						((ParserCommand<FloatWrapperAction>)params.get(1)).execute());
			}
		});
		commandMap.put("while",new ParserCommand<WhileAction>(){
			{type="Action"; prototypes = new String[]{"Condition"};}
			@Override
			public WhileAction execute(){
				return new WhileAction(((ParserCommand<Condition>)params.get(0)).execute());
			}
		});
		commandMap.put("if",new ParserCommand<IfAction>(){
			{type="Action"; prototypes = new String[]{"Condition..."};}
			@Override
			public IfAction execute(){
				List<Condition> collection0 = new ArrayList<Condition>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<Condition>)params.get(i)).execute());
				}
				return new IfAction(collection0);
			}
		});
		commandMap.put("onstart",new ParserCommand<OnStartAction>(){
			{type="Action"; prototypes = new String[]{"String", "String"};}
			@Override
			public OnStartAction execute(){
				return new OnStartAction(((ParserCommand<String>)params.get(0)).execute(),
						((ParserCommand<String>)params.get(1)).execute());
			}
		});
		commandMap.put("addTraitChoice",new ParserCommand<AddTraitChoiceAction>(){
			{type="Action"; prototypes = new String[]{"Trait..."};}
			@Override
			public AddTraitChoiceAction execute(){
				List<Trait> collection0 = new ArrayList<Trait>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<Trait>)params.get(i)).execute());
				}
				return new AddTraitChoiceAction(collection0);
			}
		});
		commandMap.put("print",new ParserCommand<PrintAction>(){
			{type="Action"; prototypes = new String[]{"Number"};}
			@Override
			public PrintAction execute(){
				return new PrintAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute());
			}
		});
		commandMap.put("every",new ParserCommand<EveryAction>(){
			{type="Action"; prototypes = new String[]{"Number", "String"};}
			@Override
			public EveryAction execute(){
				return new EveryAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute(),
						((ParserCommand<String>)params.get(1)).execute());
			}
		});
		commandMap.put("transferResource",new ParserCommand<TransferResourceAction>(){
			{type="Action"; prototypes = new String[]{"Number", "Number", "Object", "String", "Object"};}
			@Override
			public TransferResourceAction execute(){
				return new TransferResourceAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute(),
						((ParserCommand<FloatWrapperAction>)params.get(1)).execute(),
						((ParserCommand<WrapperAction>)params.get(2)).execute(),
						((ParserCommand<String>)params.get(3)).execute(),
						((ParserCommand<WrapperAction>)params.get(4)).execute());
			}
		});
		commandMap.put("implantMaterials",new ParserCommand<ImplantMaterialsAction>(){
			{type="Action"; prototypes = new String[]{"Object", "Range"};}
			@Override
			public ImplantMaterialsAction execute(){
				return new ImplantMaterialsAction(((ParserCommand<WrapperAction>)params.get(0)).execute(),
						((ParserCommand<Range>)params.get(1)).execute());
			}
		});
		commandMap.put("satellite",new ParserCommand<SetSatelliteTypeAction>(){
			{type="Action"; prototypes = new String[]{"String", "String"};}
			@Override
			public SetSatelliteTypeAction execute(){
				return new SetSatelliteTypeAction(((ParserCommand<String>)params.get(0)).execute(),
						((ParserCommand<String>)params.get(1)).execute());
			}
		});
		commandMap.put("mass",new ParserCommand<SetMassAction>(){
			{type="Action"; prototypes = new String[]{"Range"};}
			@Override
			public SetMassAction execute(){
				return new SetMassAction(((ParserCommand<Range>)params.get(0)).execute());
			}
		});
		commandMap.put("moons",new ParserCommand<SetMoonsAction>(){
			{type="Action"; prototypes = new String[]{"Range"};}
			@Override
			public SetMoonsAction execute(){
				return new SetMoonsAction(((ParserCommand<Range>)params.get(0)).execute());
			}
		});
		commandMap.put("temperature",new ParserCommand<SetTemperatureAction>(){
			{type="Action"; prototypes = new String[]{"Range"};}
			@Override
			public SetTemperatureAction execute(){
				return new SetTemperatureAction(((ParserCommand<Range>)params.get(0)).execute());
			}
		});
		commandMap.put("density",new ParserCommand<SetDensityAction>(){
			{type="Action"; prototypes = new String[]{"Range"};}
			@Override
			public SetDensityAction execute(){
				return new SetDensityAction(((ParserCommand<Range>)params.get(0)).execute());
			}
		});
		commandMap.put("random",new ParserCommand<RandomAction>(){
			{type="Number"; prototypes = new String[]{};}
			@Override
			public RandomAction execute(){
				return new RandomAction();
			}
		});
		commandMap.put("sum",new ParserCommand<SumAction>(){
			{type="Number"; prototypes = new String[]{"Number..."};}
			@Override
			public SumAction execute(){
				List<FloatWrapperAction> collection0 = new ArrayList<FloatWrapperAction>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<FloatWrapperAction>)params.get(i)).execute());
				}
				return new SumAction(collection0);
			}
		});
		commandMap.put("product",new ParserCommand<ProductAction>(){
			{type="Number"; prototypes = new String[]{"Number..."};}
			@Override
			public ProductAction execute(){
				List<FloatWrapperAction> collection0 = new ArrayList<FloatWrapperAction>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<FloatWrapperAction>)params.get(i)).execute());
				}
				return new ProductAction(collection0);
			}
		});
		commandMap.put("since",new ParserCommand<SinceAction>(){
			{type="Number"; prototypes = new String[]{"String"};}
			@Override
			public SinceAction execute(){
				return new SinceAction(((ParserCommand<String>)params.get(0)).execute());
			}
		});
		commandMap.put("now",new ParserCommand<NowAction>(){
			{type="Number"; prototypes = new String[]{};}
			@Override
			public NowAction execute(){
				return new NowAction();
			}
		});
		commandMap.put("+",new ParserCommand<Action>(){
			{type="Number"; prototypes = new String[]{"Number"};}
			@Override
			public Action execute(){
				return ((ParserCommand<FloatWrapperAction>)params.get(0)).execute();
			}
		});
		commandMap.put("*",new ParserCommand<Action>(){
			{type="Number"; prototypes = new String[]{"Number"};}
			@Override
			public Action execute(){
				return ((ParserCommand<FloatWrapperAction>)params.get(0)).execute();
			}
		});
		commandMap.put("/",new ParserCommand<InverseAction>(){
			{type="Number"; prototypes = new String[]{"Number"};}
			@Override
			public InverseAction execute(){
				return new InverseAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute());
			}
		});
		commandMap.put("-",new ParserCommand<NegativeNumberAction>(){
			{type="Number"; prototypes = new String[]{"Number"};}
			@Override
			public NegativeNumberAction execute(){
				return new NegativeNumberAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute());
			}
		});
		commandMap.put("from",new ParserCommand<FromAction>(){
			{type="Number"; prototypes = new String[]{"Object", "String"};}
			@Override
			public FromAction execute(){
				return new FromAction(((ParserCommand<WrapperAction>)params.get(0)).execute(),
						((ParserCommand<String>)params.get(1)).execute());
			}
		});
		commandMap.put("as",new ParserCommand<NormalizeAction>(){
			{type="Number"; prototypes = new String[]{"Number", "Condition"};}
			@Override
			public NormalizeAction execute(){
				return new NormalizeAction(((ParserCommand<FloatWrapperAction>)params.get(0)).execute(),
						((ParserCommand<Condition>)params.get(1)).execute());
			}
		});
		commandMap.put("ship's",new ParserCommand<GetShipAction>(){
			{type="Object"; prototypes = new String[]{};}
			@Override
			public GetShipAction execute(){
				return new GetShipAction();
			}
		});
		commandMap.put("entity's",new ParserCommand<GetEntityAction>(){
			{type="Object"; prototypes = new String[]{};}
			@Override
			public GetEntityAction execute(){
				return new GetEntityAction();
			}
		});
		commandMap.put("area",new ParserCommand<GetAreaAction>(){
			{type="Object"; prototypes = new String[]{};}
			@Override
			public GetAreaAction execute(){
				return new GetAreaAction();
			}
		});
		commandMap.put("wares",new ParserCommand<GetWaresAction>(){
			{type="Object"; prototypes = new String[]{};}
			@Override
			public GetWaresAction execute(){
				return new GetWaresAction();
			}
		});
		commandMap.put("location",new ParserCommand<GetLocationAction>(){
			{type="Object"; prototypes = new String[]{};}
			@Override
			public GetLocationAction execute(){
				return new GetLocationAction();
			}
		});
		commandMap.put("randomized",new ParserCommand<RandomizedMaterialAction>(){
			{type="Object"; prototypes = new String[]{"String..."};}
			@Override
			public RandomizedMaterialAction execute(){
				List<String> collection0 = new ArrayList<String>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<String>)params.get(i)).execute());
				}
				return new RandomizedMaterialAction(collection0);
			}
		});
		commandMap.put("traits",new ParserCommand<TraitsList>(){
			{type="Trait"; prototypes = new String[]{"Trait..."};}
			@Override
			public TraitsList execute(){
				List<Trait> collection0 = new ArrayList<Trait>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<Trait>)params.get(i)).execute());
				}
				return new TraitsList(collection0);
			}
		});
		commandMap.put("randomTrait",new ParserCommand<RandomTraitFromList>(){
			{type="Trait"; prototypes = new String[]{"Trait..."};}
			@Override
			public RandomTraitFromList execute(){
				List<Trait> collection0 = new ArrayList<Trait>();
				for(int i=0;i<params.size();++i){
					collection0.add(((ParserCommand<Trait>)params.get(i)).execute());
				}
				return new RandomTraitFromList(collection0);
			}
		});
		commandMap.put("\"",new ParserCommand<String>(){
			{type="String"; prototypes = new String[]{"String..."};}
			@Override
			public String execute(){
				StringBuilder builder = new StringBuilder();
				for(int i=0;i<params.size();++i){
					builder.append(((ParserCommand<String>)params.get(i)).execute());
					builder.append(' ');
				}
				return builder.toString().trim();
			}
		});
		commandMap.put("from",commandMap.get("from"));
		commandMap.put("by",commandMap.get("from"));
		commandMap.put("to",commandMap.get("from"));
		commandMap.put("of",commandMap.get("from"));
		commandMap.put("the",commandMap.get("from"));
		commandMap.put("ship's",commandMap.get("ship's"));
		commandMap.put("ship",commandMap.get("ship's"));
		commandMap.put("entity's",commandMap.get("entity's"));
		commandMap.put("entity",commandMap.get("entity's"));

	}
}
