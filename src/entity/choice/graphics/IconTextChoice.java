package entity.choice.graphics;

import main.Hub;
import entity.trait.Trait;
import gui.graphics.GraphicElement;
import gui.graphics.collections.TextGraphicElement;
import gui.inputs.MotionEvent;

public class IconTextChoice <ReturnType> extends IconChoice<ReturnType>{

	public IconTextChoice(String text, ReturnType mine) {
		super("gui_box",mine);
		setVisible(false);
		children.add(new TextGraphicElement(text,TextGraphicElement.MEDIUM));
	}

}
