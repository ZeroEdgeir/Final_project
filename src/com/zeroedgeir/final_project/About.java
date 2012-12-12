/*
 * Author: Randy Woolner
 * Date: 12/12/2012
 * File: About.java
 * File Description: Displays information regarding creation and version of program.
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

public class About extends BaseGameActivity {

	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
	
    private Camera mCamera;
    private Texture mAboutTexture;
    private TextureRegion mAboutTextureRegion;
    private Scene mAboutScene;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera (0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mAboutTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mAboutTextureRegion = TextureRegionFactory.createFromAsset(this.mAboutTexture, this, "gfx/AboutScreen.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mAboutTexture);
		
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		final int centerX = (CAMERA_WIDTH - this.mAboutTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mAboutTextureRegion.getHeight()) / 2;
		
		this.mAboutScene = new Scene(1);
		final Sprite menuBack = new Sprite(centerX, centerY, this.mAboutTextureRegion);
		mAboutScene.getLastChild().attachChild(menuBack);
		
		return this.mAboutScene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

}
