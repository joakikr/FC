package com.flappy.chu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class FlappyChu extends Game {
	
	private AssetManager manager;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		manager = new AssetManager();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
	
	public AssetManager getAssetManager() {
		return manager;
	}
	
	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
}