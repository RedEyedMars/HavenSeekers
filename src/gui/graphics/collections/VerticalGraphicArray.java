package gui.graphics.collections;

import main.Hub;
import gui.graphics.GraphicElement;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;

public class VerticalGraphicArray extends GraphicView{

	private float[] percentages;
	public VerticalGraphicArray(float[] percentages,GraphicView... elements) {
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
			children.get(i).adjust(getWidth(),getHeight()*percentages[i]);
		}
	}

	@Override
	public float offsetY(int index){
		float sum = 0;
		for(int i=0;i<index;++i){
			sum+=percentages[i];
		}
		return (sum)*getHeight();
	}

	@Override
	public float offsetX(int index){
		return 0;
	}
}
