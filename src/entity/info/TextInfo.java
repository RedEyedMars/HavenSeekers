package entity.info;

import gui.graphics.collections.TextGraphicElement;

public class TextInfo extends Info {

	private String text;

	public TextInfo(String text) {
		super(new TextGraphicElement(text,TextGraphicElement.MEDIUM));
		this.text = text;
		
	}

	@Override
	public void update(Info info) {
		if(info==null){
			display.setVisible(false);
		}
		else if(info instanceof TextInfo) {
			((TextGraphicElement)display).update(((TextInfo)info).text);		
		}
	}

}
