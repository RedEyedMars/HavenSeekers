package gui.menu;

public interface Modifiable extends Cloneable{
	public Modifiable clone();
	public void modify(String operation, Object... arguments);
}
