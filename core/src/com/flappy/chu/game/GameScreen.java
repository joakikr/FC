package com.flappy.chu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.flappy.chu.game.Pikachu.PIKACHU_STATE;

public class GameScreen extends ScreenAdapter {

	private final FlappyChu game;
	private Viewport viewport;
	private Camera camera;
	private BitmapFont bitmapFont;
	private GlyphLayout glyphLayout;

	private Array<Ball> balls;
	private Pikachu pikachu;
	private CreepyAsh ash;
	private int score;
	private String scoreString;
	
	private enum GAME_STATE {
		GAME_BEFORE, GAME_PLAYING, GAME_PAUSE, GAME_OVER
	}
	
	private GAME_STATE state;
	
	public GameScreen(FlappyChu game) {
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
		
		// Setup camera, viewport and rendering
		camera = new OrthographicCamera();
		camera.position.set(Commons.WORLD_WIDTH / 2, Commons.WORLD_HEIGHT / 2, 0);
		camera.update();
		viewport = new FitViewport(Commons.WORLD_WIDTH, Commons.WORLD_HEIGHT, camera);
		bitmapFont = game.getAssetManager().get("score.fnt", BitmapFont.class);
		bitmapFont.setColor(Color.BLACK);
		glyphLayout = new GlyphLayout();
		
		// Configure assets
		balls = new Array<Ball>();
		pikachu = new Pikachu(Assets.pikachuAnimation, Assets.thunderboltAnimation);
		ash = new CreepyAsh(Assets.creepyAsh);
		state = GAME_STATE.GAME_BEFORE;
		restart();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		delta = Math.min(delta, Commons.MIN_FPS);
		update(delta);
		draw();
//		drawDebug();
	}
	
	private void restart() {
		pikachu.setPosition(Commons.WORLD_WIDTH / 4, Commons.WORLD_HEIGHT / 2);
		pikachu.setNumThunderbolts(Commons.NUM_THUNDERBOLTS);
		pikachu.state = PIKACHU_STATE.FREE;
		ash.setPosition(Commons.WORLD_WIDTH / 2 - Assets.creepyAsh.getRegionWidth() / 2, - Assets.creepyAsh.getRegionHeight());
		balls.clear();
		score = 0;
		pikachu.jump();
		
		if(Settings.soundEnabled) {
			Assets.bgMusic.play();
		}
	}
	
	private void update(float delta) {
		// Check if sound is enabled/disabled
		updateSound();
		
		switch(state) {
		case GAME_BEFORE:
			updateBefore();
			break;
		case GAME_PLAYING:
			updatePlaying(delta);
			break;
		case GAME_PAUSE:
			updatePause();
			break;
		case GAME_OVER:
			updateOver(delta);
			break;
		}
	}
	
	private void updateSound() {
		if(Gdx.input.isKeyJustPressed(Commons.SOUND_KEY)) {
			Settings.soundEnabled = !Settings.soundEnabled;
			if(Settings.soundEnabled) {
				Assets.bgMusic.play();
			} else {
				Assets.bgMusic.pause();
			}
		}
	}
	
	private void updateBefore() {
		if(Gdx.input.isKeyJustPressed(Commons.JUMP_KEY)) {
			restart();
			state = GAME_STATE.GAME_PLAYING;
		}
	}
	
