package com.mycompany.bubblegame;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;

public class EndGameScreen implements Screen
{
	SpriteBatch batch;
	OrthographicCamera camera;
	BitmapFont font;
	
	TextureRegion retry;
	Rectangle retry_position;
	boolean retry_touched = false;
	TextureRegion resume;
	
	TextureRegion menu;
	Rectangle menu_position;
	boolean menu_touched = false;
	
	Vector2 font_position;
	
	float scr_width;
	float scr_height;
	
	BubbleGame game;
	public EndGameScreen(BubbleGame game)
	{
		this.game = game;
		game.orientation.setOrientation(GameOrientation.PORTRAIT);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
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
		//font.drawMultiLine(batch, "Screen Orientation\n(Portrait)", font_position.x, font_position.y);
		font.draw(batch, "Point: "+(int)(game.game_screen.score_number), font_position.x, font_position.y);
		
		if ( game.game_screen.game_complete)
		{
			batch.draw(retry, retry_position.x, retry_position.y, retry_position.width, retry_position.height);
		}
		else
		{
			batch.draw(resume, retry_position.x, retry_position.y, retry_position.width, retry_position.height);
		}
		batch.draw(menu, menu_position.x, menu_position.y, menu_position.width, menu_position.height);
		//font.draw(batch, "Reset highest score", score_position.x, score_position.y);

		batch.end();
		
		// button to bring circles down
		if (!Gdx.input.isTouched() && retry_touched)
		{
			//game.records.putFloat("highest", 0f);
			//game.records.flush();
			if (game.game_screen.game_complete)
			{
				game.orientation.setOrientatonChanged(true);
			}
			
			game.setScreen(game.game_screen);
			retry_touched = false;

		}
		else if (Gdx.input.isTouched() && retry_position.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
		{
			retry_touched = true;
		}
		
		if (!Gdx.input.isTouched() && menu_touched)
		{
			if (game.records.getFloat("highest",0f) < game.game_screen.score_number)
			{
				game.records.putFloat("highest", game.game_screen.score_number);
				game.records.flush();
			}
			//game.records.putFloat("highest", 0f);
			//game.records.flush();
			//game.orientation.submitScore((int)game.game_screen.score_number);
			game.orientation.showAd();
			game.orientation.setOrientatonChanged(true);
			game.setScreen(game.menu_screen);
			menu_touched = false;

		}
		else if (Gdx.input.isTouched() && menu_position.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
		{
			menu_touched = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.BACK))
		{
			if (game.records.getFloat("highest",0f) < game.game_screen.score_number)
			{
				game.records.putFloat("highest", game.game_screen.score_number);
				game.records.flush();
			}
			game.orientation.showAd();
			game.orientation.setOrientatonChanged(true);
			game.setScreen(game.menu_screen);
		}
		
	}

	public void configureCamera() {
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void initialize() {
		configureCamera();
		scr_width = Gdx.graphics.getWidth();
		scr_height = Gdx.graphics.getHeight();
		int font_size;
		if (scr_height > scr_width)
		{
			retry_position = new Rectangle(scr_width/2 +scr_height/20, scr_height*2/5, scr_height/5, scr_height/10);
			menu_position = new Rectangle(scr_width/2-scr_height/5 -scr_height/20, scr_height*2/5, scr_height/5, scr_height/10);
			font_position = new Vector2(scr_width/4, scr_height*3/5);
			font_size = (int) scr_height/30;
		}
		else
		{
			retry_position = new Rectangle(scr_width/2 +scr_height/5, scr_height*2/8, scr_height/3, scr_height/6);
			menu_position = new Rectangle(scr_width/2-scr_height/3-scr_height/5, scr_height*2/8, scr_height/3, scr_height/6);
			font_position = new Vector2(scr_width/3, scr_height*3/4);
			font_size = (int) scr_width/20;
		}

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ethnocentric_rg.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = font_size;
		parameter.color = Color.BLACK;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); 
	}
	
	@Override
	public void resize(int p1, int p2)
	{
		// TODO: Implement this method
		initialize();
	}

	@Override
	public void show()
	{
		// TODO: Implement this method
		Texture texture_retry = game.asset_manager.get("retry_button.png", Texture.class);
		Texture texture_menu = game.asset_manager.get("menu_button_end.png", Texture.class);
		Texture texture_resume = game.asset_manager.get("resume_button.png", Texture.class);
		retry = new TextureRegion(texture_retry);
		menu = new TextureRegion(texture_menu);
		resume = new TextureRegion(texture_resume);
		
		//initialize();
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
	}
	
}
