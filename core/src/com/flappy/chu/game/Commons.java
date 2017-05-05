package com.flappy.chu.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

public interface Commons {
	public static final float WORLD_WIDTH = 480;
	public static final float WORLD_HEIGHT = 640;
	public static final float WORLD_PADDING = 5;
	
	public static final float MIN_FPS = 1/30f;
	public static final float GAP_BETWEEN_BALLS = 200f;
	public static final int NUM_THUNDERBOLTS = 3;
	public static final int NUM_HIGHSCORES = 3;
	
	public static final int PAUSE_KEY = Keys.A;
	public static final int SOUND_KEY = Keys.S;
	public static final int JUMP_KEY = Keys.SPACE;
	public static final int THUNDERBOLT_KEY = Keys.D;
	
	public static final float BG_MUSIC_VOLUME = 0.5f;
	public static final float SOUND_VOLUME = 1f;
	
	public static final Color COLOR_BLUE = Color.valueOf("00C4DA");
}