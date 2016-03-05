package gui.graphics.collections;

import main.Hub;
import misc.Updatable;
import gui.graphics.GraphicElement;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.inputs.MotionEvent;

public class TextGraphicElement extends GraphicView implements Updatable {
	
	private float x;
	private float y;
	protected String text;
	private String previous;
	private boolean update = false;
	private float fontSize;
	
	public static final float BIG = 2f;
	public static final float MEDIUM = 1.5f;
	public static final float SMALL = 1f;
	public static final float TINY = 0.5f;

	public TextGraphicElement(String text, float  fontSize) {
		super();
		this.text = text;
		this.fontSize = fontSize;
		makeNewSquare();
	}
	public void update(String text) {
		this.text = text;
		this.update = true;
	}

	private void makeNewSquare(){
		for(String line:text.split("\\n")){
			Hub.renderer.loadText(line);
			GraphicEntity entity = new GraphicEntity("$"+line);
			entity.adjust(0.64f*fontSize, 0.05f*fontSize);
			addChild(entity);
		}
		previous = text;
	}

	public String getText() {
		return text;
	}
	@Override
	public void update(){
		if(update){
			String p = previous;
			makeNewSquare();
			if(p!=null){
				int i=0;
				for(String line:p.split("\\n")){
					removeChild(getChild(i++));
				}
			}
			update = false;
		}
	}
	
	@Override
	public void adjust(float dx, float dy){
		this.width = dx;
		this.height = dy;
	}
	@Override
	public float offsetX(int i){
		return this.width/2f-0.06f;
	}
	@Override
	public float offsetY(int i){
		return -i*0.05f;
	}

	@Override
	public boolean onClick(MotionEvent event){
		return false;
	}
}
