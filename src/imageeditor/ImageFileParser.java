package imageeditor;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import main.Hub;
import main.Main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;

import gui.gl.GLApp;
import gui.gl.GLFont;
import gui.graphics.GraphicRenderer;
import gui.inputs.KeyBoardListener;
import gui.inputs.MotionEvent;
import gui.inputs.MouseListener;



/**
 * Use setMaterial(), setLight() and makeTexture() to control light and material properties.
 * <P>
 * napier at potatoland dot org
 */
public class ImageFileParser extends GLApp {
	public static boolean running = true;
	private static Stack<MouseListener> mouseListener = new Stack<MouseListener>();
	private static Stack<KeyBoardListener> keyboardListener = new Stack<KeyBoardListener>();
	
    // Light position: if last value is 0, then this describes light direction.  If 1, then light position.
    float lightPosition2[]= { 0f, 0f, 10f, 0f };
    //------------------------------------------------------------------------
    // Run main loop of application.  Handle mouse and keyboard input.
    //------------------------------------------------------------------------

	private float[] cameraPos = new float[]{0f, 0f, 1f};
	private float[] cameraLook = new float[]{0f, 0f, 0f};
	private boolean drag = false;
	

	public static GLFont font;
	
	public static void main(String[] args){
		new ImageFileParser();
	}
	
    public ImageFileParser(){
    	super();

        window_title = "Image Viewer";
        displayWidth = 800;
        displayHeight = 600;
        Hub.width = displayWidth;
        Hub.height = displayHeight;
        this.run();  // will call init(), render(), mouse event functions
    }


    /**
     * Initialize the scene.  Called by GLApp.run()
     */
    public void setup()
    {
        giveOnClick(Hub.genericMouseListener);
        giveOnType(Hub.genericKeyBoardListener);
    	Hub.renderer = new GraphicRenderer();
        // Create texture for ground plane
        // setup perspective
        setPerspective();

        // no overall scene lighting
        setAmbientLight(new float[] { 4f, 4f, 4f, 0f });
        
        // enable lighting and texture rendering
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // select model view for subsequent transforms
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

    	font = new GLFont( new Font("Trebuchet", Font.BOLD, 18) );
    	
    	
    	
    	Hub.currentView = new ImageFileEditor(".");
    	giveOnClick(Hub.currentView);
    }


	@Override
    public void update(){
    	Hub.currentView.update(); 
    }
    
    /**
     * Set the camera position, field of view, depth.
     */
    public static void setPerspective()
    {
        // select projection matrix (controls perspective)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // fovy, aspect ratio, zNear, zFar
        GLU.gluPerspective(30f, aspectRatio, 1f, 100f);
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
    
    /**
     * Render one frame.  Called by GLApp.run().
     */
    public void draw() {
        // clear depth buffer and color buffers
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // select model view for subsequent transforms
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        // set the viewpoint
        GLU.gluLookAt(cameraPos[0], cameraPos[1], cameraPos[2],  // where is the eye
        		cameraLook[0], cameraLook[1], cameraLook[2],    // what point are we looking at
        			0f, 1f, 0f);    // which way is up
        if(Hub.renderer!=null){
        	Hub.renderer.display();
        }
        
    }


    public void mouseMove(int x, int y) {
    	mouseListener.peek().onHover(new MotionEvent(x/Hub.width,y/Hub.height,MotionEvent.ACTION_DOWN,MotionEvent.MOUSE_LEFT));
    }
    
    public void dragLeftDown(int x, int y) {
    	if(drag){
    		mouseListener.peek().onClick(new MotionEvent(x/Hub.width,y/Hub.height,MotionEvent.ACTION_DOWN,MotionEvent.MOUSE_LEFT));
    	}
    }
    
    public void mouseLeftDown(int x, int y) {
    	
    	mouseListener.peek().onClick(new MotionEvent(x/Hub.width,y/Hub.height,MotionEvent.ACTION_DOWN,MotionEvent.MOUSE_LEFT));

    }

    public void mouseLeftUp(int x, int y) {
    	mouseListener.peek().onClick(new MotionEvent(x/Hub.width,y/Hub.height,MotionEvent.ACTION_UP,MotionEvent.MOUSE_LEFT));
    }
    
    public void dragRightDown(int x, int y) {
    	if(drag){
    		mouseListener.peek().onClick(new MotionEvent(x/Hub.width,y/Hub.height,MotionEvent.ACTION_DOWN,MotionEvent.MOUSE_RIGHT));
    	}
    }
    public void mouseRightDown(int x, int y) {
    	mouseListener.peek().onClick(new MotionEvent(x/Hub.width,y/Hub.height,MotionEvent.ACTION_DOWN,MotionEvent.MOUSE_RIGHT));
    }

    public void mouseRightUp(int x, int y) {
    	mouseListener.peek().onClick(new MotionEvent(x,Hub.height-y,MotionEvent.ACTION_UP,MotionEvent.MOUSE_RIGHT));
    }

    @Override
    public void keyDown(char c, int keycode) {
    	keyboardListener.peek().input(c,keycode);
    }


    public void keyUp(int keycode) {
    }


	public static void giveOnClick(MouseListener listener) {
		mouseListener.push(listener);
		//System.out.println("push"+mouseListener.size()+listener.getClass());
	}
	
	public static void removeOnClick(MouseListener listener) {
		mouseListener.pop();
		//System.out.println("pop"+mouseListener.size()+mouseListener.peek().getClass());
	}
	

    private static void giveOnType(KeyBoardListener listener) {
    	keyboardListener.push(listener);
	}
	
	@Override
	public void cleanup(){
		Hub.currentView.onRemoveFromDrawable();
		this.running = false;
		super.cleanup();
	}


	public void disableDrag() {
		drag = false;
	}

	public void enableDrag(){
		drag = true;
	}
	
	@Override
	public void mouseWheel(int distance){
		mouseListener.peek().onMouseScroll(distance/120);
	}

	public static void removeOnKey() {
		keyboardListener.pop();
	}

	public static void giveOnKey(KeyBoardListener listener) {
		keyboardListener.push(listener);
	}

}
