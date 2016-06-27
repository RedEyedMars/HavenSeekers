package environment.ship.tile;

import java.util.HashMap;
import java.util.Map;

import environment.Direction;
import environment.Position;
import environment.Positionable;
import gui.graphics.GraphicEntity;
import misc.condition.Condition;
import parser.StringHeirachy;
import storage.Storable;
import storage.Storer;
import storage.StorerageIterator;

public class Wall extends GraphicEntity implements Positionable {

	private Position position;
	private boolean cover;
	private Map<Integer,Boolean> directionMap = new HashMap<Integer,Boolean>();
	private int directions = 0;

	public Wall(int x, int y) {
		super("walls_1");
		this.position = new Position(x,y);
		for(int i=0;i<4;++i){
			directionMap.put(i, false);
		}
		this.resetVisibility();
	}

	@Override
	public Position getPosition() {
		return position;
	}
	
	public void tileCover(boolean cover){
		this.cover = cover;
		resetVisibility();
	}
	
	public void addSideTiled(Direction direction){
		this.directionMap.put(direction.getIndex(),true);
		++this.directions;
		resetVisibility();
	}
	
	public void removeTile(Direction direction){
		this.directionMap.put(direction.getIndex(),false);
		--this.directions;
		resetVisibility();
	}

	private void resetVisibility() {
		if(!cover&&directions>0){
			this.setVisible(true);
			if(directions==1){
				for(int i=0;i<4;++i){
					if(directionMap.get(i)){
						this.setFrame(i);
						break;
					}
				}
			}
			else if(directions==2){
				int i = 0;
				if(!directionMap.get(1)){
					i+=3;
					if(!directionMap.get(0)){
						i=5;
					}
					else if(directionMap.get(2)){
						i=4;
					}
				}
				else {
					if(directionMap.get(0)){
						i=0;
					}
					else if(directionMap.get(3)){
						i=1;
					}
					else if(directionMap.get(2)){
						i=2;
					}
				}
				i+=4;//to pass last 4
				this.setFrame(i);
			}
			else if(directions==3){
				for(int i=0;i<4;++i){
					if(!directionMap.get(i)){
						this.setFrame(10+i);//10==4+6
					}
				}
			}
			else if(directions==4){
				this.setFrame(15);
			}
		}
		else {
			this.setVisible(false);
		}
	}

}
