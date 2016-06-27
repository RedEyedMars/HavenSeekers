package main;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import environment.ship.Ship;
import environment.ship.menu.ShipConstructorMenu;
import environment.ship.tile.Tile;
import environment.ship.tile.TravelPath;
import environment.ship.tile.TravelPoint;
import location.materials.ResourcePrototype;
import parser.StringHeirachy;
import storage.Storable;
import storage.Storage;
import storage.Storer;

public class TestSuite {
	@Retention(RetentionPolicy.RUNTIME)
	@interface iLocation{}
	@Retention(RetentionPolicy.RUNTIME)
	@interface iTile{}
	@Retention(RetentionPolicy.RUNTIME)
	@interface iStorage{}
	@Retention(RetentionPolicy.RUNTIME)
	@interface iCurrent{}
	@Retention(RetentionPolicy.RUNTIME)
	@interface Ignore{}
	public static final String storage = "Storage";
	public static final String location = "Location";
	public static final String tile = "Tile";
	public static final String current = "Current";
	public static void test(String... sections){
		TestSuite suite = new TestSuite();
		List<String> includeCats;
		if(sections!=null){
			includeCats = new ArrayList<String>(Arrays.asList(sections));
		}
		else {
			includeCats = new ArrayList<String>();
		}
		Method[] methods = suite.getClass().getMethods();
		for(Method method:methods){
			if(method.getName().startsWith("test")){
				try {
					boolean markToInvoke = includeCats.isEmpty();
					boolean ignore = false;
					for(Annotation an:method.getAnnotations()){
						if(includeCats.contains(an.annotationType().getSimpleName().substring(1))){
							markToInvoke = true;
						}
						if(an.annotationType().getSimpleName().equals("Ignore")){
							ignore = true;
						}
					}
					if(markToInvoke&&!ignore){
						method.invoke(suite);
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception oe){
					System.err.println(method.getName()+" threw an exception");
					if(Log.verbose){
						oe.printStackTrace();
					}
				}
			}
		}
	}
	private void setupTestShip(){
		Ship ship = new Ship();
		Tile tile = new Tile(50,6){
			{
				this.isTravelPoint = true;
			}
		};
		ship.addTileToShip(tile);
		Hub.ships.add(ship);
	}
	
	@iStorage
	public void testFileStorage(){
		setupTestShip();
		Storage.saveList(Hub.ships, "./data/ships.data");
	}

	@iLocation
	public void testMinableResourceOnLocation(){
		reportTestResult("MinableResource",
				!"not found".equals(ResourcePrototype.translateResourceId(Hub.parentBody.getMineableResourceId())),true);
	}
	@iTile
	public void testTravelPoints(){
		TravelPoint a = new TravelPoint(0,0);
		TravelPoint d = new TravelPoint(0,1);
		TravelPoint b = new TravelPoint(1,1);
		TravelPoint c = new TravelPoint(2,2);
		a.addNext(d);
		a.addNext(b);
		b.addNext(c);
		d.addNext(c);
		//System.out.println("TravelPoint answer:"+TravelPath.findQuickest(a, c));
		TravelPath answer = new TravelPath();
		answer.add(a);
		answer.add(b);
		answer.add(c);
		reportTestResult("TravelPoint",TravelPath.findQuickest(a, c),answer);
	}

	private static void reportTestResult(String testName, Object result, Object answer){
		System.out.print(testName+" ");
		System.out.println(answer.equals(answer)?"success":"FAILURE");
	}
}
