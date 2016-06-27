package environment.items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entity.Entity;
import entity.choice.ChoiceGenerator;
import entity.choice.ChoicePrototype;
import entity.choice.graphics.IconChoice;
import environment.Position;
import environment.Positionable;
import environment.ship.tile.Tile;
import environment.ship.tile.TravelPoint;
import gui.graphics.GraphicElement;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.graphics.collections.HorizontalGraphicList;
import gui.graphics.collections.TextGraphicElement;
import main.Hub;
import gui.menu.MenuItem;
import gui.menu.Modifiable;
import gui.menu.TabMenu;
import misc.condition.Condition;
import misc.condition.access.Propertiable;
import misc.wrappers.FloatWrapper;
import misc.wrappers.StorableFloatWrapper;
import parser.StringHeirachy;
import storage.Storable;
import storage.Storer;
import storage.StorerageIterator;

public class Area extends GraphicEntity implements Propertiable, Positionable, Storable{
	private List<Tile> tilesOn = new ArrayList<Tile>();
	protected Map<String,StorableFloatWrapper> values = new HashMap<String,StorableFloatWrapper>();
	private Position position = new Position(0,0);
	
	private AreaPrototype prototype;
	public Area(AreaPrototype prototype){
		super(prototype.getTextureName());
		this.prototype = prototype;
		values.put("vacantSpace", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return (float)vacant();
			}
		}));
		values.put("efficiency", FloatWrapper.s(1f));
	}
	public Area(){
		super("blank");
	}
	public void addSlot(Tile tile) {
		tilesOn.add(tile);
		tile.setOwner(this,tilesOn.size()==1);
	}
	public boolean hasVacant(){
		for(Tile slot:tilesOn){
			if(slot.getEntity()==null){
				return true;
			}
		}
		return false;
	}
	public int vacant(){
		int count = 0;
		for(Tile slot:tilesOn){
			if(slot.getEntity()==null){
				++count;
			}
		}
		return count;
	}
	public int getThoseWithRoles(String role){
		int count = 0;
		for(Tile slot:tilesOn){
			if(slot.getEntity().getRole().contains(role)){
				++count;
			}
		}
		return count;
	}

	public FloatWrapper getProperty(String property) {
		FloatWrapper i = values.get(property);
		if(i==null){
			return new FloatWrapper((float)getThoseWithRoles(property));
		}
		else return i;
	}
	public String getName() {
		return this.prototype.getName();
	}
	public IconChoice<Area> getIcon() {
		IconChoice<Area> choice = new IconChoice<Area>("area_icons",this);
		//choice.setFrame(getImageIndex());
		return choice;
	}
	public Position getPosition() {
		return position ;
	}

	public void setTravelPoint(TravelPoint point) {
		this.position = point;
	}
	@Override
	public float offsetX(int index) {
		return 0;
	}
	@Override
	public float offsetY(int index) {
		return 0;
	}
	public boolean walkInto(Entity e){
		e.setArea(this);
		return true;
	}

	@Override
	public void adjust(float x, float y){
		super.adjust(x, y,x*0.5f,y*0.25f);
	}
	public void setPosition(Position p) {
		this.position.setX(p.getX());
		this.position.setY(p.getY());
	}
	public boolean addProperty(String key, Float value) {
		if(values.containsKey(key))return false;
		values.put(key, FloatWrapper.s(value));
		return true;
	}
	public boolean hasProperty(String key) {
		return values.containsKey(key);
	}
	private Storer storer = new Storer<Area>(this,"A"){
		{
			strings = 1;
		}
		@Override
		protected Object[] storeCharacteristics() {
			return o(prototype.getName());
		}
		@Override
		protected void adjust(Object... args) {
			self.setName((String) args[0]);
		}
	};
	@Override
	public Storer getStorer() {
		return storer;
	}
	@Override
	public Iterable<Storer> getStorerIterator() {
		return new StorerageIterator(
				this,"values"){
			@Override
			protected Object getField(Field field, Object target) throws IllegalArgumentException, IllegalAccessException{
				return field.get(target);
			}
		};
	}
	public void setName(String name) {
		System.out.println("setName:"+name);
		if(Hub.areas.get(name)==null){
		for(String key:Hub.areas.keySet()){
			System.out.println(key);
		}
		}
		this.prototype = Hub.areas.get(name);
		this.entity = new GraphicElement(prototype.getTextureName(),this);
		this.isUpToDate = false;
		Hub.updateView = true;
		Hub.updateLayers = true;
		
	}
	public AreaPrototype getPrototype() {
		return prototype;
	}
}
