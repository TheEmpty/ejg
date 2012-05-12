package com.reliablerabbit.ejg;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferStrategy;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * A class to handle GUI and game state management.
 * @author Mohammad El-Abid
 */
public class Game implements WindowStateListener {
	/**
	 * Width of the canvas inside of the GUI
	 */
	private int defaultGameWidth = 800;
	/**
	 * Height of the canvas inside of the GUI
	 */
    private int defaultGameHeight = 600;
    /**
     * Has the GUI been setup before?
     */
    private boolean initalized = false;
    /**
     * The frame/window of the GUI
     */
	private JFrame frame;
	/**
	 * The canvas/drawing component of the GUI
	 */
	private Canvas canvas;
	/**
	 * The current game state running
	 */
	private GameState gameState;
	/**
	 * A stack of saved game states
	 */
	private Stack<GameState> gameStateStack = new Stack<GameState>();
	/**
	 * Currently in full-screen mode?
	 */
	private boolean fullScreen = false;
	/**
	 * Pointer to the switch for the game loop
	 */
	public boolean running = false;
	
	/**
	 * @return The current default game width
	 */
	public int getDefaultGameWidth() { return defaultGameWidth; }
	
	/**
	 * Set the default game width
	 */
	public void setDefaultGameWidth(int newWidth) { defaultGameWidth = newWidth; }
	
	/**
	 * @return The current default game height
	 */
	public int getDefaultGameHeight() { return defaultGameHeight; }
	
	/**
	 * Set the default game height
	 */
	public void setDefaultGameHeight(int newHeight) { defaultGameHeight = newHeight; }
	
	/**
	 * @return The current width of the canvas.
	 */
	public int getGameWidth() { return canvas.getWidth(); }
	
	/**
	 * @return The current height of the canvas.
	 */
	public int getGameHeight() { return canvas.getHeight(); }
	
	/**
	 * @return The JFrame/window of the game
	 */
	public JFrame getFrame() { return frame; }
	
	/**
	 * @return The canvas/art of the game
	 */
	public Canvas getCanvas() { return canvas; }
	
	/**
	 * Create or re-create the GUI
	 */
	public void createGUI() {
		// If Apple computer, listen for quit message
		if(!initalized) {
			if(System.getProperty("os.name").startsWith("Mac OS")) {
				// System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
				Runnable runner = new Runnable() {
					public void run() { exit(); }
				};
				Runtime.getRuntime().addShutdownHook(new Thread(runner));
			}
			initalized = true;
		}
		
		// destroy any previous frame
		if(frame != null) {
			frame.dispose();
		}
		
		// frame
		frame = new JFrame();
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.addWindowStateListener(this);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
		// canvas
		canvas = new Canvas();
		canvas.setIgnoreRepaint( true );
		canvas.setSize(defaultGameHeight, defaultGameWidth);
		
		// full screen
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		
		if(fullScreen) {
			frame.setIgnoreRepaint(true);
			frame.setUndecorated(true);
			gd.setFullScreenWindow(frame);
			if(!gd.isDisplayChangeSupported()) {
				System.err.println("Graphics Device does not support a display change.");
				fullScreen = false;
				createGUI();
				return;
			} else {
				DisplayMode[] modes = gd.getDisplayModes();
				gd.setDisplayMode(modes[modes.length - 1]);
				// gd.setDisplayMode( new DisplayMode(GAME_WIDTH * 2, GAME_HEIGHT * 2, 32, DisplayMode.REFRESH_RATE_UNKNOWN));
			}
		} else {
			Dimension window = new Dimension(defaultGameWidth, defaultGameHeight);
			canvas.setPreferredSize(window);
	        canvas.setMinimumSize(window);
	        canvas.setMaximumSize(window);
			gd.setFullScreenWindow(null);
		}
		
		// pack
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		
		// buffer
		canvas.createBufferStrategy(2);
	}	
	
	/**
	 * Toggle fullscreen mode
	 */
	public void switchFullScreen() { fullScreen(!this.fullScreen); }
	
	/**
	 * @return True if application is currently set in fullscreen mode.
	 */
	public boolean isFullScreen() { return this.fullScreen; }
	
	/**
	 * Switch to/from fullscreen mode
	 * @param fullScreen
	 */
	public void fullScreen(boolean fullScreen) {
		if(this.fullScreen == fullScreen) return;
		this.fullScreen = fullScreen;
		createGUI();
	}
	
	/**
	 * Perform game state logic
	 */
	public void tick() {
		gameState.tick();
	}
	
	/**
	 * Render the game state's graphics
	 */
	public void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		bs.getDrawGraphics();
		gameState.render(bs.getDrawGraphics());
		bs.show();
	}

	/**
	 * Change the game state
	 * @param nextState Game state to change to
	 * @param push Push the current game state onto the game state stack?
	 * (allowing {@link Game#back()} to come back to current state)
	 */
	public void setGameState(GameState nextState, boolean push) {
		if(this.gameState != null)  {
			if(push){
				this.gameState.sleep();
				gameStateStack.push(this.gameState);
			} else {
				this.gameState.dispose();
			}
		}
		
		nextState.setGame(this);
		this.gameState = nextState;
	}
	
	/**
	 * Return to the previous game state.
	 * @throws java.lang.IllegalStateException When there is no previous state.
	 */
	public void back() {
		if(!gameStateStack.isEmpty()) {
			this.gameState.dispose();
			this.gameState = gameStateStack.pop();
			this.gameState.wake();
		} else {
			throw new java.lang.IllegalStateException("No previous state was found");
		}
	}

	/**
	 * This method is called as the JFrame is being closed.
	 * It is abstracted here because Mac does not fire the windowStateChanged event for closing.
	 */
	private void exit() {
		if(frame != null) frame.dispose();
		running = false;
	}
	
	@Override
	public void windowStateChanged(WindowEvent e) {
		if(e.getID() == WindowEvent.WINDOW_CLOSING) exit();
	}
}
