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
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;

public class Game extends BaseGameActivity implements IOnSceneTouchListener {

	private Font mFont;
	private Texture mFontTexture;
	private static final int CAMERA_WIDTH = 320;
    private static final int CAMERA_HEIGHT = 480;    
    private static Camera mCamera;
	private TextureRegion mPlayerTextureRegion;
	private TextureRegion mEnemyTextureRegion;	
	private Sprite mPlayerSprite;
	private ArrayList<Sprite> mEnemyArray = new ArrayList<Sprite>();	
	private int mScore;
	private ChangeableText mScoreDisplay;
	private int mPlayerLife;
	private ChangeableText mPlayerLifeDisplay;
	private final int mPlayerDead = 0;
	private Handler mHandler;
	
	@Override
	public Engine onLoadEngine() {
    	Game.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);        
        Engine mEngine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),Game.mCamera));
        return mEngine;
	}

	@Override
	public void onLoadResources() {
		mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		Texture texture = new Texture(256, 128);
		mFont = FontFactory.createFromAsset(mFontTexture, this, "font/COURBD.TTF", 24, true, Color.WHITE);
    	mPlayerTextureRegion = TextureRegionFactory.createFromAsset(texture, this, "gfx/Player.png", 0, 0);
    	mEnemyTextureRegion = TextureRegionFactory.createFromAsset(texture, this, "gfx/Enemy.png", 125, 0);
    	mFont.prepareLettes("ScoreLivs:0123456789-".toCharArray());
    	this.mEngine.getTextureManager().loadTexture(texture);
    	this.onLoadSprites();
		mEngine.getTextureManager().loadTexture(mFontTexture);
		mEngine.getFontManager().loadFont(mFont);
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
        
        mScore = 0;
        mPlayerLife = 5;
        Text mScoreDisplay = new ChangeableText(0, 0, mFont, "Score: " + mScore);
        Text mPlayerLifeDisplay = new ChangeableText(185, 0, mFont, "Lives: " + mPlayerLife);
	    scene.attachChild(mScoreDisplay);
	    scene.attachChild(mPlayerLifeDisplay);
        
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
				//Commented out setText updates, were causing NullPointerExceptions
				//mScoreDisplay.setText("Score: " + mScore);
				//mPlayerLifeDisplay.setText("Lives: " + mPlayerLife);
				System.out.println("Score: " + mScore + "  Lives: " + mPlayerLife);
				//NullPointerExceptions on getting a "Game Over" condition and loading it.
				//if (mPlayerLife == mPlayerDead) {
					//mEngine.stop();
					//mHandler.post(mGameOver);
				//}
				synchronized(enemy) {
					size--;
					mEnemyArray.remove(i);
					scene.getLastChild().detachChild(enemy);
				}
			}
			
			else if (enemy.getY() > CAMERA_HEIGHT) {			
				mScore++;
				//Commented out setText updates, were causing NullPointerExceptions
				//mScoreDisplay.setText("Score: " + String.valueOf(mScore));
				System.out.println("Score: " + mScore + "  Lives: " + mPlayerLife);
				synchronized(enemy) {
					size--;
					mEnemyArray.remove(i);
					scene.getLastChild().detachChild(enemy);
				}
			}
		}
	}
	
	private Runnable mGameOver = new Runnable() {
		public void run() {
			Intent myIntent = new Intent(Game.this, GameOver.class);
			Game.this.startActivity(myIntent);
			Game.this.finish();
		}
	};

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
	    long range = (long)aEnd - (long)aStart + 1;
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
		return (CAMERA_WIDTH / 2) - width / 2;
	}
}
