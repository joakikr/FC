package com.flappy.chu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends ScreenAdapter {
	
	private static final float PROGRESS_BAR_WIDTH = 200;
	private static final float PROGRESS_BAR_HEIGHT = 50;
	
	private Viewport viewport;
	private Camera camera;
	
	private float progress = 0;
	private FlappyChu game;
	
	public LoadingScreen(FlappyChu game) {
		this.game = game;
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public void show() {
		super.show();
		camera = new OrthographicCamera();
		camera.position.set(Commons.WORLD_WIDTH / 2, Commons.WORLD_HEIGHT / 2, 0);
		camera.update();
		viewport = new FitViewport(Commons.WORLD_WIDTH, Commons.WORLD_HEIGHT, camera);

		// Textures
		game.getAssetManager().load("flappy_chu_assets.atlas", TextureAtlas.class);
		BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
		bitmapFontParameter.atlasName = "flappy_chu_assets.atlas";
		
		// Fonts
		game.getAssetManager().load("score.fnt", BitmapFont.class, bitmapFontParameter);
		
		// Sounds
		game.getAssetManager().load("pika.wav", Sound.class);
		game.getAssetManager().load("pikasad.wav", Sound.class);
		game.getAssetManager().load("thunderbolt.mp3", Music.class);
		game.getAssetManager().load("pkmbgmusic.mp3", Music.class);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		delta = Math.min(delta, Commons.MIN_FPS);
		
		if(game.getAssetManager().update() && progress == 1) {
			// Load all assets and settings before going to Start Screen
			Settings.load();
			Assets.load(game);
			game.setScreen(new StartScreen(game));
			return;
		}
		
		progress = game.getAssetManager().getProgress();
		draw();
	}
	
	private void draw() {
		Gdx.gl.glClearColor(Commons.COLOR_BLUE.r, Commons.COLOR_BLUE.g, Commons.COLOR_BLUE.b, Commons.COLOR_BLUE.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getShapeRenderer().setProjectionMatrix(camera.projection);
		game.getShapeRenderer().setTransformMatrix(camera.view);
		game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
		game.getShapeRenderer().setColor(Color.BLACK);
		game.getShapeRenderer().rect((Commons.WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2 - 5, (Commons.WORLD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2 - 5, PROGRESS_BAR_WIDTH + 10, PROGRESS_BAR_HEIGHT + 10);
		game.getShapeRenderer().setColor(Color.WHITE);
		game.getShapeRenderer().rect((Commons.WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2, (Commons.WORLD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2, progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		game.getShapeRenderer().end();
	}
}
