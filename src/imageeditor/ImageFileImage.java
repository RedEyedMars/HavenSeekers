package imageeditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import gui.Gui;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.graphics.collections.TextGraphicElement;
import gui.inputs.KeyBoardListener;
import main.Hub;

public class ImageFileImage extends GraphicEntity implements KeyBoardListener{

	private boolean reload;
	private int size;
	private String path;
	private long lastModified;
	private Thread thread;
	private String absPath;
	private TextGraphicElement text;
	private boolean animate = true;

	public ImageFileImage(File file, String name, final String path, int size) {
		super(name);
		//super("blank");
		this.path = path;
		this.absPath = file.getAbsolutePath();
		this.size = size;
		this.reload = false;
		this.lastModified = file.lastModified();
		thread = new Thread(new Runnable(){
			@Override
			public void run() {
				while(ImageFileParser.running){
					synchronized(thread){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						try {
							while(!entity.isVisible()){
								thread.wait();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						long currentModified = new File(absPath).lastModified();
						if(currentModified!=lastModified){
							reload=true;
							lastModified = currentModified;
						}
					}
				}				
			}});
		thread.start();
		this.text = new TextGraphicElement(""+size,TextGraphicElement.MEDIUM);
		this.text.setX(0.1f);
		this.text.setY(0.9f);
	}

	@Override
	public void setView(GraphicView parent){
		parent.addChild(text);
		super.setView(parent);
	}

	@Override
	public float offsetX(int i){
		if(i==0){
			return -getWidth()/2f+0.1f;
		}
		else return 0f;
	}
	@Override
	public float offsetY(int i){
		if(i==0){
			return getHeight()-0.1f;
		}
		else return 0f;
	}
	@Override
	public void update(){
		if(reload){
			this.entity.setFrame(0);
			Hub.renderer.reloadExternalTexture(entity.getTextureName(), path, size);
			reload=false;
		}
		super.update();
	}

	@Override
	public void setVisible(boolean vis){
		if(vis&&!entity.isVisible()){
			synchronized(thread){
				thread.notifyAll();
			}
		}
		text.setVisible(vis);
		super.setVisible(vis);
	}

	public static ImageFileImage create(File file, int size) {
		String name = getName(file);
		Hub.renderer.loadImageFromExternalPath(file.getPath(), size, name);
		return new ImageFileImage(file,name,file.getPath(),size);
	}
	public static ImageFileImage create(File file) {
		return create(file,1);
	}

	public static String getName(File file) {
		return file.getName().replaceAll("\\..*","");
	}

	public String getName() {
		return entity.getTextureName();
	}

	@Override
	public void input(char c, int keycode) {
		//System.out.println(keycode);
		if(keycode==14){
			if(size>1){
				size/=10;
				reload=true;
			}
			if(size<1)size=1;
		}
		else if(keycode>=2&&keycode<=11){
			if(keycode==11)keycode=1;
			keycode-=1;
			if(size==1&&keycode!=0){
				size=keycode;
			}
			else {
				size*=10;
				size+=keycode;
			}
			reload=true;
		}
		if(reload){
			text.update(""+size);
		}
	}

	@Override
	public void animate(){
		if(this.animate){
			this.entity.setFrame(this.entity.textureIndex()+1);
			if(this.entity.textureIndex()>=size){
				this.entity.setFrame(0);
			}
		}
	}

	@Override
	public void setFrame(int i){
		entity.setFrame(i%size);
	}

	public void animate(boolean b) {
		this.animate  = b;
	}
	
	public void saveData(BufferedWriter writer) throws IOException{
		writer.write(path);
		writer.write(":"+size+"\n");
	}
}
