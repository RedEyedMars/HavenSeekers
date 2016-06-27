package gui.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import gui.inputs.MotionEvent;
import gui.inputs.MouseListener;
import main.Hub;
import misc.Updatable;
import misc.condition.Condition;

public class GraphicView implements Graphicable, MouseListener{
	private List<Updatable> updaters = new ArrayList<Updatable>();
	@SuppressWarnings("unused")
	private boolean temporary;
	protected List<GraphicView> children = new ArrayList<GraphicView>();
	protected float x=0f;
	protected float y=0f;
	protected float height=1f;
	protected float width=1f;
	protected GraphicView parent = null;
	protected boolean listenToRelease = false;
	protected boolean isUpToDate = true;
	public GraphicView(){
		this(false);
	}
	public GraphicView(boolean temporary){
		this.temporary = temporary;
	}
	public void addUpdater(Updatable updatable) {
		updaters.add(updatable);
	}
	public void removeUpdater(Updatable updatable) {
		updaters.remove(updatable);
	}

	public void update() {
		for(int i=0;i<updaters.size();++i){
			updaters.get(i).update();
		}
		for(int i=0;i<children.size();++i){
			(children.get(i)).update();
		}
	}

	@Override
	public void draw() {

	}

	public void setupLayers(){
		Hub.drawLayer.clear();
		Hub.viewLayer.clear();
		for(Iterator<GraphicView> itr = new LayerIterator();itr.hasNext();){
			GraphicView view = itr.next();
			if(view!=null){
				GraphicElement toShow = view.getGraphicElement();
				if(toShow!=null){
					Hub.drawLayer.add(toShow);
				}
				Hub.viewLayer.add(view);
			}
		}
	}

	public GraphicElement getGraphicElement(){
		return null;
	}


	private class LayerIterator implements Iterator<GraphicView>{
		private Iterator<GraphicView> itr;
		private int index = 0; 
		private LinkedList<List<GraphicView>> backup = new LinkedList<List<GraphicView>>();
		{
			itr = children.iterator();
			backup.add(children);
		}
		@Override
		public boolean hasNext() {
			return !backup.isEmpty();
			//return backup.size()>1||(!backup.isEmpty()&&backup.getFirst().hasNext());
		}

		@Override
		public GraphicView next() {
			if(itr.hasNext()){
				return itr.next();
			}
			else {
				if(index<backup.getFirst().size()) {
					GraphicView next = backup.getFirst().get(index);
					itr = next.children.iterator();
					backup.add(next.children);
					++index;
					return next();
				}
				else {
					backup.pollFirst();
					if(!backup.isEmpty()){
						itr = backup.getFirst().iterator();
						index = 0;
						return next();
					}
					return null;
				}
			}
		}

	}

	@Override
	public int textureIndex() {
		return -1;
	}

	@Override
	public String getTextureName() {
		return null;
	}

	@Override
	public void setVisible(boolean b) {
		for(int i=0;i<children.size();++i){
			children.get(i).setVisible(b);
		}
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void onAddToDrawable() {
	}

	@Override
	public void onRemoveFromDrawable() {
	}

	@Override
	public void animate() {
		for(int i=0;i<children.size();++i){
			children.get(i).onRemoveFromDrawable();
		}
	}

	public float getX(){
		return x;
	}


	@Override
	public void setX(float x) {
		this.x = x;
		for(int i=0;i<children.size();++i){
			children.get(i).setX(x+offsetX(i));
		}
	}
	public float offsetX(int i){
		return 0;
	}
	public float getY(){
		return y;
	}

	@Override
	public void setY(float y) {
		this.y = y;
		for(int i=0;i<children.size();++i){
			children.get(i).setY(y+offsetY(i));
		}
	}

	public float offsetY(int i){
		return 0;
	}

	@Override
	public void adjust(float width, float height) {
		this.height = height;
		this.width = width;
		for(int i=0;i<children.size();++i){
			children.get(i).adjust(width,height);
		}
	}

	public void adjust(float width, float height, float dWidth, float dHeight){
		this.height = height;
		this.width = width;
		for(int i=0;i<children.size();++i){
			children.get(i).adjust(dWidth,dHeight);
		}
		this.isUpToDate = true;
	}

	@Override
	public void onDraw() {

	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}
	public void updateView(){
		adjust(getWidth(),getHeight());
		setX(getX());
		setY(getY());
	}
	public void addChild(GraphicView e){
		children.add(e);
		e.setView(this);
		this.isUpToDate = false;
		Hub.updateView = true;
		Hub.updateLayers = true;
		e.onAddToDrawable();
	}
	public void removeChild(int e){
		children.get(e).setView(null);
		this.isUpToDate = false;
		Hub.updateView = true;
		Hub.updateLayers = true;
		children.remove(e).onRemoveFromDrawable();;
	}
	public void removeChild(GraphicView e){
		e.setView(null);
		children.remove(e);
		this.isUpToDate = false;
		Hub.updateView = true;
		Hub.updateLayers = true;
		e.onRemoveFromDrawable();
	}


	@Override
	public boolean onClick(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
			boolean childrenHaveWithin = false;
			for(int i=0;i<children.size();++i){
				if(children.get(i).onClick(event)){
					childrenHaveWithin = true;
				}
			}
			if(!childrenHaveWithin){
				performOnClick(event);
			}
			return true;
		}
		else {
			if(listenToRelease&&event.getAction()==MotionEvent.ACTION_UP ){
				for(int i=0;i<children.size();++i){
					children.get(i).onClick(event);
				}
				performOnRelease(event);
			}
			return false;
		}
	}

	@Override
	public boolean onHover(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
			boolean childrenHaveWithin = false;
			for(int i=0;i<children.size();++i){
				if(children.get(i).onHover(event)){
					childrenHaveWithin = true;
				}
			}
			if(!childrenHaveWithin){
				performOnHover(event);
			}
			return true;
		}
		else return false;
	}

	public void onMouseScroll(int distance){
		for(int i=0;i<children.size();++i){
			children.get(i).onMouseScroll(distance);
		}
	}
	
	public void performOnClick(MotionEvent event){		
	}
	public void performOnRelease(MotionEvent event){		
	}

	public void performOnHover(MotionEvent event){		
	}


	public boolean isWithin(float dx, float dy) {
		return dx>x&&dx<x+getWidth()&&
				dy>y&&dy<y+getHeight();
	}
	public int size() {
		return children.size();
	}
	public GraphicView getChild(int i) {
		return children.get(i);
	}
	public List<GraphicView> getChildren() {
		return children;
	}
	public GraphicView getView() {
		return parent;
	}
	public void setView(GraphicView graphicView) {
		this.parent = graphicView;
	}
	public void turnOff() {
		for(int i=0;i<children.size();++i){
			children.get(i).turnOff();
		}		
	}
	public void turnOn(){
		for(int i=0;i<children.size();++i){
			children.get(i).turnOn();
		}
	}
	public void listenToRelease(boolean b) {
		if(parent!=null){
			parent.listenToRelease(b);
		}
		listenToRelease = b;

	}
	public boolean isUpToDate() {
		return isUpToDate;
	}
	public void setFrame(int i) {

	}
	public void rotate(float f) {
	}

	public GraphicView getParentWithCondition(Condition<GraphicView> condition) {
		GraphicView curParent = parent;
		while(curParent!=null&&!condition.satisfied(curParent)){
			curParent = curParent.getView();
		}
		return curParent;
	}
}
