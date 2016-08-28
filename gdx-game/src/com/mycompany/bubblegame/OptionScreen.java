package com.mycompany.bubblegame;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;

public class OptionScreen implements Screen
{
	BubbleGame game;
	SpriteBatch batch;
	OrthographicCamera camera;
	//Texture check;
	//Texture uncheck;
	TextureRegion check_button;
	TextureRegion uncheck_button;
	TextureRegion reset_button;
	Rectangle check_position;
	BitmapFont font;
	Vector2 font_position;
	Rectangle score_position;
	Rectangle reset_position;
	
	float scr_width;
	float scr_height;
	boolean checked = true;
	boolean prev_check_touched = false;
	boolean prev_score_touched = false;
	
	TextureRegion background;
	Rectangle background_position;
	
	public OptionScreen(BubbleGame game)
	{
		this.game = game;
		//font = game.font;
		game.orientation.setOrientation(GameOrientation.PORTRAIT);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		//configureCamera();
		
		
		Gdx.input.setCatchBackKey(true);
		
		//font = new BitmapFont();
		//font.setScale(4f, 4f);
		//font.setColor(Color.BLACK);
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
		batch.draw(background, background_position.x, background_position.y, background_position.width, background_position.height);
		font.draw(batch, "Screen Orientation\n(Portrait)", font_position.x, font_position.y);
		if (checked)
		{
			batch.draw(check_button, check_position.x, check_position.y, check_position.width, check_position.height);
		}
		else 
		{
			batch.draw(uncheck_button, check_position.x, check_position.y, check_position.width, check_position.height);
		}
		batch.draw(reset_button, reset_position.x, reset_position.y, reset_position.width, reset_position.height);
		font.draw(batch, "Reset highest score", score_position.x, score_position.y);
		
		batch.end();
		
		// button to bring circles down
		if (!Gdx.input.isTouched() && prev_check_touched)
		{

			if (checked)
			{
				checked = false;
				game.orientation.setOrientation(GameOrientation.LANDSCAPE);
				initialize();
			}
				
			else
			{
				checked = true;
				game.orientation.setOrientation(GameOrientation.PORTRAIT);
				initialize();
			}
			game.orientation.setOrientatonChanged(true);
			prev_check_touched = false;
				
		}
		else if (Gdx.input.isTouched() && check_position.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
		{
			prev_check_touched = true;
		}
		
		// button to bring circles down
		if (!Gdx.input.isTouched() && prev_score_touched)
		{
			//game.records.putFloat("highest", 0f);
			//game.records.flush();
			//game.orientation.showScores();
			prev_score_touched = false;

		}
		else if (Gdx.input.isTouched() && reset_position.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
		{
			prev_score_touched = true;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.BACK))
		{
			//game.orientation.showAd();
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
			check_position = new Rectangle(scr_width/2 +scr_height/5, scr_height*3/5, scr_height/20, scr_height/20);
			font_position = new Vector2(scr_width/2-scr_height/5, scr_height*3/5+scr_height/25);
			score_position = new Rectangle(scr_width/2-scr_height/5, scr_height*2/5+scr_height/25, scr_height/4, scr_height/20);
			reset_position = new Rectangle(scr_width/2+scr_height/5, scr_height*2/5, scr_height/20, scr_height/20);
			font_size = (int) scr_width/25;
			background_position = new Rectangle(((scr_width/2)-((background.getRegionWidth()/2)*scr_height/background.getRegionHeight())), 0, background.getRegionWidth()*scr_height/background.getRegionHeight(), scr_height);
		}
		else
		{
			check_position = new Rectangle(scr_width*4/5, scr_height*3/5, scr_height/10, scr_height/10);
			font_position = new Vector2(scr_width/5, scr_height*3/5+scr_height/15);
			score_position = new Rectangle(scr_width/5, scr_height*2/5+scr_height/15, scr_height/2, scr_height/10);
			reset_position = new Rectangle(scr_width*4/5, scr_height*2/5, scr_height/10, scr_height/10);
			font_size = (int) scr_width/30;
			background_position = new Rectangle(0, ((scr_height/2)-((background.getRegionHeight()/2)*scr_width/background.getRegionWidth())), scr_width, background.getRegionHeight()*scr_width/background.getRegionWidth());
		}
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ethnocentric_rg.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = font_size;
		parameter.color = Color.WHITE;
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
		
		Texture check = game.asset_manager.get("option_button.png", Texture.class);
		Texture uncheck = game.asset_manager.get("option_button.png", Texture.class);
		Texture reset = game.asset_manager.get("option_button.png", Texture.class);
		check_button = new TextureRegion(check);
		uncheck_button = new TextureRegion(uncheck);
		reset_button = new TextureRegion(reset);
		
		Texture texture_background = game.asset_manager.get("background5.jpg" , Texture.class);

		background = new TextureRegion(texture_background);
		
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
	}
	
}
