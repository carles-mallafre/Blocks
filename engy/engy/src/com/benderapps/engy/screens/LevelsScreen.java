package com.benderapps.engy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.benderapps.engy.AdInterface.InterstitialCallback;
import com.benderapps.engy.EngyGame;
import com.benderapps.engy.actors.PagedScrollPane;

public class LevelsScreen extends AbstractScreen implements Screen {

	public LevelsScreen(EngyGame game) {
		super(game);
		EngyGame.SCREEN_CURRENT = EngyGame.SCREEN_LEVELS;
	}

	@Override
	public void show() {
		super.show();

		Table table = getTable();

		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25);
		int c = 1;
		for (int l = 0; l < 3; l++) {
			Table levels = new Table().pad(20, 50, 20, 50);
			levels.defaults().pad(20, 40, 20, 40);
			for (int y = 0; y < 3; y++) {
				levels.row();
				for (int x = 0; x < 4; x++) {
					levels.add(getLevelButton(c++)).expand().fill();
				}
			}
			scroll.addPage(levels);
		}

		TextButton backButton = new TextButton("BACK", getSkin());
		backButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new MainMenuScreen(game));
			}
		});

		table.add(scroll).expand().fill();
		table.row();
		table.add(backButton);
	}

	/**
	 * Creates a button to represent the level
	 * 
	 * @param level
	 * @return The button to use for the level
	 */
	public Button getLevelButton(final int level) {
		Button button = new Button(getSkin());
		ButtonStyle style = button.getStyle();
		style.up = style.down = null;

		// Create the label to show the level number
		LabelStyle headingStyle = new LabelStyle(getFont(), Color.WHITE);
		Label label = new Label(Integer.toString(level), headingStyle);
		// label.setFontScale(1);

		// Stack the image and the label at the top of our button
		button.stack(new Image(getSkin().getDrawable("top")), label).expand()
				.fill();

		// Randomize the number of stars earned for demonstration purposes
		int stars = MathUtils.random(0, +3);
		Table starTable = new Table();
		starTable.defaults().pad(5);
		if (stars >= 0) {
			for (int star = 0; star < 3; star++) {
				if (stars > star) {
					starTable
							.add(new Image(getSkin().getDrawable("star-filled")))
							.width(20).height(20);
				} else {
					starTable
							.add(new Image(getSkin().getDrawable(
									"star-unfilled"))).width(20).height(20);
				}
			}
		}

		button.row();
		button.add(starTable).height(30);

		button.setName("Level" + Integer.toString(level));
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				try {
					game.getAdInterface().showInterstitial(
							new InterstitialCallback() {
								@Override
								public void onDismissScreen() {
//									((Game) Gdx.app.getApplicationListener())
//											.setScreen(new GameScreen(game,
//													level));
								}
							});
					((Game) Gdx.app.getApplicationListener())
							.setScreen(new GameScreen(game, level));
				} catch (NullPointerException e) {
					((Game) Gdx.app.getApplicationListener())
							.setScreen(new GameScreen(game, level));
				}
			}
		});
		// button.addListener(new InputListener() {
		// @Override
		// public boolean touchDown(InputEvent event, float x, float y,
		// int pointer, int button) {
		//
		// return true;
		// }
		//
		// @Override
		// public void touchUp(InputEvent event, float x, float y,
		// int pointer, int button) {
		// super.touchUp(event, x, y, pointer, button);
		// ((Game) Gdx.app.getApplicationListener())
		// .setScreen(new GameScreen(game, level));
		// }
		// });
		return button;
	}
}
