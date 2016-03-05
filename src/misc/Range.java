package misc;

import misc.wrappers.FloatWrapper;

public class Range extends FloatWrapper{
	private FloatWrapper start;
	private FloatWrapper end;

	public Range(String unparsed){
		super(0f);
		String[] split = unparsed.split("-");
		if(split.length==2){
			start = new FloatWrapper(Float.parseFloat(split[0].replace("k", ""))*(split[0].contains("k")?1000f:1f));
			end = new FloatWrapper(Float.parseFloat(split[1].replace("k", ""))*(split[1].contains("k")?1000f:1f));
		}
		else if(split.length==1){
			start = new FloatWrapper(Float.parseFloat(unparsed.replace("k", ""))*(unparsed.contains("k")?1000f:1f));
			end = new FloatWrapper(Float.parseFloat(unparsed.replace("k", ""))*(unparsed.contains("k")?1000f:1f));
		}
	}
	public Float get(){
		return (float) (start.get()+(end.get()-start.get())*Math.random());
	}
}
