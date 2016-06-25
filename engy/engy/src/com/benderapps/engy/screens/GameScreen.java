package com.benderapps.engy.screens;

import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.benderapps.engy.EngyGame;
import com.benderapps.engy.accessor.ImageAccessor;
import com.benderapps.engy.utils.PuzzleSolver;
import com.benderapps.engy.utils.PuzzleSolver.BlockSolver;
import com.benderapps.engy.utils.PuzzleSolver.Direction;
import com.benderapps.engy.utils.PuzzleSolver.Move;
import com.benderapps.engy.world.World;

public class GameScreen extends AbstractScreen {
	enum GameState {
		Ready, Running
	}

	GameState state = GameState.Ready;
	World world;
	private float oldX;
	private float oldY;
	private PuzzleSolver puzzleSolver;
	private boolean solving = false;
	private ArrayList<ArrayList<BlockSolver>> solution;
	private int solution_step = 0;

	private int moveStartPositionX;
	private int moveStartPositionY;
	private int stepNum = 0;
	private boolean moving_block = false;
	private float playTime = 0.0f;
	private Label totalMoves;
	private Label currentTime;

	public static Window pause;
	private Window loading;
	private Window levelComplete;

	public GameScreen(EngyGame game, int level) {
		super(game);
		EngyGame.SCREEN_CURRENT = EngyGame.SCREEN_GAME;
		world = new World(getAtlas(), level);
		solving = false;
	}

