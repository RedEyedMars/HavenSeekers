package gui.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.graphics.collections.GraphicGrid;
import gui.graphics.collections.HorizontalGraphicList;
import gui.graphics.collections.TextGraphicElement;
import gui.graphics.collections.VerticalGraphicList;

public class TabMenu extends VerticalGraphicList{
	private List<TabChoice> tabs = new ArrayList<TabChoice>();
	private int currentSelected = 0;
	private FlickerListMenu menu;
	private HashMap<String,Integer> modeMap = new HashMap<String,Integer>();
	public TabMenu(String... modes){
		super(new GraphicGrid((int) (Math.log(modes.length)/Math.log(2))+(modes.length==2?1:0),(int) (Math.log(modes.length)/Math.log(2))),
				new FlickerListMenu(modes.length));
		GraphicView view = getChild(0);
		for(GraphicView choice:
			getChoiceGraphics(modes)){
			view.addChild(choice);
		}
		for(int i=0;i<view.getChildren().size();++i){
			tabs.add((TabChoice)view.getChild(i));
			tabs.get(i).setParent(this);
			modeMap.put(modes[i], i);
		}
		if(tabs.size()>0){
			tabs.get(0).setSelected(true);
			this.menu = (FlickerListMenu) getChild(1);
			entity.setVisible(false);
		}

	}

	private static TabChoice[] getChoiceGraphics(String... modes){
		TabChoice[] choices = new TabChoice[modes.length];
		int index = 0;
		int size = (int) (Math.log(modes.length)/Math.log(2));
		for(int i=0;i<size;++i){
			for(int j=0;j<size+(size==1?1:0);++j){
				choices[index] = new TabChoice(modes[index],index,j,i);
				++index;
			}
		}
		return choices;
	}

	public void select(int index) {
		tabs.get(currentSelected).setSelected(false);
		tabs.get(index).setSelected(true);
		menu.setFlick(index);
		currentSelected = index;
	}

	@Override
	public void adjust(float dx, float dy){
		super.adjust(dx, dy);
		getChild(0).adjust(dx, dy/8f);
		if(size()>1)getChild(1).adjust(dx, dy*7f/8f);
	}

	@Override
	public void setY(float dy){
		super.setY(dy);
		getChild(0).setY(dy+getHeight()*7f/8f);
		if(size()>1)getChild(1).setY(dy);
	}

	public void addChildTo(MenuItem child, String mode){
		if(modeMap.containsKey(mode)){
			menu.addChildTo(child,modeMap.get(mode));
		}
	}

}
