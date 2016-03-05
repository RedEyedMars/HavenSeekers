package gui.graphics.collections;

import gui.graphics.GraphicView;
import main.Hub;

public class ScrollableGraphicView extends GraphicView {

	private ScrollableGraphicView self = this;
	protected float trueWidth;
	protected float trueHeight;
	public ScrollableGraphicView(){
		this(1f,1f);
	}

	public ScrollableGraphicView(float trueWidth, float trueHeight) {
		super();
		this.trueWidth = trueWidth;
		this.trueHeight = trueHeight;
		addChild(new Scrollbar(true){
			@Override
			public void onScroll(float x){
				self.setX(x);
			}
		});
		addChild(new Scrollbar(false){
			@Override
			public void onScroll(float y){
				self.setY(y);
			}
		});
	}

	@Override
	public void adjust(float dx, float dy){
		this.width = dx;
		this.height = dy;
		if(size()>1){
			if(dx<0.98f){
				getChild(size()-2).setVisible(true);
				getChild(size()-2).adjust(dx, dy);
			}
			else {
				getChild(size()-2).setVisible(false);
			}
			if(dy<0.98f){
				getChild(size()-1).setVisible(true);
				getChild(size()-1).adjust(dx, dy);
			}
			else {
				getChild(size()-1).setVisible(false);
			}
		}
		resetSeeable();
	}
	@Override
	public void adjust(float dx, float dy, float subX, float subY){
		this.width = dx;
		this.height = dy;
		if(size()>1){
			if(dx<trueWidth){
				getChild(size()-2).setVisible(true);
				getChild(size()-2).adjust(dx, dy);
			}
			else {
				getChild(size()-2).setVisible(false);
			}
			if(dy<trueHeight-0.02f){
				getChild(size()-1).setVisible(true);
				getChild(size()-1).adjust(dx, dy);
			}
			else {
				getChild(size()-1).setVisible(false);
			}
		}
		resetSeeable();
		for(int i=0;i<children.size()-2;++i){
			children.get(i).adjust(subX, subY);
		}
	}
	@Override
	public void setX(float dx){
		this.x = dx;
		resetSeeable();
	}
	@Override
	public void setY(float dy){
		this.y = dy;
		resetSeeable();
	}

	@Override
	public void addChild(GraphicView child){
		children.add(children.size()>1?(children.size()-2):children.size(),child);
		child.setView(this);
		this.isUpToDate = false;
		Hub.updateView = true;
		Hub.updateLayers = true;
		child.onAddToDrawable();
	}

	private void resetSeeable() {
		for(int i=0;i<children.size()-2;++i){
			float dx = offsetX(i)*trueWidth+children.get(i).getWidth()/2f;
			float dy = offsetY(i)*trueHeight+children.get(i).getHeight()/2f;
			if(dx>0&&dx<trueWidth&&
					dy>0&&dy<this.getHeight()){
				children.get(i).turnOn();
				children.get(i).setX(getX()+(offsetX(i)*trueWidth));
				children.get(i).setY(getY()+(offsetY(i)*trueHeight));
			}
			else {
				children.get(i).turnOff();
			}
		}
	}
}
