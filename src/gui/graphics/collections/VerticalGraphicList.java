package gui.graphics.collections;

import main.Hub;
import gui.graphics.GraphicElement;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;

public class VerticalGraphicList extends GraphicEntity{

	public VerticalGraphicList(GraphicView... elements) {
		super("gui_box");
		for(int i=0;i<elements.length;++i){
			addChild(elements[i]);
		}
	}
	@Override
	public void adjust(float x, float y){
		super.adjust(x, y);
		float dHeight = getHeight()*0.95f;
		float dWidth = getWidth()*0.95f;
		float iHeight = getY()+0.025f*getHeight();
		float iWidth = getX()+0.025f*getWidth();
		for(int i = 0;i<children.size();++i){
			children.get(i).adjust(dWidth,dHeight/children.size());
		}
	}
	
	@Override
	public float offsetX(int index){
		return 0.025f*getWidth();
	}
	
	@Override
	public float offsetY(int index) {
		return (((float)index)/children.size())*0.95f*getHeight()+getHeight()*0.025f;
	}
}
