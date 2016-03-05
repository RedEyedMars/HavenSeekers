package imageeditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.Gui;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.graphics.collections.TextGraphicElement;
import gui.inputs.MotionEvent;
import main.Hub;
import gui.menu.ButtonOption;
import gui.menu.DropObjectListener;
import gui.menu.FlickerListMenu;
import gui.menu.MenuItem;
import gui.menu.TabMenu;

public class ImageFileEditor extends GraphicView {

	private Map<String,ImageFileImage> imageMap = new HashMap<String,ImageFileImage>();
	private List<ImageFileImage> images = new ArrayList<ImageFileImage>();
	private int currentlySelected = 0;
	private TabMenu menu;
	private File directory;
	private List<File> toAdd = new ArrayList<File>();
	private GraphicEntity background;

	public ImageFileEditor(String filePath) {
		super();
		makeMenu();
		background= new GraphicEntity("blank");
		setBackground("blank");
		addChild(background);
		directory = new File(filePath);
		for(File file:directory.listFiles()){
			if(file.getName().endsWith(".meta")){
				parseMeta(file);
			}
		}
		select();
		addChild(menu);
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				while(ImageFileParser.running){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(File file:directory.listFiles()){
						if(file.getName().endsWith(".png")){
							if(!imageMap.containsKey(ImageFileImage.getName(file))){
								toAdd.add(file);
							}
						}
					}
				}
			}});
		thread.start();
	}

	private void parseMeta(File file) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while(line!=null){
				String[] split = line.split(":");
				if(split[0].endsWith(".png")){
					ImageFileImage image = ImageFileImage.create(new File(split[0]),Integer.parseInt(split[1]));
					addChild(image);
					imageMap.put(image.getName(),image);
					images.add(image);
					if(images.size()>1){
						image.setVisible(false);
					}
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void makeMenu(){
		this.menu = new TabMenu("Sizes","Controls");
		menu.adjust(0.2f, 1f);
		menu.setX(0.8f);
		Hub.renderer.loadImageFromPath("images/1/gui_box.png", 1, "gui_box");
		Hub.renderer.loadImageFromPath("images/1/blank.png", 1, "blank");
		Hub.renderer.loadImageFromPath("images/1/floor_tile_1.png", 1, "floor_tile_1");
		Hub.renderer.loadImageFromPath("images/2/tab_choice.png", 2, "tab_choice");
		this.menu.addChildTo(new MenuItem(null,new float[]{1f},new TextGraphicElement("Ship 40x40",TextGraphicElement.MEDIUM)){
			@Override
			public boolean onClick(MotionEvent event){
				if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
					setSize(0.75f,1);
					setBackground("blank");
					return true;
				}
				else return false;
			}
		}, "Sizes");
		this.menu.addChildTo(new MenuItem(null,new float[]{1f},new TextGraphicElement("Tile 1x1",TextGraphicElement.MEDIUM)){
			@Override
			public boolean onClick(MotionEvent event){
				if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
					setSize(
							0.01875f,0.025f);
					setBackground("floor_tile_1");
					return true;
				}
				else return false;
			}
		}, "Sizes");
		this.menu.addChildTo(new MenuItem(null,new float[]{1f},new TextGraphicElement("Tile 2x2",TextGraphicElement.MEDIUM)){
			@Override
			public boolean onClick(MotionEvent event){
				if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
					setSize(
							0.01875f*2,0.025f*2);
					setBackground("floor_tile_1");
					return true;
				}
				else return false;
			}
		}, "Sizes");
		this.menu.addChildTo(new MenuItem(null,new float[]{1f},new TextGraphicElement("Tile 4x4",TextGraphicElement.MEDIUM)){
			@Override
			public boolean onClick(MotionEvent event){
				if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
					setSize(
							0.01875f*4,0.025f*4);
					setBackground("floor_tile_1");
					return true;
				}
				else return false;
			}
		}, "Sizes");

		this.menu.addChildTo(new MenuItem(null,new float[]{1f},new TextGraphicElement("Play",TextGraphicElement.MEDIUM)){
			@Override
			public boolean onClick(MotionEvent event){
				if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
					startAnimation();
					return true;
				}
				else return false;
			}
		}, "Controls");
		this.menu.addChildTo(new MenuItem(null,new float[]{1f},new TextGraphicElement("Stop",TextGraphicElement.MEDIUM)){
			@Override
			public boolean onClick(MotionEvent event){
				if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
					stopAnimation();
					return true;
				}
				else return false;
			}
		}, "Controls");
		this.menu.addChildTo(new MenuItem(null,new float[]{1f},new TextGraphicElement("Step",TextGraphicElement.MEDIUM)){
			@Override
			public boolean onClick(MotionEvent event){
				if(event.getAction()==MotionEvent.ACTION_DOWN&&isWithin(event.getX(),event.getY())){
					stepAnimation();
					return true;
				}
				else return false;
			}
		}, "Controls");
	}

	@Override
	public void update(){
		while(!toAdd.isEmpty()){
			ImageFileImage image = ImageFileImage.create(toAdd.remove(0));
			addChild(image);
			imageMap.put(image.getName(),image);
			images.add(image);
			if(currentlySelected==images.size()-1){
				select();
			}
			else {
				image.setVisible(false);
			}
		}
		super.update();
	}

	@Override
	public boolean onHover(MotionEvent event){
		if(event.getX()>0.8f){
			menu.setVisible(true);
		}
		else {
			menu.setVisible(false);
		}
		return true;
	}

	@Override
	public void onMouseScroll(int distance){
		unselect();
		currentlySelected+=distance;
		if(currentlySelected>=images.size()){
			currentlySelected=images.size()-1;
		}
		if(currentlySelected<0){
			currentlySelected=0;
		}
		select();
	}
	private void unselect() {

		if(images.size()>0){
			ImageFileParser.removeOnKey();
			images.get(currentlySelected).setVisible(false);
		}
	}

	private void select() {

		if(images.size()>0){
			images.get(currentlySelected).setVisible(true);
			ImageFileParser.giveOnKey(images.get(currentlySelected));
		}
	}
	public void setSize(float dx, float dy){
		for(int i=0;i<images.size();++i){
			images.get(i).adjust(dx, dy);
			images.get(i).setX(0.5f-dx/2f);
			images.get(i).setY(0.5f-dy/2f);
		}

		background.adjust(dx, dy);
		background.setX(0.5f-dx/2f);
		background.setY(0.5f-dy/2f);
	}


	private int animationStep = 0;
	private void stepAnimation() {
		stopAnimation();
		for(int i=0;i<images.size();++i){
			images.get(i).setFrame(animationStep);
		}
		++animationStep;
	}
	private void stopAnimation(){
		for(int i=0;i<images.size();++i){
			images.get(i).animate(false);
		}
	}
	private void startAnimation(){
		for(int i=0;i<images.size();++i){
			images.get(i).animate(true);
		}
	}


	private void setBackground(String string) {
		if("blank".equals(string)){
			background.setVisible(false);
		}
		else {
			background.setVisible(true);
		}
		background.setTextureName(string);
	}

	public void onRemoveFromDrawable(){

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("fileeditor.meta",false));
			if(images.size()>0){
				images.get(currentlySelected).saveData(writer);
				for(int i=0;i<images.size();++i){
					if(i!=currentlySelected){
						images.get(i).saveData(writer);
					}
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
