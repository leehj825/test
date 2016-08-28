package com.mycompany.bubblegame;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;

public class MainMenu implements Screen 
{
	BubbleGame game;
	SpriteBatch batch;
	OrthographicCamera camera;
	BitmapFont font;
	//Texture play;
	TextureRegion play_button;
	Rectangle play_position;
	//Texture option;
	TextureRegion option_button;
	Rectangle option_position;
	//Texture exit;
	TextureRegion exit_button;
	Rectangle exit_position;
	
	TextureRegion title;
	Rectangle title_position;
	
	TextureRegion background;
	Rectangle background_position;
	
	float scr_width;
	float scr_height;
	
	Vector2 highest_score_location;
	
	
	//Stage stage;
	
	public MainMenu(BubbleGame game)
	{
		this.game = game;
		game.orientation.setOrientation(GameOrientation.PORTRAIT);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		//font = game.font;
		//configureCamera();
		//play = new Texture(Gdx.files.internal("button_play.png"));
		//option = new Texture(Gdx.files.internal("button_option.png"));
		//exit = new Texture(Gdx.files.internal("button_exit.png"));
		//Texture play = game.asset_manager.get("data/button_play.png", Texture.class);
		/*option = game.asset_manager.get("button_option.png", Texture.class);
		exit = game.asset_manager.get("button_exit.png", Texture.class);*/
		
		//play_button = new TextureRegion(play);
		//option_button = new TextureRegion(option);
		//exit_button = new TextureRegion(exit);
		
		//Gdx.input.setCatchBackKey(true);
		
		//initialize();
	}

	@Override
	public void render(float p1)
	{
	    Gdx.gl.glClearColor(0.7f, 1f, 1f,0.7f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
        batch.begin();
		//batch.draw(background, 0-(background.getRegionWidth()-scr_width)/2, 0-(background.getRegionHeight()-scr_height), background.getRegionWidth(), background.getRegionHeight());
		batch.draw(background, background_position.x, background_position.y, background_position.width, background_position.height);
		batch.draw(title, title_position.x, title_position.y, title_position.width, title_position.height);
		batch.draw(play_button, play_position.x, play_position.y, play_position.width, play_position.height);
		batch.draw(option_button, option_position.x, option_position.y, option_position.width, option_position.height);
		batch.draw(exit_button, exit_position.x, exit_position.y, exit_position.width, exit_position.height);
		font.draw(batch, "Highest Point: "+(int)(game.records.getFloat("highest")), highest_score_location.x, highest_score_location.y);
		batch.end();
		if (Gdx.input.isTouched() && play_position.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
		{
			//game.game_screen.initialize();
			//game.game_screen.resetGame();
			game.setScreen(game.game_screen);
		}
		
		if (Gdx.input.isTouched() && option_position.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
		{
			//game.orientation.setOrientation(GameOrientation.LANDSCAPE);
			game.setScreen(game.option_screen);
			
		}
		
		if (Gdx.input.isTouched() && exit_position.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
		{
			//game.setScreen(game.game_screen);
			Gdx.app.exit();
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
			play_position = new Rectangle(scr_width/2 -scr_height/7, scr_height*4/8, scr_height*2/7, scr_height/7-10);
			option_position = new Rectangle(scr_width/2-scr_height/7, scr_height*3/8, scr_height*2/7, scr_height/7-10);
			exit_position = new Rectangle(scr_width/2-scr_height/7, scr_height*2/8, scr_height*2/7, scr_height/7-10);
			highest_score_location = new Vector2(scr_width/2-scr_height/5, scr_height/5-20);
			font_size = (int) scr_width/25;
			background_position = new Rectangle(((scr_width/2)-((background.getRegionWidth()/2)*scr_height/background.getRegionHeight())), 0, background.getRegionWidth()*scr_height/background.getRegionHeight(), scr_height);
			title_position = new Rectangle(scr_width/2- scr_height*2/7, scr_height*5/8, scr_height*4/7, scr_height*2/7-10);
		}
		else
		{
			play_position = new Rectangle(scr_width*1/5, scr_height*2/5, scr_height*2/5, scr_height/5);
			option_position = new Rectangle(scr_width*2/5, scr_height*2/5, scr_height*2/5, scr_height/5);
			exit_position = new Rectangle(scr_width*3/5, scr_height*2/5, scr_height*2/5, scr_height/5);
			highest_score_location = new Vector2(scr_width/2-scr_height*2/5, scr_height/5);
			font_size = (int) scr_width/30;
			background_position = new Rectangle(0, ((scr_height/2)-((background.getRegionHeight()/2)*scr_width/background.getRegionWidth())), scr_width, background.getRegionHeight()*scr_width/background.getRegionWidth());
			title_position = new Rectangle(scr_width/10, scr_height*3/5, scr_width*4/5, scr_height*2/5);
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
		initialize();
		// TODO: Implement this method
	}

	@Override
	public void show()
	{
		// TODO: Implement this method
		Texture play = game.asset_manager.get("data/button_play.png", Texture.class);
		//play = new Texture(Gdx.files.internal("button_play.png"));
		play_button = new TextureRegion(play);
		
		Texture option = game.asset_manager.get("data/button_option.png", Texture.class);
		Texture exit = game.asset_manager.get("data/button_exit.png", Texture.class);

		//play_button = new TextureRegion(play);
		option_button = new TextureRegion(option);
		exit_button = new TextureRegion(exit);
		
		Texture title_texture = game.asset_manager.get("GameTitle.png", Texture.class);
		
		title = new TextureRegion(title_texture);
		
		//font = new BitmapFont();
		//font.setColor(Color.BLACK);
		//font.setScale(5f, 5f);
		
		Texture texture_background = game.asset_manager.get("background3.jpg" , Texture.class);
		
		background = new TextureRegion(texture_background);
		
		initialize();
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
