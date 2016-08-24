package com.mycompany.bubblegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
import java.util.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
//import android.content.res.*;
//import android.content.*;


public class BubbleGame extends Game
{
	public final GameOrientation orientation;
	GameScreen game_screen;
	MainMenu menu_screen;
	SplashScreen splash_screen;
	OptionScreen option_screen;
	EndGameScreen end_screen;
	AssetManager asset_manager;
	public Preferences records;
	//public BitmapFont font;
	
	public BubbleGame(GameOrientation orientation)
	{
		this.orientation = orientation;

		asset_manager = new AssetManager();
		
		//records = new Preferences();
		//records = Gdx.app.getPreferences("com.magicbubble.MyRecords");
		//records.flush();
		/*FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixel.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 50;
		parameter.color = Color.BLACK;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); */
		
		load();
	}
	@Override
	public void create()
	{
		splash_screen = new SplashScreen(this);
		setScreen(splash_screen);
		// currently dont mean anything
		// later, asset managed is needed, use this splash screen
		
		game_screen = new GameScreen(this);
		menu_screen = new MainMenu(this);
		option_screen = new OptionScreen(this);
		end_screen = new EndGameScreen(this);
		records = Gdx.app.getPreferences("MyRecord");
		//setScreen(menu_screen);
	}

	@Override
	public void render()
	{        
	    super.render();
	}

	public void load()
	{
		asset_manager.load("data/button_play.png", Texture.class);
		asset_manager.load("data/button_option.png", Texture.class);
		asset_manager.load("data/button_exit.png", Texture.class);
		
		asset_manager.load("bubble_red.png", Texture.class);
		asset_manager.load("bubble_red2.png", Texture.class);
		asset_manager.load("bubble_red3.png", Texture.class);
		asset_manager.load("bubble_red4.png", Texture.class);

		asset_manager.load("bubble_blue.png", Texture.class);
		asset_manager.load("bubble_blue2.png", Texture.class);
		asset_manager.load("bubble_blue3.png", Texture.class);
		asset_manager.load("bubble_blue4.png", Texture.class);

		asset_manager.load("bubble_green.png", Texture.class);
		asset_manager.load("bubble_green2.png", Texture.class);
		asset_manager.load("bubble_green3.png", Texture.class);
		asset_manager.load("bubble_green4.png", Texture.class);

		asset_manager.load("down_arrow.jpg", Texture.class);
		asset_manager.load("rotate_arrow.jpg", Texture.class);
		
		asset_manager.load("bubblehit2.mp3", Sound.class);
		asset_manager.load("bubblepop3.wav", Sound.class);
		asset_manager.load("charm.mp3", Sound.class);
		
		asset_manager.load("option_button.png", Texture.class);
		//asset_manager.load("uncheck.png", Texture.class);
		asset_manager.load("combo_list4.png", Texture.class);
		asset_manager.load("right_arrow.jpg", Texture.class);
		asset_manager.load("left_arrow.jpg", Texture.class);
		asset_manager.load("retry_button.png", Texture.class);
		asset_manager.load("menu_button_end.png", Texture.class);

		asset_manager.load("background4.jpg", Texture.class);
		asset_manager.load("background2.jpg", Texture.class);
		asset_manager.load("background3.jpg", Texture.class);
		asset_manager.load("background5.jpg", Texture.class);
		//asset_manager.load("Pixel.ttf", FreeType.class);
		//asset_manager.load("combo.png", Texture.class);
		//asset_manager.load("combo1.png", Texture.class);
		
		asset_manager.load("blue_glow.png", Texture.class);
		asset_manager.load("red_glow.png", Texture.class);
		asset_manager.load("green_glow.png", Texture.class);
		
		asset_manager.load("glow_horizental.png", Texture.class);
		asset_manager.load("glow_vertical.png", Texture.class);
		
		asset_manager.load("GameTitle.png", Texture.class);
	}
	
	@Override
	public void dispose()
	{
		asset_manager.dispose();
		records.flush();
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
	
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
	
	
}
