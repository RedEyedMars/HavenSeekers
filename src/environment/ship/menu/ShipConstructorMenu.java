package environment.ship.menu;

import entity.Entity;
import environment.Position;
import environment.items.Area;
import environment.items.AreaPrototype;
import environment.ship.Ship;
import environment.ship.tile.Corridor;
import environment.ship.tile.Room;
import environment.ship.tile.Tile;
import gui.graphics.collections.HorizontalGraphicArray;
import gui.menu.DropObjectListener;
import gui.menu.TabMenu;

public class ShipConstructorMenu extends HorizontalGraphicArray implements DropObjectListener{
	private Ship toConstruct;
	public ShipConstructorMenu(Ship shipToConstruct){
		super(new float[]{0.75f,0.25f},shipToConstruct.getInsideView(),new TabMenu("Rooms","Items", "Corridors", "Launch"));
		toConstruct = shipToConstruct;
		TabMenu list = (TabMenu) getChild(1);
		list.addChildTo(Room.makeMenuItem(2, 10), "Rooms");
		AreaPrototype.makeMenuItems(list,"Items");
		list.addChildTo(Corridor.makeMenuItem(), "Corridors");
	}
	public Ship getShip() {
		return toConstruct;
	}
	@Override
	public boolean drop(float x, float y, Object... data) {
		if(data[0] instanceof Room){
			Room room = (Room)data[0];
			Position receivedPosition = toConstruct.getPositionFromFloats(x,y);
			if(toConstruct.getTile(receivedPosition)!=null&&!toConstruct.isWalled(receivedPosition)){
				return false;
			}
			room.adjustPosition(receivedPosition);
			toConstruct.addTileToShip(room);
		}
		else if(data[0] instanceof Corridor){
			Corridor corridor = (Corridor)data[0];
			Position receivedPosition = toConstruct.getPositionFromFloats(x,y);
			if(toConstruct.getTile(receivedPosition)!=null&&!toConstruct.isWalled(receivedPosition)){
				return false;
			}
			corridor.adjustPosition(receivedPosition);
			toConstruct.addTileToShip(corridor);
		}
		else if(data[0] instanceof Position){
			Position receiver = (Position)data[0];
			Position receivedPosition = toConstruct.getPositionFromFloats(x,y);
			if(toConstruct.getTile(receivedPosition)!=null&&!toConstruct.isWalled(receivedPosition)){
				return false;
			}
			receiver.adjust(receivedPosition);
		}
		else if(data[0] instanceof Area){
			Tile tile = toConstruct.getTile(toConstruct.getPositionFromFloats(x,y));
			if(tile!=null){
				tile.setOwner((Area)data[0], true);
			}
			else return false;
		}
		return true;
	}
	
}
