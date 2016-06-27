package main;

import entity.Entity;
import environment.ship.Ship;
import environment.ship.menu.ShipConstructorMenu;
import environment.ship.tile.TravelPath;
import environment.ship.tile.TravelPoint;
import parser.Parser;
import storage.Storage;
import gui.Gui;
import location.Location;
import location.materials.ResourcePrototype;
import loom.Processor;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Parser.parseAllConfigs();
		TestSuite.test(TestSuite.current);
		//@SuppressWarnings("unused")
		Gui gui = null;
		gui = new Gui();
		if(gui==null){//not using gui
			Gui.running = false;
			Processor.close();
		}
	}

	public static void setup(){
		Hub.parentBody = Hub.locationTypes.get("solar").create();
		ShipConstructorMenu view = (ShipConstructorMenu) Storage.load("./data/current.data", ShipConstructorMenu.class);
				//new ShipConstructorMenu();
		Gui.setView(view);
	}
	
}
