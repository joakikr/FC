package com.flappy.chu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {

	public static boolean soundEnabled = true;
	public static String[] highScoreNames = new String[Commons.NUM_HIGHSCORES];
	public static int[] highScoreScores = new int[Commons.NUM_HIGHSCORES];
	public final static String file = ".flappychu";
	
	
	public static void load() {
		try {
			FileHandle filehandle = Gdx.files.external(file);
			String[] lines = filehandle.readString().split("\n");
			soundEnabled = Boolean.parseBoolean(lines[0]);
			
			for(int i = 0; i < Commons.NUM_HIGHSCORES; i++) {
				String[] score = lines[i + 1].split(" ");
				highScoreNames[i] = score[0];
				highScoreScores[i] = Integer.parseInt(score[1]);
			}
			
		} catch(Throwable e) {
			Gdx.app.log("Error", "Could not read " + file + " in Settings.load()");
		}
	}
	
	public static void save() {
		try {
			FileHandle filehandle = Gdx.files.external(file);
			filehandle.writeString(Boolean.toString(soundEnabled) + "\n", false);
			for(int i = 0; i < Commons.NUM_HIGHSCORES; i++) {
				filehandle.writeString(highScoreNames[i] + " " + highScoreScores[i] + "\n", true);
			}
			
		} catch(Exception e) {
			Gdx.app.log("Error", "Could not read " + file + " in Settings.save()");
		}
	}
	
	public static void addScore(String name, int score) {
		for (int i = 0; i < Commons.NUM_HIGHSCORES; i++) {
			if (highScoreScores[i] < score) {
				for (int j = Commons.NUM_HIGHSCORES - 1; j > i; j--) {
					highScoreScores[j] = highScoreScores[j - 1];
					highScoreNames[j] = highScoreNames[j - 1];
				}
				
				highScoreScores[i] = score;
				highScoreNames[i] = name;
				break;
			}
		}
	}
}