	private void updatePlaying(float delta) {
		// Pause button pressed?
		if(Gdx.input.isKeyJustPressed(Commons.PAUSE_KEY)) {
			state = GAME_STATE.GAME_PAUSE;
			return;
		}
		
		// Thunderbolt
		if(Gdx.input.isKeyJustPressed(Commons.THUNDERBOLT_KEY)) {
			if(pikachu.getNumThunderbolts() > 0 && pikachu.state != PIKACHU_STATE.THUNDERBOLT) {
				pikachu.state = PIKACHU_STATE.THUNDERBOLT;
				Assets.playMusic(Assets.pikaThunderboltSound);
				pikachu.setNumThunderbolts(pikachu.getNumThunderbolts() - 1);
				balls.clear();
			}
		}
		
		// Pikachu
		pikachu.update(delta);
		if(Gdx.input.isKeyJustPressed(Commons.JUMP_KEY)) {
			pikachu.jump();
		}
		blockPikachuLeavingWorld();
			
		// Balls
		for(Ball ball : balls) {
			ball.update(delta);
		}
		checkIfNewBallIsNeeded();
		removeBallIfPassed();
		
		// Score
		Ball ball = balls.first();
		if(ball.getX() < pikachu.getX() && !ball.isPointClaimed()) {
			Assets.playSound(Assets.pikaHappySound);
			ball.markPointClaimed();
			
			// Certain balls give extra thunderbolt
			if(ball.givesExtraThunderbolt()) {
				pikachu.setNumThunderbolts(pikachu.getNumThunderbolts() + 1);
			}
			
			score += ball.getPoints();
		}
		
		// Collision
		if(checkForCollision()) {
			// Pikachu got caught!
			state = GAME_STATE.GAME_OVER;
			pikachu.state = PIKACHU_STATE.CAUGHT;
			Assets.bgMusic.stop();
			Assets.playSound(Assets.pikaSadSound);

			// Did we get a new highscore?
			if(score > Settings.highScoreScores[Commons.NUM_HIGHSCORES - 1]) {
				scoreString = "NEW HIGHSCORE: " + score;
			} else {
				scoreString = "SCORE: " + score;
			}
			
			Settings.addScore("Anonym", score);
			Settings.save();
		}
	}
	
	private void updatePause() {
		if(Gdx.input.isKeyJustPressed(Commons.PAUSE_KEY)) {
			state = GAME_STATE.GAME_PLAYING;
		}
	}
	
	private void updateOver(float delta) {
		pikachu.update(delta);
		ash.update(delta);
		
		if(Gdx.input.isKeyPressed(Commons.JUMP_KEY)) {
			state = GAME_STATE.GAME_PLAYING;
			restart();
		}
	}
	
	private void draw() {
		Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.getBatch().setProjectionMatrix(camera.projection);
		game.getBatch().setTransformMatrix(camera.view);
		game.getBatch().begin();
		
		// Background
		game.getBatch().draw(Assets.bg, 0, 0);
		
		// Balls
		for(Ball ball : balls) {
			ball.draw(game.getBatch());
		}
		
		switch(state) {
		case GAME_BEFORE:
			drawBefore();
			break;
		case GAME_PLAYING:
			drawPlaying();
			break;
		case GAME_PAUSE:
			drawPause();
			break;
		case GAME_OVER:
			drawOver();
			break;
		}
		
		game.getBatch().end();
	}
	
	private void drawBefore() {
		pikachu.draw(game.getBatch());
		glyphLayout.setText(bitmapFont, "Press SPACE to jump...");
		bitmapFont.draw(game.getBatch(), "Press SPACE to jump...", 0, viewport.getWorldHeight() - glyphLayout.height, viewport.getWorldWidth() - 20, Align.center, true);
		
		glyphLayout.setText(bitmapFont, "Avoid getting caught by pokeballs!");
		bitmapFont.draw(game.getBatch(), "Avoid getting caught by pokeballs!", 0, viewport.getWorldHeight() - 4 * glyphLayout.height, viewport.getWorldWidth() - 20, Align.center, true);
		
		glyphLayout.setText(bitmapFont, "'D' for thunderbolt!");
		bitmapFont.draw(game.getBatch(), "'D' for thunderbolt!", 0, 6 * glyphLayout.height, viewport.getWorldWidth() - 20, Align.center, true);
		
		glyphLayout.setText(bitmapFont, "'A' for pause.");
		bitmapFont.draw(game.getBatch(), "'A' for pause.", 0, 4 * glyphLayout.height, viewport.getWorldWidth() - 20, Align.center, true);
		
		glyphLayout.setText(bitmapFont, "'S' for toggle Sound.");
		bitmapFont.draw(game.getBatch(), "'S' for toggle Sound.", 0, 2 * glyphLayout.height, viewport.getWorldWidth() - 20, Align.center, true);
	}
	
	private void drawPlaying() {
		// Pikachu
		pikachu.draw(game.getBatch());
		
		// Thunderbolts left
		for(int i = 0; i < pikachu.getNumThunderbolts(); i++) {
			game.getBatch().draw(Assets.thunderbolt, 
					i * Assets.thunderbolt.getRegionWidth() + Commons.WORLD_PADDING, 
					Commons.WORLD_HEIGHT - Assets.thunderbolt.getRegionHeight() - Commons.WORLD_PADDING);
		}
				
		// Score
		glyphLayout.setText(bitmapFont, Integer.toString(score));
		bitmapFont.draw(game.getBatch(), 
				Integer.toString(score), 
				Commons.WORLD_WIDTH - glyphLayout.width - Commons.WORLD_PADDING, 
				Commons.WORLD_HEIGHT - Commons.WORLD_PADDING);
	}
	
