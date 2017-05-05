package com.flappy.chu.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class UltraBall extends Ball {

	public static float CHANCE = 0.10f;

	private static final float BETWEEN_DISTANCE = 260f;
	private static final float ULTRA_BALL_Y_SPEED = 200f;
	private static final float ULTRA_BALL_MOVEMENT_SPEN = 100f;
	private static final int ULTRA_BALL_POINTS = 5;

	
	private float ySpeed = 0;
	private float movement = 0;
	
	public UltraBall(TextureRegion floorTexture, TextureRegion ceilingTexture) {
		super(floorTexture, ceilingTexture, BETWEEN_DISTANCE);
		ySpeed = MathUtils.randomSign() * ULTRA_BALL_Y_SPEED;
		points = ULTRA_BALL_POINTS;
	}

	@Override
	public void update(float delta) {
		movement += ySpeed * delta;
		setPosition(x - (xSpeed * delta), y + (ySpeed * delta));
		
		if(movement > ULTRA_BALL_MOVEMENT_SPEN) {
			ySpeed = -ULTRA_BALL_Y_SPEED;
		} else if(movement < -ULTRA_BALL_MOVEMENT_SPEN) {
			ySpeed = ULTRA_BALL_Y_SPEED;
		}
	}
}
