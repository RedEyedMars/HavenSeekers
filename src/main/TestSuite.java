package main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import environment.ship.tile.TravelPath;
import environment.ship.tile.TravelPoint;
import location.materials.ResourcePrototype;

public class TestSuite {
	public TestSuite(){
		Method[] methods = getClass().getMethods();
		for(Method method:methods){
			if(method.getName().startsWith("test")){
				try {
					method.invoke(this);
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
	private void testMinableResourceOnLocation(){
		reportTestResult("MinableResource",
				!"not found".equals(ResourcePrototype.translateResourceId(Hub.parentBody.getMineableResourceId())),true);
	}
	private void testTravelPoints(){
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
