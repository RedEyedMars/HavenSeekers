package parser;
import loom.Process;
public class ParseFileProcess extends Process {

	private String filename;

	public ParseFileProcess(Parser subject, String filename) {
		super(subject);
		this.filename = filename;
	}

	@Override
	public void act(Object subject) {
		Parser parser = ((Parser)subject);
		parser.readAndParseFile(filename);
	}

}
