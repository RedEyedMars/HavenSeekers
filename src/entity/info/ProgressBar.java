package entity.info;


public class ProgressBar extends Info{

	public ProgressBar(final Integer low,final Integer upper,final Integer start,final Integer end) {
		super(new ProgressBarGraphicElement(low,start,end,upper));
		//low is the leftmost bound
		//start is the second from the left
		//end is the second from the right
		//upper is the rightmost bound
		//[low--[start--end]--upper]
	}

	@Override
	public void update(Info info) {
		if(info==null){
			display.setVisible(false);
		}
		else if(info instanceof ProgressBar){
			ProgressBar b = (ProgressBar)info;
			((ProgressBarGraphicElement)display).update(b.low(), b.start(), b.end(), b.upper());
		}
	}

	private float low() {
		return ((ProgressBarGraphicElement)display).getLow();
	}
	private float upper() {
		return ((ProgressBarGraphicElement)display).getUpper();
	}
	private float start() {
		return ((ProgressBarGraphicElement)display).getStart();
	}
	private float end() {
		return ((ProgressBarGraphicElement)display).getEnd();
	}

}
