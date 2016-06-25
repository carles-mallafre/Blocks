package com.benderapps.engy.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.benderapps.engy.EngyGame;
import com.benderapps.engy.accessor.ActorAccessor;

public class MainMenuScreen extends AbstractScreen {

	private static final String TITLE = "REAL BLOCKS";

	Label heading;
	TextButton startGameButton;
	TextButton achievsButton;
	TextButton leaderboardButton;
	TextButton optionsButton;
	TextButton exitButton;

	Table wrapper_startGameButton;
	Table wrapper_achievsButton;
	Table wrapper_leaderboardButton;
	Table wrapper_options;
	Table wrapper_exitButton;

	public MainMenuScreen(EngyGame game) {
		super(game);
		EngyGame.SCREEN_CURRENT = EngyGame.SCREEN_MAINMENU;
	}

	@Override
	public void show() {
		super.show();
		// registering accessor for animations
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		Table table = getTable();

		wrapper_startGameButton = new Table();
		wrapper_achievsButton = new Table();
		wrapper_leaderboardButton = new Table();
		wrapper_options = new Table();
		wrapper_exitButton = new Table();

		LabelStyle headingStyle = new LabelStyle(getFont(), Color.WHITE);
		heading = new Label(TITLE, headingStyle);
		heading.setFontScale(1.5f);

		startGameButton = new TextButton("New Game", getSkin());
		startGameButton.setColor(startGameButton.getColor().r,
				startGameButton.getColor().g, startGameButton.getColor().b, 0);
		startGameButton.setSize(200, 50);
		wrapper_startGameButton.addActor(startGameButton);
		wrapper_startGameButton.setTransform(true);

		achievsButton = new TextButton("Achievements", getSkin());
		achievsButton.setColor(achievsButton.getColor().r,
				achievsButton.getColor().g, achievsButton.getColor().b, 0);
		achievsButton.setSize(200, 50);
		wrapper_achievsButton.addActor(achievsButton);
		wrapper_achievsButton.setTransform(true);

		leaderboardButton = new TextButton("Leaderboard", getSkin());
		leaderboardButton.setColor(leaderboardButton.getColor().r,
				leaderboardButton.getColor().g, leaderboardButton.getColor().b,
				0);
		leaderboardButton.setSize(200, 50);
		wrapper_leaderboardButton.addActor(leaderboardButton);
		wrapper_leaderboardButton.setTransform(true);

		optionsButton = new TextButton("Options", getSkin());
		optionsButton.setColor(optionsButton.getColor().r,
				optionsButton.getColor().g, optionsButton.getColor().b, 0);
		optionsButton.setSize(200, 50);
		wrapper_options.setTransform(true);
		wrapper_options.addActor(optionsButton);

		exitButton = new TextButton("Exit", getSkin());
		exitButton.setColor(exitButton.getColor().r, exitButton.getColor().g,
				exitButton.getColor().b, 0);
		exitButton.setSize(200, 50);
		wrapper_exitButton.addActor(exitButton);
		wrapper_exitButton.setTransform(true);

		startGameButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Tween.to(wrapper_startGameButton, ActorAccessor.SCALE_XY, .166f)
						.target(1.15f, 1.15f)
						.ease(TweenEquations.easeOutExpo)
						.setCallback(new TweenCallback() {
							public void onEvent(int type, BaseTween<?> source) {
								Tween.to(wrapper_startGameButton,
										ActorAccessor.SCALE_XY, .133f)
										.target(1.05f, 1.05f)
										.ease(TweenEquations.easeNone)
										.repeatYoyo(Tween.INFINITY, 0.05f)
										.start(getTweenManager());
							}
						}).setCallbackTriggers(TweenCallback.COMPLETE)
						.start(getTweenManager());
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

				Tween.to(wrapper_startGameButton, ActorAccessor.SCALE_XY, .166f)
						.target(1f, 1f)
						.ease(TweenEquations.easeOutExpo)
						.setCallback(new TweenCallback() {
							public void onEvent(int type, BaseTween<?> source) {
								try {
									if (!game.getActionResolver()
											.getSignedInGPGS())
										game.getActionResolver().loginGPGS();
								} catch (NullPointerException e) {
								}
								game.setScreen(new LevelsScreen(game));
							}
						}).setCallbackTriggers(TweenCallback.COMPLETE)
						.start(getTweenManager());
			}
		});

		achievsButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

				Tween.to(wrapper_achievsButton, ActorAccessor.SCALE_XY, .1f)
						.target(1f, 0f)
						.ease(TweenEquations.easeOutExpo)
						.ease(TweenEquations.easeOutCubic)
						.repeatYoyo(5, 0)
						.setCallback(new TweenCallback() {
							public void onEvent(int type, BaseTween<?> source) {
								try {
									if (game.getActionResolver()
											.getSignedInGPGS())
										game.getActionResolver()
												.getAchievementsGPGS();
									else
										game.getActionResolver().loginGPGS();
								} catch (NullPointerException e) {
								}
							}
						}).setCallbackTriggers(TweenCallback.COMPLETE)
						.start(getTweenManager());
			}
		});

		leaderboardButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				try {
					if (game.getActionResolver().getSignedInGPGS())
						game.getActionResolver().getLeaderboardGPGS();
					else
						game.getActionResolver().loginGPGS();
				} catch (NullPointerException e) {
				}
			}
		});

		optionsButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Tween.to(wrapper_options, ActorAccessor.SCALE_XY, .166f)
						.target(1.15f, 1.15f)
						.ease(TweenEquations.easeOutExpo)
						.setCallback(new TweenCallback() {
							public void onEvent(int type, BaseTween<?> source) {
								Tween.to(wrapper_options,
										ActorAccessor.SCALE_XY, .133f)
										.target(1.05f, 1.05f)
										.ease(TweenEquations.easeNone)
										.repeatYoyo(Tween.INFINITY, 0.05f)
										.start(getTweenManager());
							}
						}).setCallbackTriggers(TweenCallback.COMPLETE)
						.start(getTweenManager());
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

				Tween.to(wrapper_options, ActorAccessor.SCALE_XY, .166f)
						.target(1f, 1f).ease(TweenEquations.easeOutExpo)
						.setCallback(new TweenCallback() {
							public void onEvent(int type, BaseTween<?> source) {
								game.setScreen(new OptionsScreen(game));
							}
						}).setCallbackTriggers(TweenCallback.COMPLETE)
						.start(getTweenManager());
			}
		});

		exitButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				try {
					if (game.getActionResolver().getSignedInGPGS())
						game.getActionResolver().logout();
				} catch (NullPointerException e) {
				}
				game.dispose();
				dispose();
				Gdx.app.exit();
			}
		});

		// configuring table
		table.add();
		table.add(heading);
		table.getCell(heading).spaceBottom(50);
		table.add();
		table.row();
		table.add();
		table.add(wrapper_startGameButton).width(200).height(50);
		table.add();
		table.row();
		table.add();
		table.add(wrapper_achievsButton).width(200).height(50).padTop(10);
		table.add();
		table.row();
		table.add();
		table.add(wrapper_leaderboardButton).width(200).height(50).padTop(10);
		table.add();
		table.row();
		table.add();
		table.add(wrapper_options).width(200).height(50).padTop(10);
		table.add();
		table.row();
		table.add();
		table.add(wrapper_exitButton).width(200).height(50).padTop(10);
		table.add();

		// buttons fade-in
		Timeline.createSequence()
				.beginParallel()

				.delay(.5f)
				.push(Tween.set(startGameButton, ActorAccessor.ALPHA).target(1))
				.push(Tween.from(startGameButton, ActorAccessor.POS_X, .333f)
						.target(AbstractScreen.GAME_VIEWPORT_WIDTH)
						.ease(TweenEquations.easeOutBack))
				.push(Tween.set(achievsButton, ActorAccessor.ALPHA).target(1))
				.push(Tween.from(achievsButton, ActorAccessor.POS_X, .400f)
						.target(AbstractScreen.GAME_VIEWPORT_WIDTH)
						.ease(TweenEquations.easeOutBack))
				.push(Tween.set(leaderboardButton, ActorAccessor.ALPHA).target(
						1))
				.push(Tween.from(leaderboardButton, ActorAccessor.POS_X, .466f)
						.target(AbstractScreen.GAME_VIEWPORT_WIDTH)
						.ease(TweenEquations.easeOutBack))
				.push(Tween.set(optionsButton, ActorAccessor.ALPHA).target(1))
				.push(Tween.from(optionsButton, ActorAccessor.POS_X, .500f)
						.target(AbstractScreen.GAME_VIEWPORT_WIDTH)
						.ease(TweenEquations.easeOutBack))
				.push(Tween.set(exitButton, ActorAccessor.ALPHA).target(1))
				.push(Tween.from(exitButton, ActorAccessor.POS_X, .533f)
						.target(AbstractScreen.GAME_VIEWPORT_WIDTH)
						.ease(TweenEquations.easeOutBack))

				.end().start(getTweenManager());

	}

	@Override
	public void render(float delta) {
		super.render(delta);
		// Table.drawDebug(stage);
	}
	
}
