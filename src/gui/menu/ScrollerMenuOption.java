package gui.menu;

import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.graphics.InvertedGraphicElement;
import gui.inputs.MotionEvent;

public class ScrollerMenuOption extends MenuOption{

	private boolean horizontal;
	private int min;
	private int max;

	public ScrollerMenuOption(boolean isHorizontal,String modName, int min, int max) {
		super("blank", modName);
		this.horizontal = isHorizontal;
		entity.on(false);
		this.min = min;
		this.max = max;
		addChild(new GraphicEntity("scroller_option"){
			{
				if(!horizontal)this.entity = new InvertedGraphicElement("scroller_option",this);
				entity.setFrame(0);
			}
			@Override
			public boolean isWithin(float x, float y){
				return false;
			}
		});
		addChild(new GraphicEntity("scroller_option"){
			{
				if(!horizontal)this.entity = new InvertedGraphicElement("scroller_option",this);
				this.entity.setFrame(2);
			}
			@Override
			public boolean isWithin(float x, float y){
				return false;
			}
		});
		addChild(new GraphicEntity("scroller_option"){
			{
				if(!horizontal)this.entity = new InvertedGraphicElement("scroller_option",this);
				entity.setFrame(1);
			}
			@Override
			public boolean isWithin(float x, float y){
				return false;
			}
		});
	}
	@Override
	public void adjust(float dx, float dy){
		super.adjust(dx, dy);
		if(horizontal){
			dx/=2f;
			//getChild(1).setX(getX()+dx);
		}
		else {
			dy = dy/2f;
			getChild(1).setY(getY()+dy);
		}
		getChild(0).adjust(dx, dy);
		getChild(1).adjust(dx, dy);
		getChild(2).adjust(dx/4f, dy/1.24f);
	}
	@Override
	public float offsetX(int index){
		return index==1?(getWidth()/2f-0.01f):index==0?0.005f:getWidth()/2f;
	}
	@Override
	public float offsetY(int index){
		return index==2?getHeight()/3.24f:0f;
	}
	public void performOnClick(MotionEvent event){
		float percent = 0f;
		if(horizontal){
			percent = (event.getX()-getX())/(getWidth());
			getChild(2).setX(getX()+0.0125f+0.85f*percent*getWidth()-getChild(2).getWidth()/2f);
		}
		else {
			percent = ((event.getY()-getY())/(getHeight()));
			getChild(2).setY(getY()+percent*getHeight()-getChild(2).getHeight()/2f);
		}
		receive((int) (percent*max)+min);
	}

}
