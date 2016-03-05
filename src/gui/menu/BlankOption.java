package gui.menu;

public class BlankOption extends MenuOption {

	public BlankOption() {
		super("blank","");
		this.entity.setVisible(false);
		this.entity.on(false);
	}

	public boolean isWithin(float dx, float dy){
		return false;
	}
}
