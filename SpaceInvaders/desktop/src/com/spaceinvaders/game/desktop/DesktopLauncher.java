package com.spaceinvaders.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.spaceinvaders.game.SpaceInvaders;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SpaceInvaders.WIDTH;
		config.height = SpaceInvaders.HEIGHT;
		config.title = SpaceInvaders.TITLE;
		new LwjglApplication(new SpaceInvaders(), config);
	}
}
