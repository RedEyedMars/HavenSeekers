package entity.choice.action;

import misc.action.Action;
import entity.choice.Choice;

public class PreventChoiceAction implements Action<Choice> {

	@Override
	public void act(Choice subject) {
		subject.addProperty("prevent");
	}

}
