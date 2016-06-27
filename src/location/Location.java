package location;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.choice.ChoicePrototype;
import location.materials.Resource;
import location.materials.ResourcePrototype;
import main.Hub;
import main.Log;
import misc.condition.Condition;
import misc.condition.access.Propertiable;
import misc.wrappers.FloatWrapper;
import misc.wrappers.StorableFloatWrapper;
import parser.StringHeirachy;
import storage.Storable;
import storage.Storer;
import storage.StorerageIterator;

public class Location implements Propertiable,Storable{
	private static final float distanceToTemperatureConversionRate = 1000;
	
	
	private Map<String,StorableFloatWrapper> values = new HashMap<String,StorableFloatWrapper>();
	private List<Location> satellites = new ArrayList<Location>();
	private List<Resource> resources = new ArrayList<Resource>();
	private Float mass;
	private int density = 10;
	private int distance = 0;
	private Float temperature = 100f;
	private LocationType myClass;
	private Location parentBody;
	public Location(){
		values.put("interest", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return interest();
			}
		}));
		values.put("mineable_resource", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return getMineableResource();
			}

		}));
		values.put("mineable_resource_id", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return getMineableResourceId();
			}
		}));
		values.put("mass", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return getMass();
			}
		}));
		values.put("moons", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return (float) satellites.size();
			}
		}));
		values.put("density", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return getDensity();
			}
		}));
		values.put("temperature", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				if(parentBody!=null){
					
				}
				return getTemperature();
			}
		}));
		values.put("distance", new StorableFloatWrapper(new FloatWrapper(0f){
			@Override
			public Float get(){
				return getDistance();
			}
		}));
	}
	public Location(LocationType type){
		this();
		this.myClass = type;
		List<String> names = new ArrayList<String>();
		for(String name:Hub.resourceTypes.keySet()){
			names.add(name);
		}
		for(int i=0;i<20&&!names.isEmpty();++i){			
			Collections.shuffle(names);
			this.addResource(names.remove(0));
		}
	}
	public Float getMineableResource() {
		return getProperty(ResourcePrototype.translateResourceId(getMineableResourceId())).get();
	}

	public Float getMineableResourceId(){
		for(int i=0;i<resources.size();++i){
			if(resources.get(i).is("mineable")){
				return (float) ResourcePrototype.translateResourceName(resources.get(i).getFullName());
			}
		}
		return 0f;
	}
	public Float interest() {
		return 0f;
	}
	public FloatWrapper getProperty(String property) {
		return values.containsKey(property)?values.get(property):new FloatWrapper(0f);
	}
	@Override
	public boolean addProperty(String property, Float value) {
		if(values.containsKey(property)){
			return false;
		}
		values.put(property, FloatWrapper.s(value));
		return true;
	}
	public void removeResource(String resourceName,int by){
		if(values.containsKey(resourceName)){
			if(values.get(resourceName).get()>=by){
				values.get(resourceName).set(values.get(resourceName).get()-by);
			}
			else {
				values.get(resourceName).set(0f);
			}
			for(int r=0;r<by;++r){
				for(int i=0;i<resources.size();++i){
					if(resourceName.equals(resources.get(i).getFullName())){
						Log.d("location","location.removeResource","remove:"+resources.get(i).getFullName());
						resources.remove(i);
						break;
					}
				}
			}
		}
	}
	/*
	 * adds a random resource
	 */
	public void addResource(String name){
		Resource r = Hub.resourceTypes.get(name).create();
		if(!values.containsKey(r.getFullName())){
			values.put(r.getFullName(), FloatWrapper.s(0f));
		}
		values.get(r.getFullName()).set(values.get(r.getFullName()).get()+1);
		resources.add(r);
	}
	public void addResource(Resource r,Integer by){
		if(!values.containsKey(r.getFullName())){
			values.put(r.getFullName(), FloatWrapper.s(0f));
		}
		values.get(r.getFullName()).set(values.get(r.getFullName()).get()+by);
		for(int i=0;i<by;++i){
			resources.add(r);
		}
	}
	public Float getMass() {
		return mass;
	}
	public Float getTemperature(){
		if(parentBody!=null){
			return temperature+parentBody.getTemperature()*distanceToTemperatureConversionRate/getDistance();
		}
		else return temperature;
	}
	public Float getDensity(){
		return (float)density;
	}
	public Float getDistance(){
		return (float)distance;
	}
	public void setMass(Float f) {
		mass = f;
	}
	public void setDensity(int i) {
		density = i;
	}
	public void setTemperature(Float temp) {
		this.temperature  = temp;
	}
	public void addSatellite(Location satellite){
		this.satellites.add(satellite);
		satellite.setParentBody(this);
	}
	private void setParentBody(Location parent) {
		this.parentBody=parent;
		distance = (int) (parentBody.getMass()*2+parentBody.getMass()*(Math.random()*10f+1));
		
	}
	public Location generateSatellite() {
		if(myClass.getSatelliteType()!=null){
			return myClass.getSatelliteType().create();
		}
		else return null;
	}


	private Storer<Location> storer = new Storer<Location>(this,"L"){
		@Override
		protected Object[] storeCharacteristics() {
			return o();
		}

		@Override
		protected void adjust(Object... args) {
		}

		
	};
	@Override
	public Storer getStorer() {
		return storer;
	}

	public Iterable<Storer> getStorerIterator() {
		return new StorerageIterator(
				this,"values"){
			@Override
			protected Object getField(Field field, Object target) throws IllegalArgumentException, IllegalAccessException{
				return field.get(target);
			}
		};
	}
}
