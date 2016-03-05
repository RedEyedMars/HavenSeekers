package gui.graphics.collections;

import gui.graphics.GraphicEntity;
import gui.inputs.MotionEvent;

public class GraphicToggleButton extends GraphicEntity {

	private boolean pressed;

	public GraphicToggleButton(String text) {
		super("button_1");
		this.pressed = false;
		this.addChild(new TextGraphicElement(text,TextGraphicElement.MEDIUM));
	}
	
	@Override
	public void performOnClick(MotionEvent event){
		togglePressed();
		if(pressed){
			this.performPress(event);
		}
	}
	
	public void togglePressed() {
		this.pressed = !pressed;
		this.entity.setFrame(pressed?1:0);
	}

	public void performPress(MotionEvent event){		
	}
}
