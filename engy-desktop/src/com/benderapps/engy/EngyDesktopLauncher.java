package com.benderapps.engy;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class EngyDesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Engy Blocks";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 480;

		// create the game
		new LwjglApplication(new EngyGame(new ActionResolverDesktop(), new AdInterfaceDesktop()), cfg);
	}
}