package com.reliablerabbit.ejg;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Assists games with loading art, primarily sprite sheets.
 * @author Mohammad El-Abid
 */
public class Art {
	private BufferedImage art;
	private ArrayList<String> clips = new ArrayList<String>();
	
	/**
	 * Loads an image (from application resources) for simplified sprite management
	 * @param resource argument that is passed to Art.class.getResource(...)
	 */
	public Art(String resource) {
		try {
			art = ImageIO.read(Art.class.getResource(resource));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate an index of sprite locations
	 * @param width The width of each sprite
	 * @param height The height of each sprite
	 */
	public void generateClips(int width, int height) {
		if(art == null) return;
		
		int currentWidth = 0;
		int currentHeight = 0;
		
		while(currentHeight < art.getHeight()) {
			while(currentWidth < art.getWidth()) {
				clips.add(currentWidth + "," + currentHeight + "," + width + "," + height);
				currentWidth += width;
			}
			currentWidth = 0;
			currentHeight += height;
		}
	}
	
	/**
	 * The number of clips (useful for looping and ensuring you do not go over clip count)
	 */
	public int clipSize() { return this.clips.size(); }
	
	/**
	 * Raw access to the art file
	 */
	public BufferedImage getArt() {
		return art;
	}
	
	/**
	 * Get a sprite from this sprite sheet
	 * @param index Indexes start from zero and go left to right, top to bottom.
	 * @return a subimage containing only the sprite specified
	 */
	public BufferedImage getClip(int index) {
		String[] dims = clips.get(index).split(",");
		return art.getSubimage(Integer.valueOf(dims[0]), Integer.valueOf(dims[1]), Integer.valueOf(dims[2]), Integer.valueOf(dims[3]));
	}
	
	/**
	 * @return Returns true if the image is loaded into RAM.
	 * Operations will crash the program if it is not.
	 */
	public boolean isLoaded() {
		return art != null;
	}
}
