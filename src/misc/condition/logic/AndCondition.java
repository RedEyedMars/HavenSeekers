package misc.condition.logic;

import java.util.ArrayList;

import misc.condition.Condition;


public class AndCondition<Subject> extends ArrayList<Condition<Subject>> implements Condition<Subject>{

	private static final long serialVersionUID = 7136103763732314377L;

	@Override
	public boolean satisfied(Subject o) {
		for(Condition<Subject> condition:this){
			if(!condition.satisfied(o)){
				return false;
			}
		}
		return true;
	}

}
