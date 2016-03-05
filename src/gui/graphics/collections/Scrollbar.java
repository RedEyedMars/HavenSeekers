package gui.graphics.collections;

import gui.graphics.GraphicEntity;
import gui.inputs.MotionEvent;

public class Scrollbar extends GraphicEntity{

	private boolean horizontal = false;
	private Scrollbar self = this;
	private float percentage;
	
	public static final boolean HORIZONTAL = true;
	public static final boolean VERTICAL = false;
	
	public Scrollbar(boolean horizontal) {
		super("scrollbar");
		this.horizontal = horizontal;
		this.percentage = horizontal?getWidth():getHeight();
		addChild(new GraphicEntity("scrollbar"){
			{
				this.entity.setFrame(1);
			}
			public void adjust(float dx, float dy){
				super.adjust(self.horizontal?percentage*dx:dx,
				             self.horizontal?dy:percentage*dy);
			}
			@Override
			public void performOnClick(MotionEvent event){
				float delta = (self.horizontal?event.getX():event.getY())-(self.horizontal?getWidth():getHeight())/2f;
				if(delta+(self.horizontal?width:height)>=
				  (self.horizontal?self.getX()+self.getWidth():self.getY()+getHeight())){
					delta=(self.horizontal?self.getX()+self.getWidth():self.getY()+getHeight())
							-(self.horizontal?width:height);
				}
				else if(delta<=(self.horizontal?self.getX():self.getY())){
					delta = (self.horizontal?self.getX():self.getY());
				}
				if(event.getAction()!=MotionEvent.ACTION_MOVE){
					onScroll(delta);
				}
				if(self.horizontal){
					setX(delta);
				}
				else {
					setY(delta);
				}
			}
		});
	}

	public void onScroll(float y) {	
	}
	
	@Override
	public void setX(float dx){
		entity.setX(dx);
		float px = x;
		this.x = dx;
		getChild(0).performOnClick(new MotionEvent((dx-px)+getChild(0).getX(),getChild(0).getY(),MotionEvent.ACTION_MOVE));
	}
	@Override
	public void setY(float dy){
		entity.setY(dy);
		float py = y;
		this.y = dy;
		getChild(0).performOnClick(new MotionEvent(getChild(0).getX(),(dy-py)+getChild(0).getY(),MotionEvent.ACTION_MOVE));
	}
	@Override
	public void adjust(float dx, float dy){
		this.percentage = horizontal?getWidth():getHeight();
		if(horizontal){
			super.adjust(dx, 0.05f);
		}
		else super.adjust(0.05f, dy);
	}
	@Override
	public void performOnClick(MotionEvent event){
		getChild(0).performOnClick(event);
	}
}
