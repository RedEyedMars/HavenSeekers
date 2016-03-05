package misc.wrappers;

import java.util.Comparator;

import entity.choice.Choice;


public class ChoiceWrapper implements Comparable<Choice> {
	private Choice choice;
	private Comparator<Choice> howToChose;
	public ChoiceWrapper(Choice choice, Comparator<Choice> howToChose){
		this.choice = choice;
		this.howToChose = howToChose;
	}
	@Override
	public int compareTo(Choice c) {
		return howToChose.compare(choice, c);
	}
}
