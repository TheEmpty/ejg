package com.reliablerabbit.ejg;

import java.awt.Graphics;

/**
 * State management for games using {@link Game}
 * @author Mohammad El-Abid
 *
 */
public abstract class GameState {
	/**
	 * The Game which is running this state.
	 */
	protected Game game;
	
	/**
	 * Set the game object. Used by the Game
	 * object when setting the state, in case
	 * a default constructor was used.
	 * @param game
	 */
	protected void setGame(Game game) { this.game = game; }

	/**
	 * Creates a game state for the supplied game.
	 * @param game Game object that spawned this game state.
	 */
	public GameState(Game game) { setGame(game); }
	
	/**
	 * This method should draw any graphics onto the supplied object.
	 * @param g Graphics object of the game canvas.
	 */
	public abstract void render(Graphics g);
	
	/**
	 * This method should contain logic for this state.
	 */
	public abstract void tick();
	
	/**
	 * This method is called when the game state is losing foreground and not being pushed into the stack.
	 */
	public abstract void dispose();
	
	/**
	 * This method is called when the game state is being pushed into the stack.
	 */
	public abstract void sleep();
	
	/**
	 * This method is called when the game state is in the foreground.
	 */
	public abstract void wake();
}
