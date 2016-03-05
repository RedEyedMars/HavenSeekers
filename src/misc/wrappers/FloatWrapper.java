package misc.wrappers;

public class FloatWrapper extends ObjectWrapper<Float> {
	public FloatWrapper(Float o) {
		super(o);
	}

	public static StorableFloatWrapper s(float f) {
		return new StorableFloatWrapper(new FloatWrapper(f));
	}
}
