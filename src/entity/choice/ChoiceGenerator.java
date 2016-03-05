package entity.choice;


public interface ChoiceGenerator extends Iterable<ChoicePrototype>{
	public void addChoicePrototype(ChoicePrototype c);
	public ChoicePrototype getChoicePrototype(int i);
	public int sizeChoicePrototype();
}