	@Override
	public void show() {
		super.show();

		Tween.registerAccessor(Image.class, new ImageAccessor());

		// set background
		Texture background = new Texture(Gdx.files.internal("back.png"));
		final Image backImage = new Image(background);
		backImage.setFillParent(true);
		stage.addActor(backImage);

		stage.addActor(world.blocks);

		stage.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (solving && solution != null) {
					if (solution_step < solution.size() - 1) {
						Move movement = null;

						ArrayList<BlockSolver> step = solution
								.get(solution_step);
						ArrayList<BlockSolver> next_step = solution
								.get(solution_step + 1);
						BlockSolver block1 = null;
						for (int i = 0; i < step.size(); i++) {
							block1 = step.get(i);
							BlockSolver block2 = next_step.get(i);
							if (block1.x != block2.x) {
								if (block1.x - block2.x < 0) {
									movement = puzzleSolver.new Move(block1.id,
											Direction.right, block2.x
													- block1.x);
								} else {
									movement = puzzleSolver.new Move(block1.id,
											Direction.left, block1.x - block2.x);
								}
								world.current = world.blockInXY(World.marginX
										+ block1.x * World.BLOCK_WEIGHT
										+ World.BLOCK_WEIGHT / 2, World.marginY
										+ block1.y * World.BLOCK_WEIGHT
										+ World.BLOCK_WEIGHT / 2);
								break;
							} else if (block1.y != block2.y) {
								if (block1.y - block2.y < 0) {
									movement = puzzleSolver.new Move(block1.id,
											Direction.up, block2.y - block1.y);
								} else {
									movement = puzzleSolver.new Move(block1.id,
											Direction.down, block1.y - block2.y);
								}
								world.current = world.blockInXY(World.marginX
										+ block1.x * World.BLOCK_WEIGHT
										+ World.BLOCK_WEIGHT / 2, World.marginY
										+ block1.y * World.BLOCK_WEIGHT
										+ World.BLOCK_WEIGHT / 2);
								break;
							}
						}
						// animate block with movement
						if (movement != null && world.current != null
								&& block1 != null) {
							switch (movement.move) {
							case down:
								world.moveCurrentTo(block1.x
										* World.BLOCK_WEIGHT, block1.y
										* World.BLOCK_WEIGHT, block1.x
										* World.BLOCK_WEIGHT, block1.y
										* World.BLOCK_WEIGHT
										- movement.distance
										* World.BLOCK_WEIGHT);
								break;
							case left:
								world.moveCurrentTo(block1.x
										* World.BLOCK_WEIGHT, block1.y
										* World.BLOCK_WEIGHT, block1.x
										* World.BLOCK_WEIGHT
										- movement.distance
										* World.BLOCK_WEIGHT, block1.y
										* World.BLOCK_WEIGHT);
								break;
							case right:
								world.moveCurrentTo(block1.x
										* World.BLOCK_WEIGHT, block1.y
										* World.BLOCK_WEIGHT, block1.x
										* World.BLOCK_WEIGHT
										+ movement.distance
										* World.BLOCK_WEIGHT, block1.y
										* World.BLOCK_WEIGHT);
								break;
							case up:
								world.moveCurrentTo(block1.x
										* World.BLOCK_WEIGHT, block1.y
										* World.BLOCK_WEIGHT, block1.x
										* World.BLOCK_WEIGHT, block1.y
										* World.BLOCK_WEIGHT
										+ movement.distance
										* World.BLOCK_WEIGHT);
								break;
							}
						}
						solution_step++;
						return true;
					} else {
						solving = false;
					}
				}
				if (pointer == 0) {
					world.current = world.blockInXY(x, y);
					if (world.current != null) {
						moveStartPositionX = world.current.getFieldPosition()
								.get(0)[0];
						moveStartPositionY = world.current.getFieldPosition()
								.get(0)[1];
					}
					oldX = x;
					oldY = y;
					return true;
				}
				return false;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pointer == 0 && !solving) {
					if (!world.level_complete) {
						world.moveToNonCollisionPosition();
						if (moving_block
								&& world.current != null
								&& (world.current.getFieldPosition().get(0)[0] != moveStartPositionX || world.current
										.getFieldPosition().get(0)[1] != moveStartPositionY)) {
							stepNum++;
						}
					}
				}
				moving_block = false;
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				super.touchDragged(event, x, y, pointer);
				if (pointer == 0 && !solving) {
					if (world.current != null) {
						world.moveCurrentTo(oldX, oldY, x, y);
						oldX = x;
						oldY = y;
						moving_block = true;
					}
				}
			}
		});
		createButtons();
		createStats();
		createPauseScreen();
		createLevelCompleteScreen();
		createLoadingScreen();

		puzzleSolver = new PuzzleSolver(world);
	}

	private void createButtons() {
		Table table = new Table();
		// table.debug();

		TextButton pauseButton = new TextButton("PAUSE", getSkin());
		pauseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pause.setVisible(true);
			}
		});
		pauseButton.setSize(100, 50);

		TextButton helpButton = new TextButton("HELP", getSkin());
		helpButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				solving = true;
				solution_step = 0;
				loading.setVisible(true);
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						solution = puzzleSolver.solvePuzzle();
						loading.setVisible(false);
					}
				});
			}
		});
		helpButton.setSize(100, 50);

		TextButton backButton = new TextButton("BACK", getSkin());
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new MainMenuScreen(game));
			}
		});
		backButton.setSize(100, 50);

		// table.setTransform(true);
		table.padLeft(50);
		table.setHeight(stage.getHeight());
		table.add(pauseButton).expandY();
		table.row();
		table.add(helpButton).expandY();
		table.row();
		table.add(backButton).expandY();
		table.left();
		stage.addActor(table);
	}

	private void createStats() {
		Table table = new Table();
		// table.debug();

		LabelStyle headingStyle = new LabelStyle(getFont(), Color.WHITE);
		Label level = new Label(String.valueOf(world.current_level),
				headingStyle);

		LabelStyle movesStyle = new LabelStyle(getFont(), Color.BLACK);
		Label moves = new Label("Moves", movesStyle);
		totalMoves = new Label("0", movesStyle);
		Label time = new Label("Time", movesStyle);
		currentTime = new Label("0", movesStyle);

		table.setHeight(stage.getHeight());
		table.setWidth(stage.getWidth());
		table.top();
		table.right();
		level.setAlignment(Align.center);
		table.add(level).width(170).height(170);
		table.row();
		table.add(moves).width(170).padTop(100);
		table.row();
		table.add(totalMoves);
		table.row();
		table.add(time).width(170);
		table.row();
		table.add(currentTime);

		stage.addActor(table);
	}

	private void createPauseScreen() {
		// independent pause menu
		pause = new Window("PAUSE", getSkin());

		TextButton resumeButton = new TextButton("resume", getSkin());
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pause.setVisible(false);
			}
		});
		TextButton restartButton = new TextButton("restart", getSkin());
		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pause.setVisible(false);
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new GameScreen(game, world.current_level));
			}
		});
		TextButton puzzlesButton = new TextButton("puzzles", getSkin());
		puzzlesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pause.setVisible(false);
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new LevelsScreen(game));
			}
		});
		TextButton exitButton = new TextButton("exit", getSkin());
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pause.setVisible(false);
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new MainMenuScreen(game));
			}
		});

		pause.add(resumeButton).row();
		pause.add(restartButton).row();
		pause.add(puzzlesButton).row();
		pause.add(exitButton);
		pause.setSize(stage.getWidth() / 1.5f, stage.getHeight() / 1.5f);
		pause.setPosition(stage.getWidth() / 2 - pause.getWidth() / 2,
				stage.getHeight() / 2 - pause.getHeight() / 2);
		pause.setMovable(false);
		pause.setVisible(false);
		stage.addActor(pause);
	}

	private void createLevelCompleteScreen() {
		levelComplete = new Window("LEVEL COMPLETE", getSkin());
		levelComplete
				.setSize(stage.getWidth() / 1.5f, stage.getHeight() / 1.5f);
		levelComplete.setPosition(
				stage.getWidth() / 2 - levelComplete.getWidth() / 2,
				stage.getHeight() / 2 - levelComplete.getHeight() / 2);
		levelComplete.setMovable(false);
		levelComplete.setVisible(false);
		stage.addActor(levelComplete);
	}

	private void createLoadingScreen() {
		loading = new Window("Solving...", getSkin());
		loading.setSize(stage.getWidth() / 1.5f, stage.getHeight() / 1.5f);
		loading.setPosition(stage.getWidth() / 2 - loading.getWidth() / 2,
				stage.getHeight() / 2 - loading.getHeight() / 2);
		loading.setMovable(false);
		loading.setVisible(false);
		stage.addActor(loading);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		// Table.drawDebug(stage);

		if (state == GameState.Ready)
			updateReady();
		if (state == GameState.Running)
			updateRunning(delta);
	}

	private void updateReady() {
		// al empezar la partida mirar si queremos hacer alguna cosa y despues
		// empezamos
		state = GameState.Running;
	}

	private void updateRunning(float delta) {
		if (world.level_complete) {
			// make animation of the red block

			levelComplete.setVisible(true);
			world.current_level = world.current_level++;
			world.level_complete = false;
		} else {
			totalMoves.setText(String.valueOf(stepNum));
			playTime += delta;
			currentTime.setText(String.valueOf((int) playTime));
		}

		if (solving && solution != null) {
			// make animation next block to move to solve
		}

	}

}
