package com.reliablerabbit.ejg.example;

import java.awt.GraphicsEnvironment;

import com.reliablerabbit.ejg.Game;
import com.reliablerabbit.ejg.Sound;

public class Application {
	public static final int NANOSECONDS_IN_A_MILLISECOND = 1000000;
	public static final int NANOSECONDS_IN_A_SECOND = 1000000000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int targetFramesPerSecond = 60;
		int renderedFrames = 0;
		@SuppressWarnings("unused")
		int currentFramesPerSecond = 0;
		
		long nextRender = System.nanoTime();
		long lastFramesPerSecondUpdate = System.nanoTime();
		
		new Thread() {
			public void run() {
				Sound.load();
			}
		}.start();
		
		// Setup GUI
		Game game = new Game();
		game.setDefaultGameWidth(800);
		game.setDefaultGameHeight(600);
		game.createGUI();
		game.getFrame().setTitle("Testing application"); // need to initalize so frame exist
		game.getFrame().setVisible(true);
		
		// Setup Gamestates (wait until after GUI is created since we attack to canvas and frame for keys)
		game.setGameState(new TitleLogic(game), false);
		
		// quit flag
		boolean running = true;
		game.running = running;
		
		while(running) {
			// Logic
			game.tick();
				
			// FPS cap, skip
			if((System.nanoTime() - nextRender) >= 0) {
				game.render();
				renderedFrames++;
				nextRender = System.nanoTime() + (NANOSECONDS_IN_A_SECOND/targetFramesPerSecond);
			}
				
			// FPS update
			if(System.nanoTime() - lastFramesPerSecondUpdate >= NANOSECONDS_IN_A_SECOND) {
				currentFramesPerSecond = renderedFrames;
				renderedFrames = 0;
				lastFramesPerSecondUpdate = System.nanoTime();
			    // System.out.println("FPS: " + currentFramesPerSecond);
			}
		}
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow( null );
	}
}

//FPS cap, wait
/*
try {
	long difference = (nextRender - System.nanoTime()) / NANOSECONDS_IN_A_MILLISECOND;
	if(difference > 0) {
		Thread.sleep(difference);
	}
} catch(InterruptedException e) {
	e.printStackTrace();
}
game.render();
renderedFrames++;
nextRender = System.nanoTime() + (NANOSECONDS_IN_A_SECOND/targetFramesPerSecond);
*/