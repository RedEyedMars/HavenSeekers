package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import environment.ship.menu.ShipConstructorMenu;
import main.Hub;
import parser.Parser;
import parser.StringHeirachy;

public class Storage {

	public static void loadList(final List<Storable> list, String filename, final Class<? extends Storable> creator){

		Byte[] file = readVerbatum(filename);
		int index = 0;
		for(;file[index]!='\n';++index);
		for(++index;index<file.length;){
			Storable target;
			try {
				target = creator.newInstance();
				index = target.getStorer().load(file, index, loadStringMap(file));
				list.add(target);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}

		}	
	}

	private static Map<Integer,String> loadStringMap(Byte[] file) {
		Map<Integer,String> strings = new HashMap<Integer,String>();
		int next = 0;
		for(int i=1;i<file.length;i=next+1){
			next=i;
			while(file[next]!='\t'){
				if(file[next]=='\n'){
					strings.put(strings.size(), readStringFromBytes(file,i,next));
					return strings;
				}
				++next;
			}
			strings.put(strings.size(), readStringFromBytes(file,i,next));
		}
		return strings;
	}
	
	private static String readStringFromBytes(Byte[] file, int start, int end){
		StringBuilder builder = new StringBuilder();
		for(;start<end;++start){
			builder.append(((char)(byte)file[start]));
		}
		return builder.toString();
	}

	public static Storable load(String filename, final Class<? extends Storable> creator){
		Byte[] file = readVerbatum(filename);
		int index = 0;
		for(;file[index]!='\n';++index);
		try {
			Storable target = creator.newInstance();
			index = target.getStorer().load(file, index+1, loadStringMap(file));
			return target;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Byte[] readVerbatum(String filename){
		List<Byte> builder = new ArrayList<Byte>();
		try {
			FileInputStream reader = new FileInputStream(filename);
			int next = reader.read();
			while(((int)next)>=0){
				builder.add((byte)next);
				next = reader.read();
			}
			reader.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return builder.toArray(new Byte[0]);
	}

	public static void saveList(List<? extends Storable> list, String filename){
		createFolder("data");
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(filename,false);
			List<Byte> build = new ArrayList<Byte>();
			Map<String,Integer> strings = new HashMap<String,Integer>();
			for(Storable storable:list){
				storable.getStorer().store(build,strings);
			}
			int index = 0;
			for(int i=0;i<strings.size();++i){
				build.add(index++,(byte) '\t');
				for(String key:strings.keySet()){
					if(strings.get(key)==i){
						for(byte b:key.getBytes()){
							build.add(index++,b);
						}
					}
				}
			}
			build.add(index++,(byte)'\n');

			byte[] bytes = new byte[build.size()];
			for(int i=0;i<build.size();++i){
				bytes[i] = build.get(i);
			}
			writer.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(writer!=null){
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void save(Storable target, String filename) {
		createFolder("data");
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(filename,false);
			List<Byte> build = new ArrayList<Byte>();
			Map<String,Integer> strings = new HashMap<String,Integer>();
			target.getStorer().store(build,strings);
			int index = 0;
			for(int i=0;i<strings.size();++i){
				build.add(index++,(byte) '\t');
				for(String key:strings.keySet()){
					if(strings.get(key)==i){
						for(char b:key.toCharArray()){
							build.add(index++,(byte)b);
						}
					}
				}
			}
			build.add(index,(byte)'\n');
			byte[] bytes = new byte[build.size()];
			for(int i=0;i<build.size();++i){
				bytes[i] = build.get(i);
			}
			writer.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(writer!=null){
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createFolder(String folderName){
		StringBuilder path = new StringBuilder();
		path.append("./");
		path.append(folderName);
		File f = new File(path.toString());
		if(!f.exists()){
			f.mkdirs();
		}
	}
	public static void saveCurrentView() {
		if(Hub.currentView instanceof Storable){
			Storage.save((Storable)Hub.currentView, "./data/current.data");
		}
	}
}
