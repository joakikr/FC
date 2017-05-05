package com.flappy.chu.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.flappy.chu.game.FlappyChu;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 480;
		config.width = 360;
		
		// Pack Textures to single sheet
//		TexturePacker.process("../assets", "../assets", "flappy_chu_assets");
		
		new LwjglApplication(new FlappyChu(), config);
	}
}