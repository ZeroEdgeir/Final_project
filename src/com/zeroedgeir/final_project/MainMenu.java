/*
 * Author: Randy Woolner
 * Date: 11/12/2012
 * File: MainMenu.java
 * File Description: Creates and controls the main menu
 * Program: Star Pilot
 * Program Description: A simple Android game of dodging enemy space ships
 */

package com.zeroedgeir.final_project;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class MainMenu extends BaseGameActivity  implements IOnMenuItemClickListener {

	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
	
	private Camera mCamera;
    private Texture mMenuTexture;
    private TextureRegion mMenuTextureRegion;
    private Texture mMenuPlayTexture;
    private TextureRegion mMenuPlayTextureRegion;
    private Texture mMenuAboutTexture;
    private TextureRegion mMenuAboutTextureRegion;
    private Texture mMenuHelpTexture;
    private TextureRegion mMenuHelpTextureRegion;
    private Texture mMenuQuitTexture;
    private TextureRegion mMenuQuitTextureRegion;
	private Scene mMainScene;
	private MenuScene mStaticMenuScene;
    private Handler mHandler;
	
    protected static final int MENU_PLAY = 0;
	protected static final int MENU_ABOUT = 1;
	protected static final int MENU_HELP = 2;
	protected static final int MENU_QUIT = 3;
	
	@Override
	public Engine onLoadEngine() {
		mHandler = new Handler();
		this.mCamera = new Camera (0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}
	@Override
	public void onLoadResources() {
		this.mMenuTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuTexture, this, "gfx/MenuScreen.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuTexture);
		this.mMenuPlayTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuPlayTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuPlayTexture, this, "gfx/PlayButton.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuPlayTexture);
		this.mMenuAboutTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuAboutTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuAboutTexture, this, "gfx/AboutButton.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuAboutTexture);
		this.mMenuHelpTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuHelpTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuHelpTexture, this, "gfx/HelpButton.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuHelpTexture);
		this.mMenuQuitTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuQuitTexture, this, "gfx/QuitButton.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuQuitTexture);
		
	}
	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		this.createStaticMenuScene();
		
		final int centerX = (CAMERA_WIDTH - this.mMenuTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mMenuTextureRegion.getHeight()) / 2;
		
		this.mMainScene = new Scene(1);
		final Sprite menuBack = new Sprite(centerX, centerY, this.mMenuTextureRegion);
		mMainScene.getLastChild().attachChild(menuBack);
		mMainScene.setChildScene(mStaticMenuScene);
		
		return this.mMainScene;
	}
	protected void createStaticMenuScene() {
		this.mStaticMenuScene = new MenuScene(this.mCamera);
		final SpriteMenuItem playMenuItem = new SpriteMenuItem(MENU_PLAY, this.mMenuPlayTextureRegion);
		playMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(playMenuItem);
		final SpriteMenuItem aboutMenuItem = new SpriteMenuItem(MENU_ABOUT, this.mMenuAboutTextureRegion);
		aboutMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(aboutMenuItem);
		final SpriteMenuItem helpMenuItem = new SpriteMenuItem(MENU_HELP, this.mMenuHelpTextureRegion);
		helpMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(helpMenuItem);
		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, this.mMenuQuitTextureRegion);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(quitMenuItem);
		this.mStaticMenuScene.buildAnimations();
		this.mStaticMenuScene.setBackgroundEnabled(false);
		this.mStaticMenuScene.setOnMenuItemClickListener(this);
	}
	
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch (pMenuItem.getID()){
		case MENU_PLAY:
			Toast.makeText(MainMenu.this,  "Play Selected", Toast.LENGTH_SHORT).show();
			mHandler.postDelayed(mLaunchPlay, 1000);
			return true;
		case MENU_ABOUT:
			Toast.makeText(MainMenu.this,  "About Selected", Toast.LENGTH_SHORT).show();
			mHandler.postDelayed(mLaunchAbout, 1000);
			return true;
		case MENU_HELP:
			Toast.makeText(MainMenu.this,  "Help Selected", Toast.LENGTH_SHORT).show();
			return true;
		case MENU_QUIT:
			this.finish();
			return true;
		default:
			return false;
		}
	}
	
	private Runnable mLaunchAbout = new Runnable() {
        public void run() {
    		Intent myIntent = new Intent(MainMenu.this, About.class);
    		MainMenu.this.startActivity(myIntent);
        }
     };
     
    private Runnable mLaunchPlay = new Runnable() {
        public void run() {
    		Intent myIntent = new Intent(MainMenu.this, Game.class);
    		MainMenu.this.startActivity(myIntent);
        }
     };
	
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

}
