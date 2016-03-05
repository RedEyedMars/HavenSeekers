package gui.menu;

import gui.graphics.collections.GraphicButton;
import gui.inputs.MotionEvent;

public abstract class ButtonOption extends MenuOption{

	public ButtonOption(String text, String modName) {
		super("blank", modName);
		this.entity.on(false);
		this.addChild(new GraphicButton(text){
			@Override
			public void performPress(MotionEvent event){
				onPress();
			}
		});
	}

	public abstract void onPress();

}
