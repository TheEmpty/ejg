ejg (Easy Java Games)
=====================

A simplistic game engine for Java. It evolves around state driven design allowing for modular code (title logic and game logic are completly different). It also supports 3D sound by use of "Paul's Code."

Documentation
-------------

All methods and variables should have JavaDoc. The JavaDoc will be available online soon, but for now refer to `/docs` or the JavaDoc above the identifer.

Entry Point
-----------

The most basic entry point would look something like this:

```
public class Application {
	public static void main(String[] args) {			
		// Load the sound before the user sees anything
		Sound.load();
		
		// Setup GUI
		Game game = new Game();
		game.setDefaultGameWidth(800);
		game.setDefaultGameHeight(600);
		game.createGUI();
		game.getFrame().setTitle("My Awesome Game");
		game.getFrame().setVisible(true); // display the frame on the user's screen
		
		game.setGameState(new TitleState(game), false);
		
		// quit flag
		boolean running = true;
		game.running = running;
		
		while(running) {
			// Logic
			game.tick();
			
			// Graphics
			game.render();
		}

		// if you were in fullscreen mode, reset to default resolution
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow( null );
	}
}
```

Game State
----------

Here is an example of my "bare-bone" `GameState` with some comments.

```
public class Title extends GameState {

	public Title(Game game) {
		super(game);
		listenToKeys();
	}

	// Logic
	@Override
	public void tick() {
	}

	// Graphics
	@Override
	public void render(Graphics g) {
	}
	
	// Being deleted from RAM
	@Override
	public void dispose() {
		ignoreKeys();
	}

	// Losing focus
	@Override
	public void sleep() {
		ignoreKeys();
	}

	// Gaining focus after sleep
	@Override
	public void wake() {
		listenToKeys();
	}
	
	// Only triggered once for letter/number keys
	// up, down, space, etc. constantly trigger this
	@Override
	public void keyPressed(KeyEvent ke) {
	}
	
	// Triggered when the user lets go of a key
	// might trigger false positives for up, space, etc.
	@Override
	public void keyReleased(KeyEvent ke) {
	}
	
	// "Called just after the user types a Unicode character into the listened-to component."
	@Override
	public void keyTyped(KeyEvent ke) { /* no <3 */ }
	
	private void ignoreKeys() {
		game.getCanvas().removeKeyListener(this);
		game.getFrame().removeKeyListener(this);
	}
	
	private void listenToKeys() {
		game.getCanvas().setFocusTraversalKeysEnabled(false);
        game.getCanvas().addKeyListener(this);
        game.getFrame().setFocusTraversalKeysEnabled(false);
        game.getFrame().addKeyListener(this);
	}
}
```

Insperation
-----------

This project was inspired by [Mojam](http://kotaku.com/5886383/mojam-raises-440000-but-notchs-beard-appears-to-be-safe) by Mojang.

Teams
-----

I am willing to join any indie designers or teams. I lack the art skills require to make my own game and can't find anyone that has art skills in my area. Contact me via e-mail: `"mohammad!!!el-abid.com".gsub("!!!","@")`