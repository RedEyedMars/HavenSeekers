package entity.choice.storage;

import entity.choice.ChoicePrototype;
import misc.condition.Condition;
import parser.StringHeirachy;
import storage.Storer;

public class ChoicePrototypeStorer extends Storer<ChoicePrototype>{

	public ChoicePrototypeStorer(ChoicePrototype self) {
		super(self);
	}

	@Override
	protected String storeSelf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iterable<Condition<String>> loadRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadSelf(StringHeirachy input) {
		// TODO Auto-generated method stub
		
	}

}
