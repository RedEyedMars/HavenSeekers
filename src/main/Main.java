package main;

import entity.Entity;
import environment.ship.Ship;
import environment.ship.menu.ShipConstructorMenu;
import environment.ship.tile.TravelPath;
import environment.ship.tile.TravelPoint;
import parser.Parser;
import gui.Gui;
import location.Location;
import location.materials.ResourcePrototype;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Parser.parseAllConfigs();
		//testSuite();
		//@SuppressWarnings("unused")
		Gui gui = new Gui();
	}

	public static void setup(){
		Hub.parentBody = Hub.locationTypes.get("solar").create();
		Ship ship = new Ship();
		Gui.setView(new ShipConstructorMenu(ship));
	}
	
}
