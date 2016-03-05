package gui.menu;

import environment.Position;
import environment.Positionable;
import gui.graphics.GraphicEntity;
import gui.graphics.collections.TextGraphicElement;
import gui.inputs.MotionEvent;

public class TabChoice extends GraphicEntity implements Positionable{

	private TabMenu menu;
	private String text;
	private int index;
	private Position position;

	public TabChoice(String text, int index, int x, int y) {
		super("tab_choice");
		addChild(new TextGraphicElement(text,TextGraphicElement.BIG));
		this.text = text;
		this.index = index;
		this.position = new Position(x,y);
	}

	public void setParent(TabMenu menu){
		this.menu = menu;
	}
	
	@Override
	public void performOnClick(MotionEvent event){
		menu.select(index);
	}

	public void setSelected(boolean isSelected) {
		this.entity.setFrame(isSelected?1:0);
	}
	
	@Override
	public float offsetY(int index){
		return -getHeight()*0.3f;
	}

	@Override
	public Position getPosition() {
		return position;
	}
	
}