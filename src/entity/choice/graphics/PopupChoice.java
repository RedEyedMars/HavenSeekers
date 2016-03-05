package entity.choice.graphics;

import java.util.ArrayList;
import java.util.List;

import entity.trait.Trait;
import gui.graphics.collections.HorizontalGraphicList;
import gui.graphics.collections.TextGraphicElement;
import gui.graphics.collections.VerticalGraphicList;
import gui.inputs.MotionEvent;
import gui.inputs.MouseListener;


public class PopupChoice <ReturnType> extends VerticalGraphicList implements Runnable{

	private IconChoice<ReturnType>[] choices;
	private String text;
	private IconChoice<ReturnType> selected = null;
	private int index = -1;

	@SuppressWarnings("unchecked")
	public PopupChoice(String text, IconChoice<ReturnType>... icons) {
		super(new TextGraphicElement(text,TextGraphicElement.MEDIUM),new HorizontalGraphicList(icons));
		this.text = text;
		this.choices = icons;
	}


	@Override
	public void run() {
		while(selected == null){
			for(int i=0;i<choices.length;++i){
				if(choices[i].isSelected()){
					selected = choices[i];
					index = i;
					break;
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ReturnType getSelected() {
		return selected==null?null:selected.getObject() ;
	}


	public void reset() {
		selected = null;
	}


	public int getIndex() {
		return index ;
	}
	public static PopupChoice createPopup(String string, Iterable<? extends Iconable> icons) {
		List<Iconable> is = new ArrayList<Iconable>();
		for(Iconable i:icons){
			is.add(i);
		}
		return new PopupChoice(string,IconChoice.getIcons(is));
	}

	public String getText(){
		return text;
	}

}
