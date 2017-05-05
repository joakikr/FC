package com.flappy.chu.game;

public abstract class GameObject {
	protected float x;
	protected float y;
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
