package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import misc.Rule;
import misc.Updatable;
import misc.condition.access.Propertiable;
import misc.wrappers.FloatWrapper;

import entity.choice.Choice;
import entity.decision.Decision;
import entity.info.Info;
import entity.info.ProgressBar;
import entity.info.TextInfo;
import entity.state.State;
import entity.trait.Trait;
import environment.Direction;
import environment.Position;
import environment.Positionable;
import environment.items.Area;
import environment.ship.Ship;
import environment.ship.tile.Tile;
import gui.Gui;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.graphics.collections.HorizontalGraphicList;
import gui.inputs.MotionEvent;
import main.Log;

public class Entity extends GraphicEntity implements Propertiable, Updatable, Positionable {
	private String name;
	private List<Trait> traits = new ArrayList<Trait>();
	private Map<String, FloatWrapper> values = new HashMap<String, FloatWrapper>();
	@SuppressWarnings("rawtypes")
	public Map<String,List<Rule>> ruleClusters = new HashMap<String,List<Rule>>();
	private Ship ship;
	private String role = "";
	private State state = State.idle;
	private String animation = "idle";
	private Stack<Decision> decisions = new Stack<Decision>();
	private Position position = new Position(0,0);
	private Direction direction = Direction.right;
	private Area area;
	private boolean currentlyActing = false;

