/*
 * Author: Randy Woolner
 * Date: 10/12/2012
 * Program: Star Pilot
 * Description: A simple Android game of dodging enemy space ships
 */

package com.zeroedgeir.final_project;

import java.util.LinkedList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Display;
import android.view.Menu;

public class Main extends BaseGameActivity implements IOnSceneTouchListener {

	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
	
    private Camera mCamera;
    private Texture mTexture;
    private TextureRegion mSplashTextureRegion;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera (0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mSplashTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/Splashscreen.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mTexture);
		
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final Scene scene = new Scene(1);
		// Centers the Splashscreen
		final int centerX = (CAMERA_WIDTH - this.mSplashTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mSplashTextureRegion.getHeight()) / 2;
		// Creates and loads the Splashscreen Sprite
		final Sprite splash = new Sprite(centerX, centerY, this.mSplashTextureRegion);
		scene.getLastChild().attachChild(splash);
		return scene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
    
}
