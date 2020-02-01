package com.spaceinvaders.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import states.*;

public class SpaceInvaders extends ApplicationAdapter {

	public static final int WIDTH = 620;
	public static final int HEIGHT = 620;
	public static final int SCALE = 2;
	public static final String TITLE = "Space Invaders";

	private GameStateManager gsm;
	private SpriteBatch sb;

	@Override
	public void create () {
		sb = new SpriteBatch();
		gsm = new states.GameStateManager();
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(sb);
	}
}