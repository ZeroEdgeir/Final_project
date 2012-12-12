/*
 * Author: Randy Woolner
 * Date: 12/12/2012
 * File: Game.java
 * File Description: Creates and loads the game, allowing the player to play the game.
 * Program: Star Pilot
 * Program Description: A simple Android game of dodging enemy space ships
 */

package com.zeroedgeir.final_project;

import java.util.ArrayList;
import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class Game extends BaseGameActivity implements IOnSceneTouchListener {

	private static final int CAMERA_WIDTH = 320;
    private static final int CAMERA_HEIGHT = 480;    
    private static Camera mCamera;
	private TextureRegion mPlayerTextureRegion;
	private TextureRegion mEnemyTextureRegion;	
	private Sprite mPlayerSprite;
	private ArrayList<Sprite> mEnemyArray = new ArrayList<Sprite>();	
	private int mScore = 0;
	private int mPlayerLife = 5;
	
	@Override
	public Engine onLoadEngine() {
    	Game.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);        
        Engine mEngine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),Game.mCamera));
        return mEngine;
	}

	@Override
	public void onLoadResources() {
		Texture texture = new Texture(256, 128);    	
    	mPlayerTextureRegion = TextureRegionFactory.createFromAsset(texture, this, "gfx/Player.png", 0, 0);
    	mEnemyTextureRegion = TextureRegionFactory.createFromAsset(texture, this, "gfx/Enemy.png", 125, 0);    	
    	this.mEngine.getTextureManager().loadTexture(texture);
    	this.onLoadSprites();
	}

	private void onLoadSprites() {
		mPlayerSprite = new Sprite(getScreenCenterX(mPlayerTextureRegion.getWidth()), CAMERA_HEIGHT - mPlayerTextureRegion.getHeight(), mPlayerTextureRegion);
    }

	@Override
	public Scene onLoadScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        final Scene scene = new Scene(1);               
        scene.getLastChild().attachChild(mPlayerSprite);        
        scene.registerUpdateHandler(new IUpdateHandler() {
        	private long lastEnemyAdd = 0;
			@Override
        	public void onUpdate(final float pSecondsElapsed) {        		
				updateEnemyPoistion(scene);
        		if (System.currentTimeMillis() - lastEnemyAdd  > 1500) {
        			lastEnemyAdd = System.currentTimeMillis();        			
        			addEnemy(scene);
        		}
            }

			@Override
			public void reset() {

			}
        });
        
        scene.setOnSceneTouchListener(this);        
        return scene;
	}
	
	private void updateEnemyPoistion(Scene scene) {
		// TODO Auto-generated method stub
		int size = mEnemyArray.size();
		for (int i = 0; i < size; i++) 
		{
			Sprite enemy = mEnemyArray.get(i);        			
			enemy.setPosition(enemy.getX(), enemy.getY() + 5); 		
			if (enemy.collidesWith(mPlayerSprite)) {				
				mScore--;
				mPlayerLife--;
				System.out.println("Score: " + mScore);
				synchronized(enemy) {
					size--;
					mEnemyArray.remove(i);
					scene.getLastChild().detachChild(enemy);
				}
			}
			
			else if (enemy.getY() > CAMERA_HEIGHT) {			
				mScore++;
				System.out.println("Score: " + mScore);
				synchronized(enemy) {
					size--;
					mEnemyArray.remove(i);
					scene.getLastChild().detachChild(enemy);
				}
			}
		}
	}

	private void addEnemy(Scene scene) {
		int START = 1;
		int END = 300;
		Random random = new Random();        			    
		int randomInt = showRandomInteger(START, END, random);
		Sprite enemy = getEnemy(randomInt, 0);
		mEnemyArray.add(enemy);
		scene.getLastChild().attachChild(enemy);
	}
	
	public int showRandomInteger(int aStart, int aEnd, Random aRandom){
	    if ( aStart > aEnd ) {
	    	throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    return randomNumber;
	}
	
	public Sprite getEnemy(float x, float y) {
		return (new Sprite(x, y, mEnemyTextureRegion.clone()));
	}
	
	@Override
	public void onLoadComplete() {
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
		this.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				float touchX = pSceneTouchEvent.getX();
				mPlayerSprite.setPosition(touchX - mPlayerSprite.getWidth()/2, mPlayerSprite.getY());
			}
		});
		return true;
	}
	
	private float getScreenCenterX(float width) {
		// TODO Auto-generated method stub
		return (CAMERA_WIDTH / 2) - width / 2;
	}
}
