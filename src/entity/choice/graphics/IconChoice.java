package entity.choice.graphics;

import java.util.List;

import entity.choice.Choice;
import entity.trait.Trait;
import environment.items.Area;
import gui.graphics.GraphicElement;
import gui.graphics.GraphicEntity;
import gui.inputs.MotionEvent;

public class IconChoice <ReturnType> extends GraphicEntity {
	private boolean selected;
	private ReturnType mine;
	public IconChoice(String textureName, ReturnType mine) {
		super(textureName);
		this.mine = mine;
		adjust(0.1f,0.1f);
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void onClick() {
		selected = true;
	}
	public void onHover() {
		// TODO Auto-generated method stub
		
	}
	public ReturnType getObject() {
		return mine;
	}
	public static IconChoice[] getIcons(List<? extends Iconable> is) {
		IconChoice[] icons = new IconChoice[is.size()];
		for(int i = 0;i<is.size();++i){
			icons[i] = is.get(i).getIcon();
		}
		return icons;
	}
	@Override
	public float offsetX(int index) {
		return 0;
	}
	@Override
	public float offsetY(int index) {
		return 0;
	}

}
