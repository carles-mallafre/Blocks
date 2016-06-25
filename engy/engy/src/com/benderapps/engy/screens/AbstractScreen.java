package com.benderapps.engy.screens;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.benderapps.engy.EngyGame;
import com.benderapps.engy.utils.Constants;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen {
	// the fixed viewport dimensions (ratio: 1.6)
	public static final int GAME_VIEWPORT_WIDTH = Constants.SCREEN_WIDTH,
			GAME_VIEWPORT_HEIGHT = Constants.SCREEN_HEIGHT;
	// public static final int GAME_VIEWPORT_WIDTH = Gdx.graphics.getWidth(),
	// GAME_VIEWPORT_HEIGHT = Gdx.graphics.getHeight();

	protected final EngyGame game;
	protected final Stage stage;

	private BitmapFont font;
	private SpriteBatch batch;
	private Skin skin;
	private TextureAtlas atlas;
	private Table table;
	private TweenManager tweenManager;

	public AbstractScreen(EngyGame game) {
		this.game = game;
		int width = GAME_VIEWPORT_WIDTH;
		int height = GAME_VIEWPORT_HEIGHT;

		// this.stage = new Stage(Gdx.graphics.getWidth() / game.screen_density,
		// Gdx.graphics.getHeight() / game.screen_density, false);

		this.stage = new Stage(width, height, false);
	}

	protected String getName() {
		return getClass().getSimpleName();
	}

	protected boolean isGameScreen() {
		return false;
	}

	// Lazily loaded collaborators

	public BitmapFont getFont() {
		if (font == null) {
			font = new BitmapFont(Gdx.files.internal("skin/copperplate.fnt"),
					false);
		}
		return font;
	}

	public SpriteBatch getBatch() {
		if (batch == null) {
			batch = new SpriteBatch();
		}
		return batch;
	}

	public TextureAtlas getAtlas() {
		if (atlas == null) {
			atlas = new TextureAtlas(
					Gdx.files.internal("image-atlases-packed/blocks.atlas"));
		}
		return atlas;
	}

	protected Skin getSkin() {
		if (skin == null) {
			FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
			skin = new Skin(skinFile);
			TextureAtlas extra_atlas = new TextureAtlas(
					Gdx.files.internal("image-atlases-packed/extras.atlas"));
			skin.addRegions(extra_atlas);
		}
		return skin;
	}

	protected Table getTable() {
		if (table == null) {
			table = new Table(getSkin());
			table.setFillParent(true);
			// table.debug();
			stage.addActor(table);
		}
		return table;
	}

	public TweenManager getTweenManager() {
		if (tweenManager == null) {
			tweenManager = new TweenManager();
		}
		return tweenManager;
	}

	// Screen implementation

	@Override
	public void show() {

		// add the stage as the input processor number 0
		game.inputMultiplexer.addProcessor(0, stage);
		Gdx.input.setInputProcessor(game.inputMultiplexer);

		// Texture background = new Texture(Gdx.files.internal("back.jpg"));
		// Image backImage = new Image(background);
		// backImage.setFillParent(true);
		// stage.addActor(backImage);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT, false);
	}

	@Override
	public void render(float delta) {
		// (1) process the game logic

		// update the actors
		stage.act(delta);

		// (2) draw the result

		// clear the screen with the given RGB color (black)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw the actors
		stage.draw();
		
		getTweenManager().update(delta);
	}

	@Override
	public void hide() {

		// dispose the screen when leaving the screen;
		// note that the dipose() method is not called automatically by the
		// framework, so we must figure out when it's appropriate to call it
		dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

		// the following call disposes the screen's stage, but on my computer it
		// crashes the game so I commented it out; more info can be found at:
		// http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=3624
		// stage.dispose();

		// as the collaborators are lazily loaded, they may be null
		if (font != null)
			font.dispose();
		if (batch != null)
			batch.dispose();
		if (skin != null)
			skin.dispose();
		if (atlas != null)
			atlas.dispose();
		if (stage != null)
			stage.dispose();
	}
}
