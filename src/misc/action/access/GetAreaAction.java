package misc.action.access;

import misc.action.WrapperAction;
import entity.Entity;

public class GetAreaAction extends WrapperAction<Entity>{

	private String name;
	public GetAreaAction(String name){
		super(null);
		this.name = name;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void act(Entity subject) {
		get().set(subject.getShip().getArea(name));
	}

}
