package com.benderapps.engy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.FPSLogger;
import com.benderapps.engy.screens.GameScreen;
import com.benderapps.engy.screens.MainMenuScreen;

public class EngyGame extends Game {
	// constant useful for logging
	public static final String LOG = EngyGame.class.getSimpleName();
	// a libgdx helper class that logs the current FPS each second
	private FPSLogger fpsLogger;
	
	// for google play game services
	public ActionResolver actionResolver;
	// for interestial ads
	public AdInterface adInterface;
	
	// public InputMultiplexer to be able to handle InputsProcessors
	public InputMultiplexer inputMultiplexer;
	
	public static int SCREEN_CURRENT;
	public final static int SCREEN_MAINMENU = 0;
	public final static int SCREEN_GAME = 1;
	public final static int SCREEN_OPTIONS = 2;
	public final static int SCREEN_LEVELS = 3;

	public EngyGame(ActionResolver actionResolver, AdInterface adInterface) {
		this.actionResolver = actionResolver;
		this.adInterface = adInterface;
	}
	
	public ActionResolver getActionResolver() throws NullPointerException {
		if (actionResolver != null)
			return actionResolver;
		throw new NullPointerException("actionResolver is null");
	}
	
	public AdInterface getAdInterface() throws NullPointerException {
		if (adInterface != null)
			return adInterface;
		throw new NullPointerException("AdInterface is null");
	}

	@Override
	public void create() {
		Gdx.app.log(EngyGame.LOG, "Creating game");
		fpsLogger = new FPSLogger();

		// avoid memory leaks, only calls render one time, and every time an
		// inputevent is triggered.
		// don't let animations work, like tween engine
//		Gdx.graphics.setContinuousRendering(false);
//		Gdx.graphics.requestRendering();

		// to handle multiple inputProcessor
		Gdx.input.setCatchBackKey(true);
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new InputAdapter() {
			@Override
			public boolean keyUp(final int keycode) {
				if (keycode == Keys.BACK) {
					switch (EngyGame.SCREEN_CURRENT) {
					case SCREEN_MAINMENU:
						Gdx.app.log("MAINMENUSCREEN", "Exit App");
						Gdx.app.exit();
						break;
					case SCREEN_GAME:
						Gdx.app.log("GAMESCREEN", "Go to PauseScreen");
						GameScreen.pause.setVisible(true);
//						setScreen(new MainMenuScreen(EngyGame.this));
						break;
					case SCREEN_OPTIONS:
						Gdx.app.log("OPTIONSSCREEN", "Go to MainMenuScreen");
						setScreen(new MainMenuScreen(EngyGame.this));
						break;
					case SCREEN_LEVELS:
						Gdx.app.log("LEVELSSCREEN", "Go to MainMenuScreen");
						setScreen(new MainMenuScreen(EngyGame.this));
						break;
					default:
						Gdx.app.log("UNKNOWNSCREEN", "Go to MainMenuScreen");
						setScreen(new MainMenuScreen(EngyGame.this));
						break;
					}
				}
				return false;
			}
		});
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		// set first Screen
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void resize(int width, int height) {
		// only for screens that can be resized (DESKTOP APPS)
		Gdx.app.log(EngyGame.LOG, "Resizing game to: " + width + " x " + height);
	}

	@Override
	public void render() {
		super.render();

		// output the current FPS
		fpsLogger.log();
	}

	@Override
	public void pause() {
		Gdx.app.log(EngyGame.LOG, "Pausing game");
	}

	@Override
	public void resume() {
		Gdx.app.log(EngyGame.LOG, "Resuming game");
	}

	@Override
	public void dispose() {
		Gdx.app.log(EngyGame.LOG, "Disposing game");
	}

}
