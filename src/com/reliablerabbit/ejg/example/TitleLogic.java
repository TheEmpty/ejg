package com.reliablerabbit.ejg.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.reliablerabbit.ejg.Game;
import com.reliablerabbit.ejg.GameState;

public class TitleLogic extends GameState implements KeyListener {

	public TitleLogic(Game game) {
		super(game);
		setupKeyboard();
	}

	@Override
	public void keyPressed(KeyEvent ke) {
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		game.setGameState(new GameLogic(game), true);
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
	    g.fillRect(0, 0, game.getFrame().getWidth(), game.getFrame().getHeight());
	    
		g.setColor(java.awt.Color.magenta);
		g.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 28));
		g.drawString("Testing Application", 250, 200);
		
		g.setColor(java.awt.Color.red);
		g.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 24));
		g.drawString("Press any key to start", 255, 230);
	}

	@Override
	public void tick() {
	}

	@Override
	public void dispose() {
		destroyKeyboard();
	}

	@Override
	public void sleep() {
		destroyKeyboard();
	}

	@Override
	public void wake() {
		setupKeyboard();
	}
	
	private void destroyKeyboard() {
		game.getCanvas().removeKeyListener(this);
		game.getFrame().removeKeyListener(this);
	}
	
	private void setupKeyboard() {
		game.getCanvas().setFocusTraversalKeysEnabled(false);
        game.getCanvas().addKeyListener(this);
        game.getFrame().setFocusTraversalKeysEnabled(false);
        game.getFrame().addKeyListener(this);
	}

}
