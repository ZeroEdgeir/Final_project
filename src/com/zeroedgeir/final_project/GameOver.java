/*
 * Author: Randy Woolner
 * Date: 13/12/2012
 * File: GameOver.java
 * File Description: Screen displayed when the player loses.
 * Program: Star Pilot
 * Program Description: A simple Android game of dodging enemy space ships
 */

package com.zeroedgeir.final_project;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.os.Handler;
import android.content.Intent;

public class GameOver extends BaseGameActivity {

	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
	
    private Camera mCamera;
    private Texture mTexture;
    private TextureRegion mGameOverTextureRegion;
	private Handler mHandler;
	
	@Override
	public Engine onLoadEngine() {
		mHandler = new Handler(); 
		this.mCamera = new Camera (0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mGameOverTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/GameOverScreen.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final Scene scene = new Scene(1);
		final int centerX = (CAMERA_WIDTH - this.mGameOverTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mGameOverTextureRegion.getHeight()) / 2;
		final Sprite gameOver = new Sprite(centerX, centerY, this.mGameOverTextureRegion);
		scene.getLastChild().attachChild(gameOver);
		return scene;
	}

	@Override
	public void onLoadComplete() {
		mHandler.postDelayed(mLaunchTask, 8000);
	}
	
	private Runnable mLaunchTask = new Runnable() {
		public void run() {
			Intent myIntent = new Intent(GameOver.this, MainMenu.class);
			GameOver.this.startActivity(myIntent);
			GameOver.this.finish();
		}
	};
}
