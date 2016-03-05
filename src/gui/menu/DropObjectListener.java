package gui.menu;

import gui.graphics.GraphicView;
import misc.condition.Condition;

public interface DropObjectListener {
	public boolean drop(float x, float y, Object... data);

	public class conditions {
		public static Condition<GraphicView> getInstanceCondition(){
			return new Condition<GraphicView>(){
				@Override
				public boolean satisfied(GraphicView o) {
					return o instanceof DropObjectListener;
				}};
		}
	}
}
