package gui.menu;

import gui.graphics.GraphicView;
import gui.graphics.collections.HorizontalGraphicList;
import gui.graphics.collections.VerticalGraphicList;
import gui.inputs.MotionEvent;

public class FlickerListMenu extends GraphicView {
		private int flick = 0;
		public FlickerListMenu(int length) {
			for(int i=0;i<length;++i){
				addChild(new VerticalGraphicList());
				getChild(i).setVisible(false);
			}
			if(length>0){
				getChild(0).setVisible(true);
			}
		}
		@Override
		public void setVisible(boolean vis){
			if(!vis){
				super.setVisible(false);
			}
			else {
				for(int i=0;i<children.size();++i){
					getChild(i).setVisible(false);
				}
				if(children.size()>0){
					getChild(flick).setVisible(true);
				}
			}
		}
		public void setFlick(int i){
			getChild(flick).setVisible(false);
			getChild(i).setVisible(true);
			flick = i;
		}
		public void addChildTo(MenuItem child, int index){
			getChild(index).addChild(child);
			if(index!=flick)child.setVisible(false);
		}
		public void addChildToFlick(MenuItem child){
			addChildTo(child,flick);
		}
		@Override
		public boolean onClick(MotionEvent event){
			return getChild(flick).onClick(event);
		}
	}