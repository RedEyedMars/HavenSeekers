package gui.menu;

import gui.graphics.GraphicEntity;

public class MenuOption extends GraphicEntity{

	private Modifiable target;
	private String modName;
	public MenuOption(String textureName, String modName) {
		super(textureName);
		this.modName = modName;
	}

	public void setTarget(Modifiable target) {
		this.target = target;
	}

	public void receive(Object... data) {
		this.target.modify(modName, data);
	}

}
