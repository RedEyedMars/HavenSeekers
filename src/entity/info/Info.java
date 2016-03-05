package entity.info;

import gui.graphics.GraphicView;

public abstract class Info {

	protected GraphicView display;
	public Info(GraphicView display) {
		this.display = display;
	}
	public void display(boolean b){
		display.setVisible(b);
	}
	public GraphicView getDisplay() {
		return display;
	}
	public abstract void update(Info info);
}
