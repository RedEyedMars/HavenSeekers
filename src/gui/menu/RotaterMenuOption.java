package gui.menu;

import gui.graphics.GraphicEntity;
import gui.inputs.MotionEvent;

public class RotaterMenuOption extends MenuOption{

	public RotaterMenuOption(String modName){
		super("blank",modName);
		this.entity.on(false);
		addChild(new GraphicEntity("menu_option_rotate"){
			{
				entity.setFrame(0);
			}
			@Override
			public void adjust(float dx, float dy){				
				if(dx>dy){
					super.adjust(dy, dy);
				}
				else {
					super.adjust(dx, dx);
				}
			}
			@Override
			public void performOnClick(MotionEvent event){
				float x = getX()+getWidth()/2f;
				float y = getY()+getHeight()/2f;
				x=event.getX()-x;
				y=event.getY()-y;
				float angle = (float) Math.atan2(y,x);
				if(angle<0){
					angle=(float) (2*Math.PI-Math.abs(angle));
				}
				receive(angle);
			}
		});
		addChild(new GraphicEntity("menu_option_rotate"){
			{
				entity.setFrame(1);
			}
			@Override
			public void adjust(float dx, float dy){				
				if(dx>dy){
					super.adjust(dy, dy);
				}
				else {
					super.adjust(dx, dx);
				}
			}
			@Override
			public boolean isWithin(float x, float y){ return false; }
		});
	}
	@Override
	public void receive(Object... data){
		getChild(1).rotate((float)data[0]);
		super.receive(data);
	}
	@Override
	public float offsetX(int index){
		return index==1?getChild(0).getWidth()/2f:0;
	}
	@Override
	public float offsetY(int index){
		return index==1?getChild(0).getHeight()/2f:0;
	}
}
