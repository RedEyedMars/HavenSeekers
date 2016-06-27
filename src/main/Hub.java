package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import time.Ticker;
import location.Location;
import location.LocationPrototype;
import location.LocationType;
import location.materials.ResourcePrototype;

import entity.trait.Trait;
import environment.items.Area;
import environment.items.AreaPrototype;
import environment.ship.Ship;
import gui.graphics.GraphicElement;
import gui.graphics.GraphicRenderer;
import gui.graphics.GraphicView;
import gui.inputs.KeyBoardListener;
import gui.inputs.MotionEvent;
import gui.inputs.MouseListener;
public class Hub {
	public static List<Ship> ships = new ArrayList<Ship>();
	public static Map<String,Trait> traits = new HashMap<String,Trait>();
	public static List<Trait> defaultTraits = new ArrayList<Trait>();
	public static Map<String,AreaPrototype> areas = new ConcurrentHashMap<String,AreaPrototype>();
	public static Map<String,LocationType> locationTypes = new ConcurrentHashMap<String,LocationType>();
	public static Map<String,ResourcePrototype> resourceTypes = new LinkedHashMap<String,ResourcePrototype>();
	public static Ticker ticker = new Ticker();
	public static GraphicView currentView;
	public static GraphicRenderer renderer;
	public static float width;
	public static float height;
	public static List<GraphicElement> drawLayer = new ArrayList<GraphicElement>();
	public static List<GraphicView> viewLayer = new ArrayList<GraphicView>();
	public static boolean updateLayers = false;
	public static boolean updateView = false;
	public static MouseListener genericMouseListener = new MouseListener(){
		@Override
		public boolean onClick(MotionEvent event) {
			return false;
		}
		@Override
		public boolean onHover(MotionEvent event) {
			return false;
		}
		@Override
		public void onMouseScroll(int distance) {			
		}};
	public static KeyBoardListener genericKeyBoardListener = new KeyBoardListener(){
		@Override
		public void input(char c, int keycode) {			
		}};
	public static Location parentBody;

}
