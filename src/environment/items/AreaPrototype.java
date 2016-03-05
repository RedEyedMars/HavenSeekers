package environment.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entity.Entity;
import entity.choice.ChoiceGenerator;
import entity.choice.ChoicePrototype;
import entity.choice.graphics.IconChoice;
import entity.choice.graphics.Iconable;
import environment.Position;
import environment.Positionable;
import environment.items.storage.AreaStorer;
import environment.ship.tile.Tile;
import environment.ship.tile.TravelPoint;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.graphics.collections.HorizontalGraphicList;
import gui.graphics.collections.TextGraphicElement;
import gui.menu.MenuItem;
import gui.menu.Modifiable;
import gui.menu.TabMenu;
import main.Hub;
import misc.condition.access.Propertiable;
import misc.wrappers.FloatWrapper;
import storage.Storable;
import storage.Storer;
import storage.StorerIteratorIterator;

public class AreaPrototype extends GraphicEntity implements Iconable, Propertiable, ChoiceGenerator, Modifiable, Storable{
	private static int uid = 0;
	private List<ChoicePrototype> choicePrototypes = new ArrayList<ChoicePrototype>();
	private int id = 0;
	private String name;
	private Map<String,FloatWrapper> values = new HashMap<String,FloatWrapper>();
	
	private AreaStorer storer;
	public AreaPrototype(String name){
		super("area_"+(name.contains("(")?name.replaceAll("\\(.*", ""):name).toLowerCase());
		this.name = name;
		if(name.contains("(")){
			int i=name.indexOf('(');
			this.name = name.substring(0,i);
			boolean end=false;
			++i;
			while(!end){
				int frontIndex = i;
				i=name.indexOf(',',i)+1;
				if(i==0){
					i=name.length()-1;
					end = true;
				}
				values.put(name.substring(frontIndex,i),new FloatWrapper(1f));
			}
		}
		values.put("efficiency", new FloatWrapper(1f));
		id = uid++;
	}

	public FloatWrapper getProperty(String property) {
		FloatWrapper i = values.get(property);
		return i;
	}
	public String getName() {
		return name;
	}
	public IconChoice<AreaPrototype> getIcon() {
		IconChoice<AreaPrototype> choice = new IconChoice<AreaPrototype>("area_icons",this);
		//choice.setFrame(getImageIndex());
		return choice;
	}

	@Override
	public void addChoicePrototype(ChoicePrototype c) {
		choicePrototypes.add(c);
	}
	@Override
	public ChoicePrototype getChoicePrototype(int i) {
		return choicePrototypes.get(i);
	}
	@Override
	public Iterator<ChoicePrototype> iterator() {
		return choicePrototypes.iterator();
	}
	@Override
	public float offsetX(int index) {
		return 0;
	}
	@Override
	public float offsetY(int index) {
		return 0;
	}
	@Override
	public int size() {
		return choicePrototypes.size();
	}

	@Override
	public void adjust(float x, float y){
		super.adjust(x, y,x*0.5f,y*0.25f);
	}
	public boolean addProperty(String key, Float value) {
		if(values.containsKey(key))return false;
		values.put(key, new FloatWrapper(value));
		return true;
	}
	public boolean hasProperty(String key) {
		return values.containsKey(key);
	}
	public Area create(){
		Area area = new Area(name,this);
		for(String key:values.keySet()){
			area.addProperty(key, values.get(key).get());
		}
		area.setId(id);
		return area;
	}
	@Override
	public AreaPrototype clone(){
		AreaPrototype area = new AreaPrototype(name);
		for(String key:values.keySet()){
			area.values.put(key, new FloatWrapper(values.get(key).get()));
		}
		area.choicePrototypes = choicePrototypes;
		area.id = id;
		return area;
	}
	@Override
	public int sizeChoicePrototype() {
		return choicePrototypes.size();
	}
	@Override
	public void modify(String operation, Object... arguments) {		
	}
	public static void makeMenuItems(TabMenu list, String catagory) {
		for(final String key:Hub.areas.keySet()){
			AreaPrototype area = Hub.areas.get(key).clone();
			GraphicView graphicView = new HorizontalGraphicList(){
				{
					addChild(new TextGraphicElement(key, TextGraphicElement.MEDIUM));
				}
				@Override
				public void adjust(float dx, float dy){
					super.adjust(dx, dy);
					if(dx>dy){
						children.get(1).adjust(dy, dy);
					}
					else {
						children.get(1).adjust(dx, dx);
					}
				}
			};
			graphicView.addChild(area);
			list.addChildTo(new AreaMenuItem(area,graphicView), catagory);
		}
	}
	private static class AreaMenuItem extends MenuItem{
		public AreaMenuItem(final Modifiable capturedTarget, GraphicView visualTarget) {
			super(capturedTarget, new float[]{1f}, visualTarget);
		}
		
	}
	@Override
	public Storer getStorer() {
		return storer;
	}
	@Override
	public Iterable<Storer> getStorerIterator() {
		return new StorerIteratorIterator(
				new List[]{},
				new Map[]{});
	}
}
