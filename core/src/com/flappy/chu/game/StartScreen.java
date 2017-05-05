package com.flappy.chu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class StartScreen extends ScreenAdapter {

	private final FlappyChu game;
	private Stage stage;
	
	public StartScreen(FlappyChu game) {
		this.game = game;
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void show() {
		super.show();
		stage = new Stage(new FitViewport(Commons.WORLD_WIDTH, Commons.WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		
		Image bg = new Image();
		bg.setColor(Commons.COLOR_BLUE);
		
		ImageButton play = new ImageButton(new TextureRegionDrawable(Assets.playRegion), 
				new TextureRegionDrawable(Assets.playPressRegion));
		play.setPosition(Commons.WORLD_WIDTH / 2, Commons.WORLD_HEIGHT / 4, Align.center);
		play.addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				super.tap(event, x, y, count, button);
				startGame();
			}
		});
		
		Image title = new Image(Assets.titleRegion);
		title.setPosition(Commons.WORLD_WIDTH / 2, 5 * Commons.WORLD_HEIGHT / 7, Align.center);
		
		stage.addActor(bg);
		stage.addActor(play);
		stage.addActor(title);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		delta = Math.min(delta, Commons.MIN_FPS);
		
		// Clear the screen
		Gdx.gl.glClearColor(Commons.COLOR_BLUE.r, Commons.COLOR_BLUE.g, Commons.COLOR_BLUE.b, Commons.COLOR_BLUE.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			startGame();
			return;
		}
		
		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}
	
	private void startGame() {
		game.setScreen(new GameScreen(game));
		dispose();
	}
}
