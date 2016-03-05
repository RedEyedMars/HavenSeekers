package entity.choice.action;

import misc.action.Action;
import entity.Entity;

public class AddRoleAction implements Action<Entity> {

	private String role;

	public AddRoleAction(String role) {
		this.role = role;
	}

	@Override
	public void act(Entity subject) {
		subject.addRole(role);
	}

}
