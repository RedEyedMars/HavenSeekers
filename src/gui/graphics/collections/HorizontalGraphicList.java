package gui.graphics.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Hub;
import gui.graphics.GraphicElement;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;

public class HorizontalGraphicList extends GraphicEntity {

	public HorizontalGraphicList(GraphicView... elements) {
		super("gui_box");
		for(GraphicView element:elements){
			addChild(element);
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
			children.get(i).adjust(dWidth/children.size(),dHeight);
			children.get(i).setY(iHeight);
			children.get(i).setX(iWidth+(((float)i)/children.size())*dWidth);
		}
	}

	@Override
	public float offsetY(int index){
		return getHeight()*0.025f;
	}

	@Override
	public float offsetX(int index){
		return (((float)index)/children.size())*0.95f*getWidth()+getWidth()*0.025f;
	}
	@Override
	public void onDraw(){
		//System.out.println("draw list");
	}

}
