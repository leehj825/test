package com.mycompany.bubblegame;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;


public class SplashScreen implements Screen
{
	BubbleGame game;
	SpriteBatch batch;
	BitmapFont font;
	OrthographicCamera camera;
	//AssetManager asset_manager;
	boolean is_touched =false;

	public SplashScreen(BubbleGame game)
	{
		this.game = game;
		game.orientation.setOrientation(GameOrientation.PORTRAIT);
		camera = new OrthographicCamera();
		//font = game.font;
		
		font = new BitmapFont();
		batch = new SpriteBatch();
		//asset_manager = new AssetManager();
		//game.load();
	}
	@Override
	public void render(float p1)
	{
		// TODO: Implement this method
		Gdx.gl.glClearColor(0.7f, 1f, 1f,0.7f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		//font.setColor(Color.BLACK);
		//font.setScale(5f, 5f);
		font.draw(batch, "Loading Game", Gdx.graphics.getWidth()/7, Gdx.graphics.getHeight()/2);
		batch.end();
		
		/*if (is_touched && !Gdx.input.isTouched())
		{
			game.setScreen(game.menu_screen);
		}*/
		if (game.asset_manager.update())
		{
			game.setScreen(game.menu_screen);
		}
		//if (Gdx.input.isTouched())
		//{
		//	is_touched = true;
			
		//}
		
	}

	
	@Override
	public void resize(int p1, int p2)
	{
		// TODO: Implement this method
	}
	
	@Override
	public void show()
	{
		// TODO: Implement this method
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ethnocentric_rg.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int)Gdx.graphics.getWidth()/15;
		parameter.color = Color.BLACK;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose();
	}

	@Override
	public void hide()
	{
		// TODO: Implement this method
	}

	@Override
	public void pause()
	{
		// TODO: Implement this method
	}

	@Override
	public void resume()
	{
		// TODO: Implement this method
	}

	@Override
	public void dispose()
	{
		// TODO: Implement this method
		batch.dispose();
		font.dispose();
	}
	
}
