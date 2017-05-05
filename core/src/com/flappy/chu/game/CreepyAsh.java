package com.flappy.chu.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CreepyAsh {

	private static final float PEEK_MOVEMENT = 1.3f;
	
	private float x = 0;
	private float y = 0;
	
	private TextureRegion texture;
	
	public CreepyAsh(TextureRegion texture) {
		this.texture = texture;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void update(float delta) {
		if(y < 0) {
			setPosition(x, y + PEEK_MOVEMENT);
		}
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, x, y);
	}
	
	public void drawDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(x, y, texture.getRegionWidth(), texture.getRegionHeight());
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