	private HorizontalGraphicList info = new HorizontalGraphicList(){
		@Override
		public void addChild(GraphicView e){
			super.addChild(e);
			e.setVisible(info.isVisible());
		}
		
		@Override
		public boolean onHover(MotionEvent event){
			if(isWithin(event.getX(),event.getY())){
				super.onHover(event);
				return true;
			}
			else {
				this.setVisible(false);
				Gui.removeOnClick(this);
				return false;
			}
		}
	};
	private HashMap<String,Info> infoMap = new HashMap<String,Info>();
	public Entity(String name, Ship ship){
		super("belma_entity");
		this.name = name;
		this.ship = ship;
		values.put("wounds", new FloatWrapper(0f){
			@Override
			public Float get(){
				return (float)numberOfWoundedTraits();
			}

		});
		info.setVisible(false);
		addInfo("name",new TextInfo(name));

		ship.getInsideView().addUpdater(this);
	}
	private Integer numberOfWoundedTraits() {
		int count = 0;
		for(Trait trait:traits){
			if(trait.ancestoryContains("Wounded")){
				++count;
			}
		}
		return count;
	}
	public void addTrait(Trait trait) {
		fireRule("onGain",this,trait);
		traits.add(trait);
	}
	public void removeTrait(Trait trait) {
		fireRule("onLose",this,trait);
		traits.remove(trait);
	}
	public boolean hasTrait(Trait trait) {
		for(Trait myTrait:traits){
			if(trait.getName().equals(myTrait.getName())){
				return true;
			}
		}
		return false;
	}
	public void forceTrait(Trait trait) {
		if(trait.canAdd(this)){
			addTrait(trait);
		}
		else {
			trait.force(this);
			addTrait(trait);
		}
	}
	public void addRule(String key, Rule rule){
		if(!ruleClusters.containsKey(key)){
			ruleClusters.put(key, new ArrayList<Rule>());
		}
		ruleClusters.get(key).add(rule);
	}
	public void removeRule(String key, Rule rule) {
		if(ruleClusters.containsKey(key)){
			ruleClusters.get(key).remove(rule);
		}
	}
	private void fireRule(String key, Object onAction, Object onCondition){
		Log.d("entity","entity.fireRule",key+":"+onAction);
		if(ruleClusters.containsKey(key)){
			List<Rule> cluster = ruleClusters.get(key);
			for(int i=0;i<cluster.size();++i){
				cluster.get(i).rate(onCondition,onAction);
			}
		}
	}
	public Ship getShip() {
		return ship;
	}
	public String getRole() {
		return role ;
	}
	public void addRole(String role) {
		this.role = this.role+role;
	}
	public void removeRole(String role) {
		this.role = this.role.replace(role, "");
	}
	@Override
	public FloatWrapper getProperty(String property) {
		return values.get(property);
	}
	public boolean addProperty(String property, Float value) {
		if(values.containsKey(property)){
			return false;
		}
		values.put(property, new FloatWrapper(value));
		return true;
	}
	public void needsNewChoice() {

		currentlyActing = false;
	}
	public void setCurrentState(State state, String name,
			String desc, Integer low, Integer start, Integer end, Integer upper) {
		this.state  = state;
		this.setAnimation(name);
		//this.addInfo("description",new TextInfo(desc.replace("NAME", this.getName())));
		//System.out.println(low+"["+start+","+end+"]"+upper);
		this.addInfo("progressBar",new ProgressBar(low,upper,start,end));
	}
	private void addInfo(String name, Info info) {
		if(!infoMap.containsKey(name)){
			infoMap.put(name, info);
			this.info.addChild(info.getDisplay());
			addChild(info.getDisplay());
		}
		else {
			infoMap.get(name).update(info);
		}
	}
	public String getName() {
		return name;
	}
	private void setAnimation(String animation) {
		this.animation  = animation;
	}
	public String filterText(String prompt) {
		return prompt.replace("NAME", getName());
	}
	public void setNextDecision(Decision moveToDecision) {
		decisions.push(moveToDecision);
	}
	public void update(){
		if(currentlyActing){

		}
		else if(decisions.isEmpty() ){
			getShip().fillWithChoices(this);
		}
		else {
			if(decisions.peek().step(this)){
				decisions.pop();
			}
		}
	}
	public float getDistanceToArea(Area area) {
		return getPosition().distanceTo(area.getPosition());
	}
	public Position getPosition() {
		return position ;
	}
	public void moveToward(Area area) {
		Position newPos = getPosition().getDirectionTo(area.getPosition()).move(getPosition());
		ship.getTile(newPos.getX(),newPos.getY()).setEntity(this);;
	}
	public Area getArea() {
		return area;
	}
	public List<Trait> getTraits() {
		return traits;
	}
	public int getPreferenceFor(Choice choice) {
		fireRule("onNewChoice",choice,choice);
		return (int) (choice.with("prevent")?-1000:choice.get());
	}
	int tick = 0;
	@Override
	public void animate() {
		if(tick%10==0){
		this.entity.setFrame(this.entity.textureIndex()+1);
		if(this.entity.textureIndex()==3){
			this.entity.setFrame(0);
		}
		}
		tick++;
	}
	@Override
	public void onDraw() {

	}
	public void setArea(Area area) {
		this.area = area;
	}
	public void setActing(boolean b) {
		currentlyActing = b;
	}
	@Override
	public void onAddToDrawable(){
		super.onAddToDrawable();
		addChild(info);
	}
	@Override
	public void onRemoveFromDrawable(){
		super.onRemoveFromDrawable();
		removeChild(info);
	}
	@Override
	public boolean onHover(MotionEvent event) {
		if(isWithin(event.getX(),event.getY())){
			info.setVisible(true);
			info.onHover(event);
			super.onHover(event);
			Gui.giveOnClick(info);
			return true;
		}
		else  {
			info.setVisible(false);
		}
		return false;
	}
	public void adjust(float x, float y){
		super.adjust(x, y);
		if(info!=null){
			this.info.adjust(x*7.5f, y*3.33f);
		}
	}
	public void setX(float x){
		super.setX(x);
		if(info!=null){
			this.info.setX(x-this.info.getWidth()/2f+this.getWidth()/2f);
		}
	}
	public void setY(float y){
		super.setY(y);
		if(info!=null){
			this.info.setY(y);
		}
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction directionTo) {
		this.direction = directionTo;
	}
	public void placeSelf(Tile tile) {
		tile.setEntity(this);
	}
	public void setPosition(Position position) {
		this.position = position;
	}
}
