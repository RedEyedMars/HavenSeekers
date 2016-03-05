package gui.menu;

import gui.Gui;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.graphics.collections.VerticalGraphicArray;
import gui.inputs.MotionEvent;
import misc.condition.Condition;

public class MenuItem extends VerticalGraphicArray{

	private boolean isSelected = false;
	private Modifiable target;
	public MenuItem(Modifiable capturedTarget,float[] percentages, GraphicView visualTarget, MenuOption... options) {
		super(percentages,combineForConstructor(visualTarget,options));
		for(MenuOption option:options){
			option.setTarget(capturedTarget);
		}
		this.target = capturedTarget;
	}

	private static GraphicView[] combineForConstructor(GraphicView visualTarget, MenuOption[] options) {
		GraphicView[] ret = new GraphicView[options.length+1];
		for(int i=0;i<options.length;++i){
			ret[i]=options[i];
		}
		ret[options.length] = visualTarget;
		return ret;
	}
	
	@Override
	public boolean onClick(MotionEvent event){
		if(isSelected&&event.getAction()==MotionEvent.ACTION_UP){
			if(((DropObjectListener)getParentWithCondition(DropObjectListener.conditions.getInstanceCondition()))
				.drop(event.getX(), event.getY(), target.clone())){
				isSelected = false;
				Gui.removeOnClick(this);
			}
			return true;
		}
		else if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
			if(children.get(children.size()-1).isWithin(event.getX(), event.getY())){
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					if(!isSelected){
						isSelected = true;
						Gui.giveOnClick(this);
					}
				}
				return true;
			}
			boolean childrenHaveWithin = false;
			for(int i=0;i<children.size()-1;++i){
				if(children.get(i).onClick(event)){
					childrenHaveWithin = true;
				}
			}
			if(!childrenHaveWithin){
				performOnClick(event);
			}
			return true;
		}
		else return false;
	}

	protected boolean superOnClick(MotionEvent event){
		return super.onClick(event);
	}
	
}
