package entity.decision;

import java.util.List;
import entity.choice.TraitChoice;
import entity.choice.graphics.PopupChoice;
import entity.trait.Trait;

public class TraitDecision extends Decision<Trait>{
	private static final long serialVersionUID = -7877847336924114751L;

	@SuppressWarnings("unchecked")
	public TraitDecision(String displayString,List<Trait> traits){
		super(PopupChoice.createPopup(displayString,traits));
		for(Trait trait:traits){
			TraitChoice choice = new TraitChoice(trait);
			this.add(choice);
		}
	}

}
