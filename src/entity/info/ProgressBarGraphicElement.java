package entity.info;

import gui.graphics.GraphicEntity;

public class ProgressBarGraphicElement extends GraphicEntity{
	protected float parentalWidth;
	protected float parentalHeight;	
	protected float mlow;
	protected float mstart;
	protected float mend;
	protected float mupper;
	public ProgressBarGraphicElement(float low, float start, float end, float upper){
		super("info_progress_bar");
		parentalHeight=getHeight();
		parentalWidth=getWidth();
		mlow = low;
		mstart = start;
		mend = end;
		mupper = upper;
		addChild(new GraphicEntity("info_progress_bar"){
			{
				entity.setFrame(1);
			}
			@Override
			public void animate(){
				adjust(parentalWidth*(mend-mstart)/(mupper-mlow),parentalHeight);
			}
		});
	}
	@Override
	public void adjust(float x, float y){
		parentalWidth = x;
		parentalHeight = y;
		super.adjust(x, y);
	}
	@Override
	public float offsetX(int index) {
		return getWidth()*(mstart-mlow)/(mupper-mlow);
	}
	@Override
	public float offsetY(int index) {
		return 0;
	}
	
	public void update(float low,float start,float end,float upper){
		mlow = low;
		mstart = start;
		mend = end;
		mupper = upper;
		setX(getX());
	}
	public float getLow() {
		return mlow;
	}
	public float getStart(){
		return mstart;
	}
	public float getEnd(){
		return mend;
	}
	public float getUpper(){
		return mupper;
	}

}
