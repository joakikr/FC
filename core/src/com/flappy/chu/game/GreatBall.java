package com.flappy.chu.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class GreatBall extends Ball {
	
	public static float CHANCE = 0.20f;

	private static final float BETWEEN_DISTANCE = 230f;
	private static final float GREAT_BALL_Y_SPEED = 50f;
	private static final float GREAT_BALL_MOVEMENT_SPEN = 50f;
	private static final int GREAT_BALL_POINTS = 2;
	
	private float ySpeed = 0;
	private float movement = 0;
	
	public GreatBall(TextureRegion floorTexture, TextureRegion ceilingTexture) {
		super(floorTexture, ceilingTexture, BETWEEN_DISTANCE);
		ySpeed = MathUtils.randomSign() * GREAT_BALL_Y_SPEED;
		points = GREAT_BALL_POINTS;
	}

	@Override
	public void update(float delta) {
		movement += ySpeed * delta;
		setPosition(x - (xSpeed * delta), y + (ySpeed * delta));
		
		if(movement > GREAT_BALL_MOVEMENT_SPEN) {
			ySpeed = -GREAT_BALL_Y_SPEED;
		} else if(movement < -GREAT_BALL_MOVEMENT_SPEN) {
			ySpeed = GREAT_BALL_Y_SPEED;
		}
	}
}
