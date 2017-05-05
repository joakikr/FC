package com.flappy.chu.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PokeBall extends Ball {

	public static float CHANCE = 0.65f;
	
	private static final float BETWEEN_DISTANCE = 200f;
	private static final int POKEBALL_POINTS = 1;

	
	public PokeBall(TextureRegion floorTexture, TextureRegion ceilingTexture) {
		super(floorTexture, ceilingTexture, BETWEEN_DISTANCE);
		points = POKEBALL_POINTS;
	}

	@Override
	public void update(float delta) {
		setPosition(x - (xSpeed * delta), y);
	}
}
