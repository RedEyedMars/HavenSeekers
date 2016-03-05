package gui.graphics.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Hub;
import gui.graphics.GraphicElement;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;

public class HorizontalGraphicArray extends GraphicView {

	private float[] percentages;
	public HorizontalGraphicArray(float[] percentages,GraphicView... elements) {
		super();
		this.percentages = percentages;
		for(GraphicView element:elements){
			addChild(element);
		}
	}
	@Override
	public void adjust(float x, float y){
		this.width = x;
		this.height = y;
		for(int i = 0;i<children.size();++i){
			children.get(i).adjust(getWidth()*percentages[i],getHeight()*0.98f);
		}
	}

	@Override
	public float offsetY(int index){
		return 0;
	}

	@Override
	public float offsetX(int index){
		float sum = 0;
		for(int i=0;i<index;++i){
			sum+=percentages[i];
		}
		return sum*getWidth();
	}
	@Override
	public void onDraw(){
		//System.out.println("draw list");
	}

}
