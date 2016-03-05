package gui.graphics.collections;

import gui.graphics.GraphicEntity;
import gui.inputs.MotionEvent;

public class GraphicButton extends GraphicEntity {

	private boolean pressed;

	public GraphicButton(String text) {
		super("button_1");
		this.pressed = false;
		this.addChild(new TextGraphicElement(text,TextGraphicElement.MEDIUM));
	}
	
	@Override
	public void performOnClick(MotionEvent event){
		togglePressed();
		this.performPress(event);
	}
	
	public void togglePressed() {
		this.pressed = true;
	}

	public void performPress(MotionEvent event){		
	}
	@Override
	public void animate(){
		if(pressed){
			this.entity.setFrame(this.textureIndex()+1);
		}
		if(this.entity.textureIndex()>4){
			this.pressed = false;
			this.entity.setFrame(0);
		}
	}
}
