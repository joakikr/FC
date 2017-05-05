package com.flappy.chu.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Pikachu extends GameObject {
	
	private static final float FALL_ACCELERATION = 0.30f;
	private static final float JUMP_ACCELERATION = 6f;
	private static final float COLLISON_RADIUS = 26f;
	private static final float THUNDERBOLT_ANIMATION_TIME = 2.5f;
	
	private final Circle collisionCircle;
	private float ySpeed = 0;

	private Animation animation;
	private Animation thunderbolt;
	private float animationTimer = 0;
	private float thunderboltTimer = 0;
	
	private int numThunderbolts;
	
	public enum PIKACHU_STATE {
		FREE, CAUGHT, THUNDERBOLT
	}
	
	public PIKACHU_STATE state;
	
	public Pikachu(Animation pikachuAnimation, Animation thunderboltAnimation) {
		animation = pikachuAnimation;
		animation.setPlayMode(Animation.PlayMode.LOOP);
		thunderbolt = thunderboltAnimation;
		thunderbolt.setPlayMode(Animation.PlayMode.LOOP);
		collisionCircle = new Circle(x, y, COLLISON_RADIUS);
	}
	
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		collisionCircle.setPosition(x, y);
	}
	
	public void update(float delta) {
		// Update animationTimer, % 4 to avoid overflow
		animationTimer = (animationTimer + delta) % 4;
		ySpeed -= FALL_ACCELERATION;
		
		switch(state) {
		case FREE:
			setPosition(x, y + ySpeed);
			break;
		case THUNDERBOLT:
			thunderboltTimer += delta;
			setPosition(x, y + ySpeed);
			break;
		case CAUGHT:
			// Only draw fall until Pikachu is out of screen
			thunderboltTimer = 0;
			if(y + 2 * collisionCircle.radius > 0) {
				setPosition(x, y + ySpeed);
			}
			break;
		}
	}
	
	public void draw(SpriteBatch batch) {
		TextureRegion pikachuTexture = null;
		
		switch(state) {
		case FREE:
			pikachuTexture = animation.getKeyFrame(animationTimer);
			break;
		case THUNDERBOLT:
			pikachuTexture = animation.getKeyFrame(animationTimer);
			batch.draw(thunderbolt.getKeyFrame(animationTimer), 0, 0);
			
			// Play thunderbolt animation for 'THUDERBOLT_ANIMATION_TIME' seconds.
			if(thunderboltTimer > THUNDERBOLT_ANIMATION_TIME) {
				thunderboltTimer = 0;
				state = PIKACHU_STATE.FREE;
			}
			
			break;
		case CAUGHT:
			pikachuTexture = animation.getKeyFrames()[3];
			break;
		}
		
		float textureX = collisionCircle.x - (4 * pikachuTexture.getRegionWidth() / 5);
		float textureY = collisionCircle.y - pikachuTexture.getRegionHeight() / 2;
		batch.draw(pikachuTexture, textureX, textureY);

	}
	
	public void drawDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
	}
	
	public void jump() {
		ySpeed = JUMP_ACCELERATION;
		setPosition(x, y + ySpeed);
	}
	
	public Circle getCollisionCircle() {
		return collisionCircle;
	}
	
	public void setNumThunderbolts(int numThunderbolts) {
		this.numThunderbolts = numThunderbolts;
	}
	
	public int getNumThunderbolts() {
		return numThunderbolts;
	}
}