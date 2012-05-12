package com.reliablerabbit.ejg;

import java.util.Set;
import java.util.TreeSet;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;

/**
 * Play 3D sounds in your game simply and easily.
 * @author Mohammad El-Abid
 */
public class Sound {
	private static SoundSystem soundSystem;
	private static Set<String> loaded = new TreeSet<String>(); // is this better then an arraylist?
	
	private String resource;
	private String name;
	
	private static float zCordSounds = 0f;
	
	/**
	 * Create a sound object that does not loop.
	 * @see Sound#Sound(String, boolean)
	 */
	public Sound(String resource) {
		this(resource, false);
	}
	
	/**
	 * Create a sound object to manipulate
	 * @param resource Resource path of sound file
	 * @param loop True if the sound should loop forever
	 */
	public Sound(String resource, boolean loop) {
		if(soundSystem == null) setupSoundSystem();
		this.resource = resource;
		this.name = resource;
		
		int i = 0;
		while(loaded.contains(this.name + i)) i++;
		this.name += i;
		
		soundSystem.newSource(false, this.name, Sound.class.getResource(resource), resource, loop, 0, 0, 0, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff());
		loaded.add(this.name);
	}
	
	/**
	 * Move the 3D "microphone" of where the user is/hears sound relative to. 
	 * @see Sound#setListenerPosition(float, float, float)
	 */
	public static void setListenerPosition(float x, float y) {
		setListenerPosition(x, y, 50f);
	}
	
	/**
	 * Move the 3D "microphone" of where the user is/hears sound relative to.
	 * @param x Any x value, be consistent in how you generate x values here for your play(x, y)
	 * @param y Any y value, be consistent in how you generate y values here for your play(x, y)
	 * @param z Any z value, this mainly plays a role in the volume of your sounds.
	 */
	public static void setListenerPosition(float x, float y, float z) {
        soundSystem.setListenerPosition(x, y, z);
    }
	
	/**
	 * Set the Z coordinate for sounds that are played.
	 * @param newZ New Z coordinate.
	 */
	public static void setZCordSounds(float newZ) {
		Sound.zCordSounds = newZ;
	}
	
	/**
	 * Play a one-off sound, do not loop.
	 * @see Sound#play(String, float, float, boolean)
	 */
	public static void play(String source, float x, float y) {
		play(source, x, y, false);
	}
	
	/**
	 * Play a sound object without instantiating an object.
	 * Useful for bullets or other sounds that may overlap
	 * or do not need to be persisted in RAM.
	 * @param source Resource path of sound file.
	 * @param x X coordinate, relative to the listener position.
	 * @param y Y coordinate, relative to the listener position.
	 * @param loop True if the value should be looped.
	 */
	public static void play(final String source, final float x, final float y, final boolean loop) {
		new Thread() {
			public void run() {
				Sound temp = new Sound(source, loop);
				temp.play(x, y);
				while(temp.isPlaying()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				temp.release();
			}
		}.start();
	}
	
	/**
	 * Play the sound at the x and y coordinates specified. 
	 * @param x X coordinate, relative to the listener position.
	 * @param y Y coordinate, relative to the listener position.
	 */
	public void play(float x, float y) {
		if(soundSystem.playing(resource)) soundSystem.stop(name);
		soundSystem.setPosition(name, x, y, zCordSounds);
		soundSystem.play(name);
	}
	
	/**
	 * Stop the sound, if it is currently playing.
	 */
	public void stop() {
		if(soundSystem.playing(resource)) soundSystem.stop(name);
	}
	
	/**
	 * Release all current sounds. Will cause problems for pre-existing sounds,
	 * but is a way to flush all current sounds.
	 */
	public static void releaseAll() {
		setupSoundSystem();
	}
	
	/**
	 * Remove the sound from RAM, this should be done before deleting a Sound object.
	 * Otherwise the sound will continue to exist in the sound system.
	 */
	public void release() {
		if(soundSystem.playing(resource)) soundSystem.stop(name);
		soundSystem.unloadSound(name);
		loaded.remove(name);
	}
	
	/**
	 * Check if the current sound is playing.
	 */
	public boolean isPlaying() {
		if(soundSystem == null) return false;
		return soundSystem.playing(name);
	}
	
	/**
	 * Setup the sound system, may take a few seconds.
	 * It is automatically loaded when you create a sound
	 * object, but it may be preferable to load it while
	 * the application is starting.
	 */
	public static void load() {
		if(soundSystem == null) setupSoundSystem();
	}
	
	private static void setupSoundSystem() {
		try {
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
        } catch (SoundSystemException ex) {
        }

        try {
            SoundSystemConfig.setCodec("wav", CodecWav.class);
        } catch (SoundSystemException ex) {
        }

        try {
            soundSystem = new SoundSystem(LibraryJavaSound.class);
        } catch (SoundSystemException ex) {
            soundSystem = null;
        }
	}
}
