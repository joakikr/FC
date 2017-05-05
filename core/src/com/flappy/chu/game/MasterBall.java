package com.flappy.chu.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class MasterBall extends Ball {

	public static float CHANCE = 0.05f;

	private static final float BETWEEN_DISTANCE = 280f;
	private static final float MASTER_BALL_Y_SPEED = 400f;
	private static final float MASTER_BALL_MOVEMENT_SPEN = 100f;
	private static final int MASTER_BALL_POINTS = 10;
	
	private float ySpeed = 0;
	private float movement = 0;
	
	public MasterBall(TextureRegion floorTexture, TextureRegion ceilingTexture) {
		super(floorTexture, ceilingTexture, BETWEEN_DISTANCE);
		ySpeed = MathUtils.randomSign() * MASTER_BALL_Y_SPEED;
		points = MASTER_BALL_POINTS;
	}

	@Override
	public void update(float delta) {
		movement += ySpeed * delta;
		setPosition(x - (xSpeed * delta), y + (ySpeed * delta));
		
		if(movement > MASTER_BALL_MOVEMENT_SPEN) {
			ySpeed = -MASTER_BALL_Y_SPEED;
		} else if(movement < -MASTER_BALL_MOVEMENT_SPEN) {
			ySpeed = MASTER_BALL_Y_SPEED;
		}		
	}
	
	@Override
	public boolean givesExtraThunderbolt() {
		return true;
	}
	
}
