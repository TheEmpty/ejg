package com.reliablerabbit.ejg.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import com.reliablerabbit.ejg.Art;
import com.reliablerabbit.ejg.Game;
import com.reliablerabbit.ejg.GameState;
import com.reliablerabbit.ejg.Sound;

public class GameLogic extends GameState implements KeyListener {
	public final int NANOSECONDS_IN_A_SECOND = 1000000000;
	
	BufferedImage title;
	Art character, fountain;
	int chrX, chrY, animation, animationAdd, row, xVel, yVel, speed;
	long lastFrameUpdate = System.nanoTime();
	int playerFPS = 3;
	
	int fountainFPS = 3;
	int fountainAnimation = 0;
	long lastFountainUpdate = System.nanoTime();
	
	private Sound stepSound = new Sound("/sounds/step.wav", false);
	private Sound fountainSound = new Sound("/sounds/fountain.wav", true);
	
	public GameLogic(Game game) {
		super(game);
		
		chrX = (game.getGameWidth() - 24) / 2;
		chrY = (game.getGameHeight() - 32) / 2;
		animation = animationAdd = 1;
		row = 2;
		xVel = yVel = 0;
		speed = 8;
		setupKeyboard();
		
		character = new Art("/character.png");
		fountain = new Art("/fountain.png");
		
		if(!character.isLoaded() || !fountain.isLoaded()) { System.exit(3); }
		
		character.generateClips(24, 32);
		fountain.generateClips(96, 96);
		
		Sound.setListenerPosition(chrX, chrY);
		fountainSound.play(25, 25);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
	    g.fillRect(0, 0, game.getFrame().getWidth(), game.getFrame().getHeight());
	    
		g.setColor(java.awt.Color.red);
		g.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16));
		g.drawString("Press escape to return to main menu", 3, 16);
		g.drawImage(fountain.getClip(fountainAnimation), 25, 25, null);
		g.drawImage(character.getClip((row * 3) + animation), chrX, chrY, null);
	}
	
	public void tick() {
		if((System.nanoTime() - lastFountainUpdate) >= NANOSECONDS_IN_A_SECOND / fountainFPS) {
			lastFountainUpdate = System.nanoTime();
			fountainAnimation = (fountainAnimation + 1) % (fountain.clipSize() - 1);
		}
		
		if((xVel != 0 || yVel != 0) && (System.nanoTime() - lastFrameUpdate) >= NANOSECONDS_IN_A_SECOND / playerFPS) {
			lastFrameUpdate = System.nanoTime();
			
			if(xVel != 0 || yVel != 0) {
				// position
				animation += animationAdd;
				
				if(animation >= 2) {
					animationAdd = -1;
				} else if(animation == 0) {
					animationAdd = 1;
				}
				
				Sound.setListenerPosition(chrX, chrY);
				stepSound.play(chrX, chrY);
			}
			
			chrX += xVel;
			chrY += yVel;
			
			if(chrX < 0) chrX = 0;
			else if(chrX + 24 > game.getGameWidth()) chrX -= xVel;
			
			if(chrY < 0) chrY = 0;
			else if(chrY + 32 > game.getGameHeight()) chrY -= yVel;
		}		
	}

	@Override
	public void dispose() {
		// It's being deleted
		fountainSound.release();
		stepSound.release();
	}

	@Override
	public void sleep() {
		// It's getting hidden
		destroyKeyboard();
		fountainSound.stop();
	}
	
	@Override
	public void wake() {
		// It's getting ready to be shown
		setupKeyboard();
		fountainSound.play(25, 25);
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

	@Override
	public void keyPressed(KeyEvent ke) {
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			row = 0;
			xVel = 0;
			yVel = -speed;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			row = 1;
			xVel = speed;
			yVel = 0;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			row = 2;
			xVel = 0;
			yVel = speed;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			row = 3;
			xVel = -speed;
			yVel = 0;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			xVel = yVel = 0;
			break;
		case KeyEvent.VK_ESCAPE:
			game.back();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}
}
