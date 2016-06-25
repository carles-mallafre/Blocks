package com.benderapps.engy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.benderapps.engy.EngyGame;

public class OptionsScreen extends AbstractScreen {

	public OptionsScreen(EngyGame game) {
		super(game);
		EngyGame.SCREEN_CURRENT = EngyGame.SCREEN_OPTIONS;
	}

	@Override
	public void show() {
		super.show();

		Table table = getTable();

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

		table.add(backButton);
	}

}