	private void drawPause() {
		pikachu.draw(game.getBatch());
		glyphLayout.setText(bitmapFont, "Game Paused, press 'A' to continue.");
		bitmapFont.draw(game.getBatch(), "Game Paused, press 'A' to continue.", 0, viewport.getWorldHeight() - glyphLayout.height, viewport.getWorldWidth() - 20, Align.center, true);
	}
	
	private void drawOver() {
		ash.draw(game.getBatch());
		pikachu.draw(game.getBatch());
		glyphLayout.setText(bitmapFont, "Press SPACE to play...");
		bitmapFont.draw(game.getBatch(), "Press SPACE to play...", 0, viewport.getWorldHeight() - glyphLayout.height, viewport.getWorldWidth() - 20, Align.center, true);		

		// Scores
		glyphLayout.setText(bitmapFont, scoreString);
		bitmapFont.draw(game.getBatch(), scoreString, 0, viewport.getWorldHeight() - 3 * glyphLayout.height, viewport.getWorldWidth() - 20, Align.center, true);
		for(int i = 0; i < Commons.NUM_HIGHSCORES; i++) {
			String message = i + 1 + ": " + Settings.highScoreScores[i];
			glyphLayout.setText(bitmapFont, message);
			bitmapFont.draw(game.getBatch(), message, 4 * viewport.getWorldWidth() / 10, viewport.getWorldHeight() - 7 * glyphLayout.height - (i + 1) * glyphLayout.height);
		}
	}
	
	private void drawDebug() {
		game.getShapeRenderer().setProjectionMatrix(camera.projection);
		game.getShapeRenderer().setTransformMatrix(camera.view);
		game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
		pikachu.drawDebug(game.getShapeRenderer());
		for(Ball ball : balls) {
			ball.drawDebug(game.getShapeRenderer());
		}
		game.getShapeRenderer().end();
	}
	
	private void blockPikachuLeavingWorld() {
		pikachu.setPosition(pikachu.getX(), MathUtils.clamp(pikachu.getY(), 0, Commons.WORLD_HEIGHT));
	}
	
	private void createNewBall() {
		// TODO: Create better random algorithm, more general one
		Ball newBall = null;
		float random = MathUtils.random();
		
		if(random <= MasterBall.CHANCE) {
			newBall = new MasterBall(Assets.masterball, Assets.masterballFlipped);
		} else if(random <= MasterBall.CHANCE + UltraBall.CHANCE) {
			newBall = new UltraBall(Assets.ultraball, Assets.ultraballFlipped);
		} else if(random <= MasterBall.CHANCE + UltraBall.CHANCE + GreatBall.CHANCE) {
			newBall = new GreatBall(Assets.greatball, Assets.greatballFlipped);
		} else if(random <= MasterBall.CHANCE + UltraBall.CHANCE + GreatBall.CHANCE + PokeBall.CHANCE) {
			newBall = new PokeBall(Assets.pokeball, Assets.pokeballFLipped);
		}			
		
		newBall.setPosition(Commons.WORLD_WIDTH + Ball.WIDTH, newBall.getY());
		balls.add(newBall);
	}
	
	private void removeBallIfPassed() {
		if(balls.size > 0) {
			Ball first = balls.first();
			if(first.getX() < - Ball.WIDTH) {
				balls.removeValue(first, true);
			}
		}
	}
	
	private void checkIfNewBallIsNeeded() {
		if(balls.size == 0) {
			createNewBall();
		} else {
			Ball ball = balls.peek();
			if(ball.getX() < Commons.WORLD_WIDTH - Commons.GAP_BETWEEN_BALLS) {
				createNewBall();
			}
		}
	}
	
	private boolean checkForCollision() {
		
		// Check with balls
		for(Ball ball : balls) {
			if(ball.isPikachuColliding(pikachu)) {
				return true;
			}
		}
		return false;
	}
}