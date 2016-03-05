package parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CommandParser extends Parser{

	public static void main(String[] args){
		CommandParser.parseCommandsFile();
		FileWriter writer;
		try {
			writer = new FileWriter("src/gui/graphics/images/image.list",false);
			for(File size:new File("src/gui/graphics/images/").listFiles()){
				if(size.isDirectory()){
					for(String name:size.list()){
						writer.write(size.getPath().replace("src/gui/graphics/images/".replace("/", File.separator),"")+File.separator+name+"\n");
					}

				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void parseCommandsFile(){
		File parserDirectory = new File("res/parser/");
		CommandParser parser = new CommandParser();
		for(String filename:parserDirectory.list()){
			parser.readAndParseFile("res/"+parserDirectory.getName()+File.separator+filename);
		}
	}

	@Override
	public void parseInput(String prefix,String suffix,StringHeirachy input){
		StringBuilder builder = new StringBuilder();
		StringBuilder ender = new StringBuilder();
		for(StringHeirachy s:input){
			parse(s,null,null,builder,ender);
		}
		builder.append(ender.toString());
		try{
			FileWriter writer = new FileWriter(new File("src/parser/ParserCommandsAreas.java"), false);
			writer.write(header.replace("{0}", builder.toString()));
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void findDIR(File file,String header){
		if(file==null||file.listFiles()==null)return;
		for(File sub:file.listFiles()){
			if(sub.isDirectory()){
				findDIR(sub,header+"/"+sub.getName());
			}
			else {
				System.out.println(header+"/"+sub.getName());
			}
		}
	}

	private void parse(StringHeirachy s, String classType, String type, StringBuilder builder, StringBuilder ender){
		switch(s.getId()){
		case ':':{
			if(classType==null){
				for(StringHeirachy sub:s){
					parse(sub,s.getValue(),type,builder,ender);
				}
			}
			else if(type==null){
				for(StringHeirachy sub:s){
					parse(sub,classType,s.getValue(),builder,ender);
				}
			}
			break;
		}
		case '/':{
			String[] split = s.getValue().split("/");
			for(String name:split){
				ender.append("\t\tcommandMap.put(\"");
				ender.append(name);
				ender.append("\",commandMap.get(\"");
				ender.append(split[0]);
				ender.append("\"));\n");
			}
			break;
		}
		default:{
			String value = s.getValue()+s.getId();
			String front = value.substring(0,value.indexOf(' '));
			String rest = value.substring(value.indexOf(' ')+1);
			String callClassName = front+classType;
			boolean isStar = false;
			boolean isDash = false;
			if("*".equals(front)){
				isStar = true;
				callClassName= classType;
			}
			else if("-".equals(front)){
				isDash = true;
			}
			boolean hasParameters = rest.indexOf(' ')!=-1;
			if(hasParameters){
				front = rest.substring(0,rest.indexOf(' '));
				rest = rest.substring(rest.indexOf(' ')+1);
			}
			else {
				front = rest;
				rest = "";
			}
			if(isDash){
				callClassName = front.substring(0,1).toUpperCase()+front.substring(1)+classType;
			}
			String[] returnTypes = rest.split(" ");
			builder.append("\t\tcommandMap.put(\""+front+"\",new ParserCommand<"+callClassName+">(){\n");
			if(!"".equals(rest)){
				builder.append("\t\t\t{type=\""+type.replace("[]","")+"\"; prototypes = new String[]{\""+rest.replace(" ","\", \"")+"\"};}\n");
			}
			else {
				builder.append("\t\t\t{type=\""+type.replace("[]","")+"\"; prototypes = new String[]{};}\n");
			}
			builder.append("\t\t\t@Override\n");
			builder.append("\t\t\tpublic "+callClassName+" execute(){\n");
			StringBuilder returnStatement = new StringBuilder();
			returnStatement.append("\t\t\t\treturn ");
			if(!isStar){
				returnStatement.append("new "+callClassName+"(");
			}
			for(int i=0;i<returnTypes.length;++i){
				if(returnTypes[i].equals("")){
					continue;
				}
				if(i>0){
					returnStatement.append("\t\t\t\t\t\t");
				}
				String returnType = returnTypes[i];

				if(returnType.equals("Number")){
					returnType = "FloatWrapperAction";
				}
				else if(returnType.equals("Object")){
					returnType = "WrapperAction";
				}
				else if(returnType.equals("Number...")){
					returnType = "FloatWrapperAction...";
				}
				else if(returnType.equals("Object...")){
					returnType = "WrapperAction...";
				}
				if(type.equals("String[]")){
					returnType = returnType.substring(0,returnType.length()-3);
					builder.append("\t\t\t\tStringBuilder builder = new StringBuilder();\n");
					builder.append("\t\t\t\tfor(int i=");
					builder.append(i);
					builder.append(";i<params.size();++i){\n");
					builder.append("\t\t\t\t\tbuilder.append(");
					builder.append("((ParserCommand<");
					builder.append(returnType);
					builder.append(">)");
					builder.append("params.get(i)).execute());\n");
					builder.append("\t\t\t\t\tbuilder.append(\' \');\n");
					builder.append("\t\t\t\t}\n");
					returnStatement.append("builder.toString().trim()");
					if(i<returnTypes.length-1){
						returnStatement.append(",");
					}
				}
				else if(returnType.endsWith("...")){
					returnType = returnType.substring(0,returnType.length()-3);
					builder.append("\t\t\t\tList<");
					builder.append(returnType);
					builder.append("> collection");
					builder.append(i);
					builder.append(" = new ArrayList<");
					builder.append(returnType);
					builder.append(">();\n");
					builder.append("\t\t\t\tfor(int i=");
					builder.append(i);
					builder.append(";i<params.size();++i){\n");
					builder.append("\t\t\t\t\tcollection");
					builder.append(i);
					builder.append(".add(");
					builder.append("((ParserCommand<");
					builder.append(returnType);
					builder.append(">)");
					builder.append("params.get(i)).execute());\n");
					builder.append("\t\t\t\t}\n");
					returnStatement.append("collection");
					returnStatement.append(i);
					if(i<returnTypes.length-1){
						returnStatement.append(",");
					}
				}
				else {
					returnStatement.append("((ParserCommand<");
					returnStatement.append(returnType);
					returnStatement.append(">)");

					returnStatement.append("params.get(");
					returnStatement.append(i);
					returnStatement.append(")).execute()");
					if(i<returnTypes.length-1){
						returnStatement.append(",\n");
					}
				}
			}
			if(!isStar){
				returnStatement.append(")");
			}
			returnStatement.append(";\n");
			returnStatement.append("\t\t\t}\n");
			returnStatement.append("\t\t});\n");
			builder.append(returnStatement.toString());
			break;
		}
		}
	}
}
