package com.flappy.chu.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	// Start screen
	public static TextureRegion playRegion;
	public static TextureRegion playPressRegion;
	public static TextureRegion titleRegion;
	
	// Game screen
	public static TextureRegion bg;
	public static TextureRegion pokeball;
	public static TextureRegion pokeballFLipped;
	public static TextureRegion greatball;
	public static TextureRegion greatballFlipped;
	public static TextureRegion ultraball;
	public static TextureRegion ultraballFlipped;
	public static TextureRegion masterball;
	public static TextureRegion masterballFlipped;
	public static TextureRegion creepyAsh;
	public static TextureRegion thunderbolt;
	public static Animation pikachuAnimation;
	public static Animation thunderboltAnimation;

	// Sounds
	public static Sound pikaHappySound;
	public static Sound pikaSadSound;
	public static Music pikaThunderboltSound;
	public static Music bgMusic;
	
	public static void load(FlappyChu game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("flappy_chu_assets.atlas", TextureAtlas.class);

		// Start Screen
		playRegion = textureAtlas.findRegion("play");
		playPressRegion = textureAtlas.findRegion("playPress");
		titleRegion = textureAtlas.findRegion("title");
		
		// Game Screen
		bg = textureAtlas.findRegion("forest"); 
		pokeball = textureAtlas.findRegion("pokeball");
		pokeballFLipped = textureAtlas.findRegion("pokeballFlipped");
		greatball = textureAtlas.findRegion("greatball");
		greatballFlipped = textureAtlas.findRegion("greatballFlipped");
		ultraball = textureAtlas.findRegion("ultraball");
		ultraballFlipped = textureAtlas.findRegion("ultraballFlipped");
		masterball = textureAtlas.findRegion("masterball");
		masterballFlipped = textureAtlas.findRegion("masterballFlipped");
		creepyAsh = textureAtlas.findRegion("creepyash");
		thunderbolt = textureAtlas.findRegion("thunderbolt");
		pikachuAnimation = new Animation(0.15f, textureAtlas.findRegion("pika1"), 
				textureAtlas.findRegion("pika2"), 
				textureAtlas.findRegion("pika3"), 
				textureAtlas.findRegion("pika4"));
		thunderboltAnimation = new Animation(0.20f, textureAtlas.findRegion("thunderboltbg1"),textureAtlas.findRegion("thunderboltbg2"));
		
		// Sound
		pikaHappySound = game.getAssetManager().get("pika.wav", Sound.class);
		pikaSadSound = game.getAssetManager().get("pikasad.wav", Sound.class);
		pikaThunderboltSound = game.getAssetManager().get("thunderbolt.mp3", Music.class);
		bgMusic = game.getAssetManager().get("pkmbgmusic.mp3", Music.class);
		bgMusic.setLooping(true);
		bgMusic.setVolume(Commons.BG_MUSIC_VOLUME);
	}
	
	public static void playSound (Sound sound) {
		if (Settings.soundEnabled) {
			sound.play(Commons.SOUND_VOLUME);
		}
	}
	
	public static void playMusic (Music music) {
		if (Settings.soundEnabled) {
			 music.play();
		}
	}
}
