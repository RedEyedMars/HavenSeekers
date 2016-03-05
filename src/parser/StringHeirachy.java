package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class StringHeirachy extends ArrayList<StringHeirachy>{
	private static final long serialVersionUID = -4022004832508973191L;
	private Map<String,String> rules;
	private int level;
	private String value;
	private StringHeirachy parent;
	private char id;
	public StringHeirachy(int level, String value, StringHeirachy parent){
		super();
		this.level = level;
		this.value = value.substring(0,value.length()-1);
		this.id = value.charAt(value.length()-1);
		this.parent = parent;
		if(parent!=null){
			 rules = parent.rules;
		}
		else {
			rules = new HashMap<String,String>();
		}
	}
	public StringHeirachy(int level, String value, char id, StringHeirachy parent){
		super();
		this.level = level;
		this.value = value;
		this.id = id;
		this.parent = parent;
		if(parent!=null){
			 rules = parent.rules;
		}
		else {
			rules = new HashMap<String,String>();
		}
	}
	public StringHeirachy appendLine(String in) {
		int level = 0;
		StringHeirachy input = this;
		while(in.startsWith("\t")){
			++level;
			in = in.substring(1);
		}
		if(level==this.level){
			input  = new StringHeirachy(level,in,parent);
			parent.add(input);
		}
		else if(level==this.level+1){
			input = new StringHeirachy(level,in,this);
			this.add(input);
		}
		else {
			while(level<input.level+1){
				input = input.parent;
			}
			StringHeirachy addition = new StringHeirachy(level,in,input);
			input.add(addition);
			input = addition;
		}
		return input;
	}
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(hashCode());
		builder.append(":<");
		builder.append(parent==null?"null":parent.hashCode());
		builder.append(">(");
		builder.append(level);
		builder.append(",");
		builder.append(id);
		builder.append(")]");
		for(int i=0;i<level;++i){
			builder.append("\t");
		}
		builder.append(value);
		builder.append("\n");
		for(StringHeirachy string:this){
			builder.append(string.toString());
		}
		return builder.toString();
	}
	public StringHeirachy append(String line){
		for(String name:rules.keySet()){
			line = line.replace(name, rules.get(name));
		}
		return appendLine(line);
	}
	public void write(String line){
		
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public char getId() {
		return id;
	}
	public void addRule(String s1, String s2) {
		rules.put(s1, s2);
	}
}