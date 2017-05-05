package com.flappy.chu.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class Ball extends GameObject {
	
	protected static final float COLLISION_RECTANGLE_WIDTH = 13f;
	protected static final float COLLISION_RECTANGLE_HEIGHT = 330f;
	protected static final float COLLISION_CIRCLE_RADIUS = 44f;
	protected static final float HEIGHT_MAX = -350f;
	protected static final float HEIGHT_MIN = -100f;
	protected static final float TOP_MARGIN = 28f;
	public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;

	protected final float betweenDistance;
	
	protected final Circle floorCollisionCircle;
	protected final Rectangle floorCollisionRectangle;
	protected final TextureRegion floorTexture;
	
	protected final Circle ceilingCollisionCircle;
	protected final Rectangle ceilingCollisionRectangle;	
	protected final TextureRegion ceilingTexture;
	
	protected float xSpeed = 180f;
	
	protected boolean pointClaimed = false;
	protected int points;
	
	public Ball(TextureRegion floorTexture, TextureRegion ceilingTexture, float betweenDistance) {
		super.setPosition(0, MathUtils.random(HEIGHT_MIN, HEIGHT_MAX));
		this.floorTexture = floorTexture;
		this.ceilingTexture = ceilingTexture;
		this.betweenDistance = betweenDistance;
		floorCollisionRectangle = new Rectangle(x, y, COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
		floorCollisionCircle = new Circle(x + floorCollisionRectangle.width / 2, y + floorCollisionRectangle.height + COLLISION_CIRCLE_RADIUS, COLLISION_CIRCLE_RADIUS);
		
		ceilingCollisionRectangle = new Rectangle(x, floorCollisionCircle.y + betweenDistance + TOP_MARGIN + 2 * COLLISION_CIRCLE_RADIUS, COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
		ceilingCollisionCircle = new Circle(x + ceilingCollisionRectangle.width / 2, ceilingCollisionRectangle.y - COLLISION_CIRCLE_RADIUS, COLLISION_CIRCLE_RADIUS);
	}

	public abstract void update(float delta);
	
	public void draw(SpriteBatch batch) {
    	drawFloorBall(batch);
    	drawCeilingBall(batch);
	}
	
	public void drawDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.circle(floorCollisionCircle.x, floorCollisionCircle.y, floorCollisionCircle.radius);
		shapeRenderer.rect(floorCollisionRectangle.x, floorCollisionRectangle.y, floorCollisionRectangle.width, floorCollisionRectangle.height);
		shapeRenderer.circle(ceilingCollisionCircle.x, ceilingCollisionCircle.y, ceilingCollisionCircle.radius);
		shapeRenderer.rect(ceilingCollisionRectangle.x, ceilingCollisionRectangle.y, ceilingCollisionRectangle.width, ceilingCollisionRectangle.height);
	}
	
	public void setPosition(float newX, float newY) {
		super.setPosition(newX, newY);
		updateCollisionCircle();
		updateCollisionRectangle();
	}
	
	public boolean isPointClaimed() {
		return pointClaimed;
	}
	
	public void markPointClaimed() {
		pointClaimed = true;
	}
	
	public int getPoints() {
		return points;
	}
	
    public boolean isPikachuColliding(Pikachu pikachu) {
        Circle pikachuCollisionCircle = pikachu.getCollisionCircle();
        return
                Intersector.overlaps(pikachuCollisionCircle, ceilingCollisionCircle) ||
                        Intersector.overlaps(pikachuCollisionCircle, floorCollisionCircle) ||
                        Intersector.overlaps(pikachuCollisionCircle, ceilingCollisionRectangle) ||
                        Intersector.overlaps(pikachuCollisionCircle, floorCollisionRectangle);
    }
	
    public boolean givesExtraThunderbolt() {
    	return false;
    }
    
    protected void updateCollisionCircle() {
    	floorCollisionCircle.setPosition(x + floorCollisionRectangle.width / 2, y + floorCollisionRectangle.height + COLLISION_CIRCLE_RADIUS);
    	ceilingCollisionCircle.setPosition(x + ceilingCollisionRectangle.width / 2, ceilingCollisionRectangle.y - COLLISION_CIRCLE_RADIUS);
    }
    
    protected void updateCollisionRectangle() {
    	floorCollisionRectangle.setPosition(x, y);
    	ceilingCollisionRectangle.setPosition(x, floorCollisionCircle.y + betweenDistance + TOP_MARGIN + 2 * COLLISION_CIRCLE_RADIUS);
    }
    
    protected void drawFloorBall(SpriteBatch batch) {
		float textureX = floorCollisionCircle.x - floorTexture.getRegionWidth() / 2;
		float textureY = floorCollisionRectangle.getY();
		batch.draw(floorTexture, textureX, textureY);
	}
	
	protected void drawCeilingBall(SpriteBatch batch) {
		float textureX = ceilingCollisionCircle.x - ceilingTexture.getRegionWidth() / 2;
		float textureY = ceilingCollisionRectangle.getY() - 2 * COLLISION_CIRCLE_RADIUS - TOP_MARGIN;
		batch.draw(ceilingTexture, textureX, textureY);
	}
}
