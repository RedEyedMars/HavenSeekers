package entity.choice.action;

import misc.action.Action;
import entity.Entity;

public class RemoveRoleAction implements Action<Entity> {

	private String role;

	public RemoveRoleAction(String role) {
		this.role = role;
	}

	@Override
	public void act(Entity subject) {
		subject.removeRole(role);
	}

}
