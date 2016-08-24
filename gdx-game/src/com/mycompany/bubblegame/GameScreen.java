package com.mycompany.bubblegame;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import java.util.*;
import android.animation.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;


public class GameScreen implements Screen
{
	OrthographicCamera camera;
	ShapeRenderer shape;
	
	boolean circle_settled;
	int number_of_circle;
	int[] current_index;
	int number_of_moving;
	int[][] overall_index;

	List<Vector2> circle_pos;
	List<Color> circle_color;
	List<Boolean> circle_settle;
	List<Boolean> circle_removed;
	List<Boolean> circle_glow;
	boolean prev_touch;
	boolean initial_touch;
	float prev_y;
	float initial_x;
	int circle_size;
	int move_speed;
	int original_speed;
	int fast_speed;
	int medium_speed;
	int game_speed; // increase as time for harder to pkay

	int column_number;
	int row_number;
	int[] column_height;

	float scr_height;
	float scr_width;
	float border_left;
	float border_right;
	Rectangle special_button_pos;
	boolean special_button_touched = false;
	//boolean special_button_touched1 = false;
	Rectangle border;

	Rectangle turn_button_pos;
	boolean turn_button_touched = false;
	//boolean turn_button_touched1 = false;

	float time=0.0f;
	float ani_time=0.0f;
	float combo_time=0.0f;

	TextureRegion red_bubble;
	TextureRegion[] red_frames;
	Animation red_ani;

	TextureRegion blue_bubble;
	TextureRegion[] blue_frames;
	Animation blue_ani;

	
	TextureRegion green_bubble;
	TextureRegion[] green_frames;
	Animation green_ani;
	
	TextureRegion blue_glow;
	//Animation blue_glow_ani;
	TextureRegion green_glow;
	TextureRegion red_glow;

	TextureRegion down_arrow;

	TextureRegion turn_arrow;
	SpriteBatch batch;

	Sound hit_bottom;
	long sound_id;
	boolean sound_start;
	Sound bubble_pop_sound;
	boolean charm_start;
	Sound charm;

	boolean remove_complete = true;

	BitmapFont score;
	public float score_number;
	float score_shown;
	Vector2 score_position;

	float score_per_circle = 50f;
	float scale_for_combo = 2.0f;
	float combo_number=0.0f;

	TextureRegion combo_image;
	TextureRegion[] combo_frames;
	Animation combo_ani;
	Rectangle combo_position;
	Rectangle combo_position_original;
	boolean combo_ani_start = false;
	
	Animation glow_horizon_ani;
	Animation glow_vertical_ani;
	
	boolean glow_sound_start;
	
	boolean glow_touched;
	
	Rectangle left_button_pos;
	boolean left_button_touched = false;
	TextureRegion left_button;

	Rectangle right_button_pos;
	boolean right_button_touched = false;
	TextureRegion right_button;
	
	TextureRegion background;
	Rectangle background_position;
	
	BubbleGame game;
	
	public GameScreen(BubbleGame game)
	{
		this.game = game;
		
		Gdx.input.setCatchBackKey(true);
		
		camera = new OrthographicCamera();
		configureCamera();
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		//circle_position.x = Gdx.graphics.getWidth()/2;
		//circle_position.y = Gdx.graphics.getHeight();
		circle_pos = new ArrayList<Vector2>();
		circle_color = new ArrayList<Color>();
		circle_settle = new ArrayList<Boolean>();
		circle_removed = new ArrayList<Boolean>();
		circle_glow = new ArrayList<Boolean>();
		column_number = 9;
		row_number = 19;

		overall_index = new int[column_number][row_number+3];
		number_of_moving = 3;


		column_height = new int[column_number];
		current_index = new int[number_of_moving];

		//score = new BitmapFont();
		
		score_position = new Vector2();
	}

	@Override
	public void render(float p1)
	{
		// TODO: Implement this method
		Gdx.gl.glClearColor(0.7f, 1f, 1f,0.7f);
	    //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camera.update();

		drawAllCircle();

		moveCircle();

		// reach to bottom
		reachBottom();

		if (isAllSettled())
		{
			// remove any three or more consecutive circles with samecolor
			removeMatchColor();
			// check any glow one being removed
			removeGlowColor();
		}
		if ( blue_ani.isAnimationFinished(ani_time) && green_ani.isAnimationFinished(ani_time) && red_ani.isAnimationFinished(ani_time)&&!remove_complete)
		{
			resetPosition();
		}
		// add check if there is no empty slot
		if (isAllSettled()&&remove_complete)
		{
			// add dump circles here certain time frame
			//   or add new circles
			addNewCircle();
		}

		for (int i=0; i<column_number; i++)
		{
			if (column_height[i] >= scr_height)
			{
				//resetGame();
				// we should save point to file
				if (game.records.getFloat("highest",0f) < score_number)
				{
					game.records.putFloat("highest", score_number);
					game.records.flush();
				}
				game.setScreen(game.end_screen);
			}
				
		}
	}

	public void drawAllCircle()
	{

		
		if (Gdx.input.isKeyPressed(Input.Keys.BACK))
		{
			if (game.records.getFloat("highest",0f) < score_number)
			{
				game.records.putFloat("highest", score_number);
				game.records.flush();
			}
			game.setScreen(game.end_screen);
		}

		batch.setProjectionMatrix(camera.combined);
        batch.begin();
		
		//batch.draw(background, 0, 0, scr_width*scr_height/background.getRegionHeight(), scr_height);
		batch.draw(background, background_position.x, background_position.y, background_position.width, background_position.height);
		batch.end();
		
		shape.setProjectionMatrix(camera.combined);
		shape.begin(ShapeRenderer.ShapeType.Filled);

		shape.setColor(Color.BLACK);
		shape.rect(border_left-10, border.getY(), 10, scr_height);
		shape.rect(border_right, border.getY(), 10, scr_height);
		shape.rect(border_left-10, border.getY()-10, border_right-border_left+20, 10);

		shape.end();
		
		batch.begin();
		
		//boolean glow_ani_start = false;
		for (int i=0; i < circle_pos.size(); i++)
		{
			if (circle_removed.get(i).equals(Boolean.FALSE))
			{
				if (circle_color.get(i).equals(Color.RED))
				{
					if (circle_glow.get(i).equals(Boolean.FALSE))
					{
						batch.draw(red_bubble, circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
					}
					else
					{
						batch.draw(red_glow, circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
					}
					//batch.draw(red_ani.getKeyFrame(time, true),circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
				} 
				else if (circle_color.get(i).equals(Color.BLUE))
				{
					if (circle_glow.get(i).equals(Boolean.FALSE))
					{
						batch.draw(blue_bubble, circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
					}
					else
					{
						batch.draw(blue_glow, circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
					}
					//batch.draw(blue_bubble, circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
					//batch.draw(blue_glow_ani.getKeyFrame(time), circle_pos.get(i).x-circle_size*2/3, circle_pos.get(i).y-circle_size*2/3, circle_size*4/3, circle_size*4/3);
				}
				else if (circle_color.get(i).equals(Color.GREEN))
				{
					if (circle_glow.get(i).equals(Boolean.FALSE))
					{
						batch.draw(green_bubble, circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
					}
					else
					{
						batch.draw(green_glow, circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
					}
					//batch.draw(green_bubble, circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
				}
			}
			else
			{
				if (circle_color.get(i).equals(Color.RED))
				{
					batch.draw(red_ani.getKeyFrame(ani_time),circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
				} 
				else if (circle_color.get(i).equals(Color.BLUE))
				{
					batch.draw(blue_ani.getKeyFrame(ani_time),circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
				}
				else if (circle_color.get(i).equals(Color.GREEN))
				{
					batch.draw(green_ani.getKeyFrame(ani_time),circle_pos.get(i).x-circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
				}
				
			}
		}
		for (int i=0; i < circle_pos.size(); i++)
		{
			if (circle_removed.get(i).equals(Boolean.TRUE))
			{
				
				if (circle_glow.get(i).equals(Boolean.TRUE))
				{
					batch.draw(glow_horizon_ani.getKeyFrame(ani_time), border_left, circle_pos.get(i).y-circle_size/4, circle_size*column_number, circle_size/2);
					batch.draw(glow_vertical_ani.getKeyFrame(ani_time), circle_pos.get(i).x-circle_size/4, border.y, circle_size/2, circle_size*row_number);
					if (!glow_sound_start)
					{
						charm.play();
						glow_sound_start = true;
					}
				}

			}
		}
		
		ani_time += Gdx.graphics.getDeltaTime();
		//if (combo_ani_start)
		//{
			//if ((int)(combo_number)%2 == 0)
			//{
		
		if ( combo_time <1.0f)
		{
			batch.draw(combo_ani.getKeyFrame(combo_time), combo_position.x, combo_position.y, combo_position.width, combo_position.height);
			//batch.draw(combo_image, border_left, border.y, circle_size*5, circle_size);
		}
		else if (combo_number == 0f)
		{
			combo_position.set(combo_position_original);
		}
		combo_time += Gdx.graphics.getDeltaTime();
			/*}
			else
			{
				batch.draw(combo_ani[1].getKeyFrame(combo_time), border_right, border.y, circle_size*5, circle_size);
			}*/
			
		//}
		//else
		//	combo_time = 0f;*/
			
		batch.draw(turn_arrow, turn_button_pos.x, turn_button_pos.y, turn_button_pos.width, turn_button_pos.height);
		batch.draw(down_arrow, special_button_pos.x, special_button_pos.y, special_button_pos.width, special_button_pos.height);
		batch.draw(left_button, left_button_pos.x, left_button_pos.y, left_button_pos.width, left_button_pos.height);
		batch.draw(right_button, right_button_pos.x, right_button_pos.y, right_button_pos.width, right_button_pos.height);
		
		score.draw(batch, "Point:" + (int)(score_shown), score_position.x, score_position.y);
		//score.draw(batch, "combo:" + (int)(combo_number), score_position.x, score_position.y-60);
		//score.draw(batch, "speed:" + (move_speed), score_position.x, score_position.y-120);
        //score.draw(batch, "time:" + (int)(time/2), score_position.x, score_position.y-180);
		batch.end();
		
		

		if ( blue_ani.isAnimationFinished(ani_time) && green_ani.isAnimationFinished(ani_time) && red_ani.isAnimationFinished(ani_time)&&!remove_complete)
		{
			bubble_pop_sound.play();
			
		}
		
		if ( glow_vertical_ani.isAnimationFinished(ani_time) && glow_horizon_ani.isAnimationFinished(ani_time) && !remove_complete)
		{
			glow_sound_start = false;
		}

		time += Gdx.graphics.getDeltaTime();

		if (score_shown < score_number)
		{
			if ((score_number-score_shown) >1000)
			{
				score_shown +=1000;
			}
			else if ((score_number-score_shown) >100)
			{
				score_shown += 100;
			}
			else
			{
				score_shown += 1;
			}
			
		}


		/*if (combo_ani[0].isAnimationFinished(combo_time))
		{
			combo_ani_start=false;
			//combo_time = 0f;
		}*/
			
		/*if (red_ani.isAnimationFinished(ani_time)) 
		 {
		 //ani_time=0.0f;
		 remove_complete=true;
		 }*/
		//combo_time += Gdx.graphics.getDeltaTime();
		time += Gdx.graphics.getDeltaTime();

	}

	public void moveCircle()
	{
		if (!sound_start && (move_speed == fast_speed))
		{
			hit_bottom.play();
			sound_start=true;
		}

		// move all unsetled circles
		for (int i=0; i<circle_pos.size(); i++)
		{
			if (circle_settle.get(i).equals(Boolean.FALSE)
				&& circle_removed.get(i).equals(Boolean.FALSE))
			{
				circle_pos.get(i).y -= move_speed;
			}

		}

		if (move_speed!=medium_speed)
		{
			int most_bottom_index = 0;
			int most_left_index = 2;
			int most_right_index = 0;
			int current_column = getColumn(current_index[most_bottom_index]);
			int left_column = getColumn(current_index[most_left_index]);
			int right_column = getColumn(current_index[most_right_index]);
			
			// button to bring circles down
			if (!Gdx.input.isTouched() && special_button_touched)
			{

				move_speed = fast_speed;
				special_button_touched = false;
				//hit_bottom.play();
			}
			else if (!prev_touch && Gdx.input.isTouched() && special_button_pos.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
			{
				special_button_touched = true;
			}

			if (!Gdx.input.isTouched() && turn_button_touched)
			{
				turnCircles();

				turn_button_touched = false;
			}
			else if (!prev_touch && Gdx.input.isTouched() && turn_button_pos.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
			{
				turn_button_touched = true;
			}
			
			// button to bring circles left
			if (!Gdx.input.isTouched() && left_button_touched)
			{

				if ((current_column > 0) && (left_column>0))
				{
					if (!(circle_pos.get(current_index[most_bottom_index]).y < column_height[current_column-1])
						&& !(circle_pos.get(current_index[most_left_index]).y < column_height[left_column -1]))
					{
						for (int i=0; i<number_of_moving; i++)
						{
							circle_pos.get(current_index[i]).x -= circle_size;
						}
					}
				}
				left_button_touched = false;
				//hit_bottom.play();
			}
			else if (!prev_touch && Gdx.input.isTouched() && left_button_pos.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
			{
				left_button_touched = true;
			}

			if (!Gdx.input.isTouched() && right_button_touched)
			{
				if ((current_column < (column_number-1)) && (right_column < (column_number-1)))
				{
					if (!(circle_pos.get(current_index[most_bottom_index]).y < column_height[current_column+1])
						&&  !(circle_pos.get(current_index[most_right_index]).y < column_height[right_column+1]) )
					{
						for (int i=0; i<number_of_moving; i++)
						{
							circle_pos.get(current_index[i]).x += circle_size;
						}
					}
				}
				right_button_touched = false;
			}
			else if (!prev_touch && Gdx.input.isTouched() && right_button_pos.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
			{
				right_button_touched = true;
			}
			

			/*if (!Gdx.input.isTouched() && glow_touched)
			{
				if ((current_column < (column_number-1)) && (right_column < (column_number-1)))
				{
					if (!(circle_pos.get(current_index[most_bottom_index]).y < column_height[current_column+1])
						&&  !(circle_pos.get(current_index[most_right_index]).y < column_height[right_column+1]) )
					{
						for (int i=0; i<number_of_moving; i++)
						{
							circle_pos.get(current_index[i]).x += circle_size;
						}
					}
				}
				glow_touched = false;
			}
			else */
	        /*for (int i=0; i<circle_pos.size(); i++)
			{
				Rectangle temp = new Rectangle(circle_pos.get(i).x -circle_size/2, circle_pos.get(i).y-circle_size/2, circle_size, circle_size);
				if (!prev_touch && Gdx.input.isTouched() && circle_glow.get(i).equals(Boolean.TRUE) && temp.contains(Gdx.input.getX(), scr_height-Gdx.input.getY()))
				{
					circle_removed.set(i, Boolean.TRUE);
					removeGlowColor();
					resetPosition();
					//glow_touched = true;
				}
			}*/
			// move active circles by touch
			//if (Gdx.input.isTouched() && border.contains(Gdx.input.getX(), Gdx.input.getY()))
			if (Gdx.input.isTouched())
			{
				if (prev_touch)
				{
					/*int most_bottom_index = 0;
					int most_left_index = 2;
					int most_right_index = 0;
					int current_column = getColumn(current_index[most_bottom_index]);
					int left_column = getColumn(current_index[most_left_index]);
					int right_column = getColumn(current_index[most_right_index]);*/
					if (Math.abs( initial_x - Gdx.input.getX()) > circle_size)
					{
						if (initial_x > Gdx.input.getX())
						{
							if ((current_column > 0) && (left_column>0))
							{
								if (!(circle_pos.get(current_index[most_bottom_index]).y < column_height[current_column-1])
									&& !(circle_pos.get(current_index[most_left_index]).y < column_height[left_column -1]))
								{
									for (int i=0; i<number_of_moving; i++)
									{
										circle_pos.get(current_index[i]).x -= circle_size;
									}
								}
							}
						}
						else
						{
							if ((current_column < (column_number-1)) && (right_column < (column_number-1)))
							{
								if (!(circle_pos.get(current_index[most_bottom_index]).y < column_height[current_column+1])
									&&  !(circle_pos.get(current_index[most_right_index]).y < column_height[right_column+1]) )
								{
									for (int i=0; i<number_of_moving; i++)
									{
										circle_pos.get(current_index[i]).x += circle_size;
									}
								}
							}
						}
						initial_x = Gdx.input.getX();
					}

					if (!(circle_pos.get(current_index[most_bottom_index]).y < column_height[current_column])
						&& ((Gdx.input.getY() - prev_y) > (circle_size*4)))
					{
						move_speed = fast_speed;
						prev_y = Gdx.input.getY();
						//hit_bottom.play();
					}
					
					if (!(circle_pos.get(current_index[most_bottom_index]).y < column_height[current_column])
						&& ((prev_y - Gdx.input.getY()) > (circle_size*3)))
					{
						//move_speed = fast_speed;
						turnCircles();
						prev_y = Gdx.input.getY();
						//hit_bottom.play();
					}
				}
				else
				{
					initial_x = Gdx.input.getX();
					prev_y = Gdx.input.getY();
				}
				prev_touch = true;
			}
			else
			{
				prev_touch = false;
			}

		}

	}

	public void reachBottom()
	{
		for (int i=0; i<circle_pos.size(); i++)
		{
			int current_column = getColumn(i);
			if ((circle_pos.get(i).y <= (column_height[current_column])) 
				&& (circle_settle.get(i).equals( Boolean.FALSE))
				&& (circle_removed.get(i).equals(Boolean.FALSE)))
			{
				circle_pos.get(i).y = column_height[current_column];
				circle_settle.set(i, Boolean.TRUE);
				overall_index[getColumn(i)][getRow(i)] = i;
				column_height[current_column] += circle_size;
				move_speed = medium_speed;
				//did_hit = true;
				sound_start=false;
			}
		}
	}

	public void removeMatchColor()
	{
		List<Integer> index_to_remove = new ArrayList<Integer>();
		for (int i=0; i<row_number; i++)
		{
			for ( int j=0; j < (column_number-2); j++)
			{
				if ((overall_index[j][i]!= 0xFF) && (overall_index[j+1][i]!= 0xFF) && (overall_index[j+2][i]!= 0xFF))
				{
					if (circle_color.get(overall_index[j][i]).equals(circle_color.get(overall_index[j+1][i]))
						&& circle_color.get(overall_index[j][i]).equals(circle_color.get(overall_index[j+2][i])))
					{
						index_to_remove.add(Integer.valueOf( overall_index[j][i]));
						index_to_remove.add(Integer.valueOf( overall_index[j+1][i]));
						index_to_remove.add(Integer.valueOf( overall_index[j+2][i]));
					}
				}
			}
		}
		for (int j=0; j<column_number; j++)
		{
			for ( int i=0; i < (row_number-2); i++)
			{
				if ((overall_index[j][i]!= 0xFF) && (overall_index[j][i+1]!= 0xFF) && (overall_index[j][i+2]!= 0xFF))
				{
					if (circle_color.get(overall_index[j][i]).equals(circle_color.get(overall_index[j][i+1]))
						&& circle_color.get(overall_index[j][i]).equals(circle_color.get(overall_index[j][i+2])))
					{
						index_to_remove.add(Integer.valueOf( overall_index[j][i]));
						index_to_remove.add(Integer.valueOf( overall_index[j][i+1]));
						index_to_remove.add(Integer.valueOf( overall_index[j][i+2]));
					}
				}
			}
		}

		
		for ( int k=0; k < index_to_remove.size(); k++)
		{
			circle_removed.set(index_to_remove.get(k).intValue(), Boolean.TRUE);
			overall_index[getColumn(index_to_remove.get(k).intValue())][getRow(index_to_remove.get(k).intValue())] = 0xFF;
			remove_complete=false;
		}
		//drawAllCircle();
		if (index_to_remove.size() >0)
		{
			ani_time=0;
			//combo_time=0.0f;
			//combo_ani_start=true;
		}

	}
	
	public void removeGlowColor()
	{
		List<Integer> index_to_remove = new ArrayList<Integer>();
		/*for (int i=0; i<row_number; i++)
		{
			for ( int j=0; j < (column_number-2); j++)
			{
				if ((overall_index[j][i]!= 0xFF) && (overall_index[j+1][i]!= 0xFF) && (overall_index[j+2][i]!= 0xFF))
				{
					if (circle_color.get(overall_index[j][i]).equals(circle_color.get(overall_index[j+1][i]))
						&& circle_color.get(overall_index[j][i]).equals(circle_color.get(overall_index[j+2][i])))
					{
						index_to_remove.add(Integer.valueOf( overall_index[j][i]));
						index_to_remove.add(Integer.valueOf( overall_index[j+1][i]));
						index_to_remove.add(Integer.valueOf( overall_index[j+2][i]));
					}
				}
			}
		}*/
		for ( int i=0; i < circle_pos.size(); i++)
		{
			if (circle_removed.get(i).equals(Boolean.TRUE) && circle_glow.get(i).equals(Boolean.TRUE))
			{
				for ( int j=0; j < circle_pos.size(); j++)
				{
					//if (circle_color.get(i).equals(circle_color.get(j)) && circle_removed.get(j).equals(Boolean.FALSE))
					if ((getColumn(i) == getColumn(j)) || (getRow(i) == getRow(j)))
					{
						index_to_remove.add(Integer.valueOf(j));
					}
				}
				
			}
		}
		//repeat if any glow one is removed by other glow
		for ( int i=0; i < circle_pos.size(); i++)
		{
			if (circle_removed.get(i).equals(Boolean.TRUE) && circle_glow.get(i).equals(Boolean.TRUE))
			{
				for ( int j=0; j < circle_pos.size(); j++)
				{
					//if (circle_color.get(i).equals(circle_color.get(j)) && circle_removed.get(j).equals(Boolean.FALSE))
					if ((getColumn(i) == getColumn(j)) || (getRow(i) == getRow(j)))
					{
						index_to_remove.add(Integer.valueOf(j));
					}
				}

			}
		}
		

		for ( int k=0; k < index_to_remove.size(); k++)
		{
			circle_removed.set(index_to_remove.get(k).intValue(), Boolean.TRUE);
			overall_index[getColumn(index_to_remove.get(k).intValue())][getRow(index_to_remove.get(k).intValue())] = 0xFF;
		}
		//drawAllCircle();
		if (index_to_remove.size() >0)
		{
			//combo_time=0.0f;
			//combo_ani_start=true;
			charm_start = false;
		}

	}

	public void resetPosition()
	{
		int max=circle_pos.size();
		for ( int i=max-1; i>=0; i--)
		{
			//if(circle_color.get(i).equals(Color.WHITE))
			if (circle_removed.get(i).equals(Boolean.TRUE))
			{
				circle_settle.remove(i);
				circle_color.remove(i);
				circle_pos.remove(i);
				circle_removed.remove(i);
				circle_glow.remove(i);

			}
		}
		/*if (move_speed != medium_speed)
		{
			combo_number = 0;
		}
		else
		{
			combo_number = combo_number + 1;
			combo_ani_start = true;
		}*/
		if (combo_number > 0.0f)
		{
			combo_time = 0.0f;
			combo_position.setWidth(combo_position_original.width*(combo_number+5)/5);
			combo_position.setHeight(combo_position_original.height*(combo_number+5)/5);
		}
		
		if (combo_number>1f)
		{
			int random = new Random().nextInt(circle_pos.size());
			circle_glow.set(random, Boolean.TRUE);
		}
		//combo_ani_start=true;
		//combo_time = 0;
		//score_number += Math.pow(2.0,(max - circle_pos.size()))*score_per_circle + score_per_circle*Math.pow(scale_for_combo, combo_number);
		score_number += (max - circle_pos.size())*score_per_circle* (1+ Math.pow(2, combo_number));
		combo_number = combo_number + 1;
		for (int j=0; j<number_of_moving;j++)
		{
			current_index[j]= circle_pos.size() -3+j;
		}
		for (int m=0; m <circle_settle.size(); m++)
		{
			circle_settle.set(m, Boolean.FALSE);
		}

		for (int n=0; n<column_number; n++)
		{
			column_height[n] = (int) border.getY() + circle_size/2;
		}
		for (int i=0; i<column_number; i++)
		{
			for (int j=0; j<row_number;j++)
			{
				overall_index[i][j] = 0xFF;
			}
		}
		move_speed = medium_speed;

		remove_complete=true;
		//ani_start=false;
		ani_time=0;
		//combo_time=0f;
		//}

	}
	public void addNewCircle()
	{
		if (((int) (time/120) + original_speed < fast_speed))
		{
			move_speed = (int) (time/120) + original_speed;
			if (move_speed == medium_speed)
			{
				move_speed += 1;
			}
		}
		
		combo_number = 0f;
		combo_ani_start = false;
		
		charm_start = false;
		//combo_position.set( combo_position_original);
		for (int i=0; i<number_of_moving; i++)
		{
			// get another circle
			circle_pos.add(new Vector2(scr_width/2, scr_height+i*circle_size));
			circle_color.add(new Color(getRandomColor()));
			circle_settle.add(new Boolean(Boolean.FALSE));
			circle_removed.add(new Boolean(Boolean.FALSE));
			circle_glow.add(new Boolean(Boolean.FALSE));

			current_index[i] += 3;

		}
		// if 1st and 2nd are same color, make sure 3rd one is different
		if (circle_color.get(current_index[0]).equals(circle_color.get(current_index[1])))
		{
			while (circle_color.get(current_index[0]).equals(circle_color.get(current_index[2])))
			{
				circle_color.set(current_index[2], getRandomColor());
			}
		}
	}
	public boolean isAllSettled()
	{
		boolean settled = true;

		for ( int i=0; i <circle_pos.size(); i++)
		{
			if (circle_settle.get(i).equals(Boolean.FALSE)
				&& circle_removed.get(i).equals(Boolean.FALSE))
			{
				settled = false;
			}
		}
		return settled;
	}

	public int getColumn(int i)
	{
		return (int)((circle_pos.get(i).x-border_left) / circle_size);
	}

	public int getRow(int i)
	{
		return (int)((circle_pos.get(i).y-border.getY()) / circle_size);
	}

	public boolean isEnoughSpace()
	{
		boolean enough = false;
		if (getColumn(current_index[0]) == 0)
		{
			if((circle_pos.get(current_index[0]).y > column_height[1])
			   && (circle_pos.get(current_index[0]).y > column_height[2]))
			{
				enough = true;
			}
		}
		else if (getColumn(current_index[0]) == (column_number-1))
		{
			if ((circle_pos.get(current_index[0]).y > column_height[column_number-2])
				&& (circle_pos.get(current_index[0]).y > column_height[column_number-3]))
			{
				enough = true;
			}
		}
		else if ((circle_pos.get(current_index[0]).y < column_height[getColumn(current_index[0])-1])
				 && ((getColumn(current_index[0])+2) != column_number))
		{
			if((circle_pos.get(current_index[0]).y > column_height[getColumn(current_index[0])+1])
			   && (circle_pos.get(current_index[0]).y > column_height[getColumn(current_index[0])+2]))
			{
				enough = true;
			}
		}

		else if ((circle_pos.get(current_index[0]).y < column_height[getColumn(current_index[0])+1])
				 && ((getColumn(current_index[0])-1) != 0))
		{
			if ((circle_pos.get(current_index[0]).y > column_height[getColumn(current_index[0])-1])
				&& (circle_pos.get(current_index[0]).y > column_height[getColumn(current_index[0])-2]))
			{
				enough = true;
			}
		}
		else if ((circle_pos.get(current_index[0]).y > column_height[getColumn(current_index[0])-1])
				 && (circle_pos.get(current_index[0]).y > column_height[getColumn(current_index[0])+1]))
		{
			enough = true;
		}

		return enough;
	}

	public void turnCircles()
	{

		if (circle_pos.get(current_index[0]).y < (circle_pos.get(current_index[2]).y-circle_size))
		{
			if (isEnoughSpace())
			{
				float temp_x = circle_pos.get(current_index[0]).x;
				float temp_y = circle_pos.get(current_index[0]).y;
				circle_pos.set(current_index[0], new Vector2( temp_x+circle_size, temp_y+circle_size));

				temp_x = circle_pos.get(current_index[2]).x;
				temp_y = circle_pos.get(current_index[2]).y;
				circle_pos.set(current_index[2], new Vector2( temp_x-circle_size, temp_y-circle_size));

			}
			Color temp;
			temp = circle_color.get(current_index[0]);
			circle_color.set(current_index[0], circle_color.get(current_index[2]));
			circle_color.set(current_index[2], temp);
		}
		else
		{
			float temp_x = circle_pos.get(current_index[0]).x;
			float temp_y = circle_pos.get(current_index[0]).y;
			circle_pos.set(current_index[0], new Vector2( temp_x-circle_size, temp_y-circle_size));

			temp_x = circle_pos.get(current_index[2]).x;
			temp_y = circle_pos.get(current_index[2]).y;
			circle_pos.set(current_index[2], new Vector2( temp_x+circle_size, temp_y+circle_size));
		}
		//int most_bottom_index = 0;
		int most_left_index = 2;
		int most_right_index = 0;
		//int current_column = getColumn(current_index[most_bottom_index]);
		int left_column = getColumn(current_index[most_left_index]);
		int right_column = getColumn(current_index[most_right_index]);
		if ( (circle_pos.get(current_index[most_left_index]).x < border_left) )
		{
			for (int i=0; i<number_of_moving; i++)
			{
				circle_pos.get(current_index[i]).x += circle_size;
			}
		}
		else if ((circle_pos.get(current_index[most_left_index]).y < column_height[left_column ]))
		{
			for (int i=0; i<number_of_moving; i++)
			{
				circle_pos.get(current_index[i]).x += circle_size;
			}
		}
		if ((circle_pos.get(current_index[most_right_index]).x > border_right))
		{
			for (int i=0; i<number_of_moving; i++)
			{
				circle_pos.get(current_index[i]).x -= circle_size;
			}
		}
		else if (circle_pos.get(current_index[most_right_index]).y < column_height[right_column])
		{
			for (int i=0; i<number_of_moving; i++)
			{
				circle_pos.get(current_index[i]).x -= circle_size;
			}
		}
	}

	public void configureCamera() {
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public Color getRandomColor() {
		Color colorArray[] = { Color.RED, Color.BLUE, Color.GREEN};
		int random = new Random().nextInt(3);
		return colorArray[random];
	}

	public void initialize()
	{
		configureCamera();


		scr_height = Gdx.graphics.getHeight();
		scr_width = Gdx.graphics.getWidth();

		int border_bottom;
		if (scr_width > scr_height)
		{
			circle_size = (int) scr_height/20;
			border_bottom = (int) (scr_height - circle_size*row_number);
		}
		else
		{
			circle_size = (int) scr_height/25;
			border_bottom = (int) (scr_height - circle_size *row_number);
		}

		border_left = scr_width/2- (circle_size*column_number/2);
		border_right = border_left+circle_size*column_number;
		border = new Rectangle(border_left, border_bottom, border_right, scr_height);

		original_speed = (circle_size*row_number)/400;
		fast_speed = (Gdx.graphics.getHeight()-border_bottom)/30;
		medium_speed = (Gdx.graphics.getHeight()-border_bottom)/200;

		move_speed = original_speed;
		game_speed = original_speed;

		if (scr_height > scr_width)
		{
			//turn_button_pos = new Rectangle(border_left-circle_size*2-10, scr_height/2-circle_size*2, circle_size*2, circle_size*2);
			//special_button_pos = new Rectangle(border_left-circle_size*2-10, scr_height/2-circle_size*6, circle_size*2, circle_size*2);
			special_button_pos = new Rectangle(border_left-circle_size, border.y-circle_size*4, circle_size*2, circle_size*2);
			turn_button_pos = new Rectangle(border_left+circle_size*2, border.y-circle_size*4, circle_size*2, circle_size*2);
			left_button_pos = new Rectangle(border_right-circle_size*4, border.y-circle_size*4, circle_size*2, circle_size*2);
			right_button_pos = new Rectangle(border_right-circle_size, border.y-circle_size*4, circle_size*2, circle_size*2);
			
			score_position = new Vector2(scr_width/3, border_bottom-40);
			combo_position_original = new Rectangle(border_left+circle_size*2, border.y+circle_size*5, circle_size*6, circle_size*2);
			//score.setScale(5.0f, 5.0f);
			//score.scale(2f);

			background_position = new Rectangle(((scr_width/2)-((background.getRegionWidth()/2)*scr_height/background.getRegionHeight())), 0, background.getRegionWidth()*scr_height/background.getRegionHeight(), scr_height);
		}
		else
		{
			special_button_pos = new Rectangle(border_left-circle_size*10, scr_height/2-circle_size*6, circle_size*3, circle_size*3);
			turn_button_pos = new Rectangle(border_left-circle_size*6, scr_height/2 -circle_size*6, circle_size*3, circle_size*3);
			left_button_pos = new Rectangle(border_right+circle_size*4, scr_height/2-circle_size*6, circle_size*3, circle_size*3);
			right_button_pos = new Rectangle(border_right+circle_size*8, scr_height/2 -circle_size*6, circle_size*3, circle_size*3);
			
			score_position= new Vector2(border_left-circle_size*9, scr_height/2+circle_size*6);
			combo_position_original = new Rectangle(border_left+circle_size*2, border.y+circle_size*5, circle_size*9, circle_size*3);
			//score.setScale(5.0f, 5.0f);
			background_position = new Rectangle(0, ((scr_height/2)-((background.getRegionHeight()/2)*scr_width/background.getRegionWidth())), scr_width, background.getRegionHeight()*scr_width/background.getRegionWidth());
		}
		combo_position = new Rectangle();
		combo_position.set(combo_position_original);
		//score.
		//score.setColor(Color.PURPLE);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ethnocentric_rg.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int) (circle_size/1.3f);
		parameter.color = Color.BLACK;
		score = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); 
		

		score_number=0.0f;
		score_shown=0.0f;
		combo_number = 0.0f;
		remove_complete=true;
		
		combo_time = 2.5f;
		time = 0.0f;
	}
	public void resetGame()
	{

		charm_start = false;
		
		circle_pos.clear();
		circle_settle.clear();
		circle_color.clear();
		circle_removed.clear();
		circle_glow.clear();

		// add first three circles
		for (int i=0; i<number_of_moving; i++)
		{
			circle_pos.add(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()+i*circle_size));
			circle_color.add(new Color(getRandomColor()));
			circle_settle.add(new Boolean(Boolean.FALSE));
			circle_removed.add(new Boolean(Boolean.FALSE));
			circle_glow.add(new Boolean(Boolean.FALSE));
			current_index[i] = i;
		}
		// if 1st and 2nd are same color, make sure 3rd one is different
		if (circle_color.get(current_index[0]).equals(circle_color.get(current_index[1])))
		{
			while (circle_color.get(current_index[0]).equals(circle_color.get(current_index[2])))
			{
				circle_color.set(current_index[2], getRandomColor());
			}
		}

		for (int i = 0; i < column_number; i++)
		{
			column_height[i] = (int) border.getY() + circle_size/2;
		}

		for (int i=0; i<column_number; i++)
		{
			for (int j=0; j<row_number;j++)
			{
				overall_index[i][j] = 0xFF;
			}
		}

	}

	@Override
	public void resize(int p1, int p2)
	{
		// TODO: Implement this method
		
		
		
		initialize();
		resetGame();
	}

	@Override
	public void show()
	{
		// TODO: Implement this method
		Texture texture_red = game.asset_manager.get("bubble_red.png", Texture.class);
		Texture red_2 = game.asset_manager.get("bubble_red2.png", Texture.class);
		Texture  red_3 = game.asset_manager.get("bubble_red3.png", Texture.class);
		Texture  red_4 = game.asset_manager.get("bubble_red4.png", Texture.class);

		Texture  texture_blue= game.asset_manager.get("bubble_blue.png", Texture.class);
		Texture  blue_2 = game.asset_manager.get("bubble_blue2.png", Texture.class);
		Texture  blue_3 = game.asset_manager.get("bubble_blue3.png", Texture.class);
		Texture  blue_4 = game.asset_manager.get("bubble_blue4.png", Texture.class);

		Texture  texture_green = game.asset_manager.get("bubble_green.png", Texture.class);
		Texture  green_2 = game.asset_manager.get("bubble_green2.png", Texture.class);
		Texture  green_3 = game.asset_manager.get("bubble_green3.png", Texture.class);
		Texture  green_4 = game.asset_manager.get("bubble_green4.png", Texture.class);

		Texture  texture_red_glow = game.asset_manager.get("red_glow.png", Texture.class);
		Texture  texture_blue_glow = game.asset_manager.get("blue_glow.png", Texture.class);
		Texture  texture_green_glow = game.asset_manager.get("green_glow.png", Texture.class);
		
		Texture  texture_down= game.asset_manager.get("down_arrow.jpg", Texture.class);
		Texture  texture_turn = game.asset_manager.get("rotate_arrow.jpg", Texture.class);
		

		Texture  texture_left = game.asset_manager.get("left_arrow.jpg", Texture.class);
		Texture  texture_right = game.asset_manager.get("right_arrow.jpg", Texture.class);
		
		red_bubble = new TextureRegion(texture_red);
		red_frames = new TextureRegion[4];
		red_frames[0] = (new TextureRegion(texture_red));
		red_frames[1] = (new TextureRegion(red_2));
		red_frames[2] = (new TextureRegion(red_3));
		red_frames[3] = (new TextureRegion(red_4));
		red_ani = new Animation(0.1f,red_frames);
		red_ani.setPlayMode(Animation.PlayMode.NORMAL);

		blue_bubble = new TextureRegion(texture_blue);
		blue_frames = new TextureRegion[4];
		blue_frames[0] = (new TextureRegion(texture_blue));
		blue_frames[1] = (new TextureRegion(blue_2));
		blue_frames[2] = (new TextureRegion(blue_3));
		blue_frames[3] = (new TextureRegion(blue_4));
		blue_ani = new Animation(0.10f,blue_frames);
		blue_ani.setPlayMode(Animation.PlayMode.NORMAL);

		green_bubble = new TextureRegion(texture_green);
		green_frames = new TextureRegion[4];
		green_frames[0] = (new TextureRegion(texture_green));
		green_frames[1] = (new TextureRegion(green_2));
		green_frames[2] = (new TextureRegion(green_3));
		green_frames[3] = (new TextureRegion(green_4));
		green_ani = new Animation(0.1f,green_frames);
		green_ani.setPlayMode(Animation.PlayMode.NORMAL);
		
		/*red_bomb = new TextureRegion(texture_red_bomb);
		blue_glow = new TextureRegion[4];
		TextureRegion[][] blue_glow_split = TextureRegion.split(texture_blue_glow, texture_blue_glow.getWidth(), texture_blue_glow.getHeight()/4);
		blue_glow[0] = blue_glow_split[0][0];
		blue_glow[1] = blue_glow_split[1][0];
		blue_glow[2] = blue_glow_split[2][0];
		blue_glow[3] = blue_glow_split[3][0];
		blue_glow_ani = new Animation(0.25f, blue_glow);
		blue_glow_ani.setPlayMode(Animation.PlayMode.LOOP);
		
		green_bomb = new TextureRegion(texture_green_bomb);*/
		blue_glow = new TextureRegion(texture_blue_glow);
		red_glow = new TextureRegion(texture_red_glow);
		green_glow = new TextureRegion(texture_green_glow);

		down_arrow = new TextureRegion(texture_down);
		turn_arrow = new TextureRegion(texture_turn);

		left_button = new TextureRegion(texture_left);
		right_button = new TextureRegion(texture_right);
		
		Texture combo = game.asset_manager.get("combo_list4.png", Texture.class);
		/*Texture combo1 = game.asset_manager.get("combo1.png", Texture.class);
		Texture combo2 = game.asset_manager.get("combo2.png", Texture.class);
		Texture combo3 = game.asset_manager.get("combo3.png", Texture.class);*/
		//combo_image = new TextureRegion(combo);
		TextureRegion[][] combo_frames_tmp = TextureRegion.split(combo, combo.getWidth(), combo.getHeight()/5);
		
		combo_frames = new TextureRegion[5];

		combo_frames[0] = combo_frames_tmp[3][0];
		combo_frames[1] = combo_frames_tmp[2][0];
		combo_frames[2] = combo_frames_tmp[1][0];
		combo_frames[3] = combo_frames_tmp[0][0];
		combo_frames[4] = combo_frames_tmp[0][0];
		
		/*combo_frames[0] = new TextureRegion(combo1, circle_size*5/3, circle_size/3);
		combo_frames[1] = new TextureRegion(combo2, circle_size*5/2, circle_size/2);
		combo_frames[2] = new TextureRegion(combo3, circle_size*10/3, circle_size*2/3);
		combo_frames[3] = new TextureRegion(combo, circle_size*5, circle_size);*/
		
		/*combo_frames[1][0] = new TextureRegion(combo, (circle_size*5/3), (circle_size/3));
		combo_frames[1][1] = new TextureRegion(combo, (circle_size*5/2), (circle_size/2));
		combo_frames[1][2] = new TextureRegion(combo, (circle_size*10/3), (circle_size*2/3));
		combo_frames[1][3] = new TextureRegion(combo, (circle_size*5), (circle_size));*/
		//combo_ani = new Animation;
		combo_ani = new Animation(0.050f, combo_frames);
		//combo_ani[1] = new Animation(0.25f, combo_frames[1]);
		combo_ani.setPlayMode(Animation.PlayMode.NORMAL);
		
		//hit_bottom = Gdx.audio.newSound(Gdx.files.internal("bubblehit2.mp3"));
		//bubble_pop_sound = Gdx.audio.newSound(Gdx.files.internal("bubblepop3.wav"));
		
		hit_bottom = game.asset_manager.get("bubblehit2.mp3", Sound.class);
		bubble_pop_sound = game.asset_manager.get("bubblepop3.wav", Sound.class);
		charm = game.asset_manager.get("charm.mp3", Sound.class);
		
		//score = new BitmapFont(Gdx.files.internal("Pixel_LCD_7.ttf"));
		
		
		Texture texture_background = game.asset_manager.get("background4.jpg", Texture.class);

		background = new TextureRegion(texture_background);
		
		Texture texture_glow_hori = game.asset_manager.get("glow_horizental.png", Texture.class);
		Texture texture_glow_vert = game.asset_manager.get("glow_vertical.png", Texture.class);
		
		TextureRegion[] glow_hori_frames = new TextureRegion[4];
		TextureRegion[] glow_vert_frames = new TextureRegion[4];
		
		TextureRegion[][] glow_hori = TextureRegion.split(texture_glow_hori, texture_glow_hori.getWidth(), texture_glow_hori.getHeight()/5);
		glow_hori_frames[0] = glow_hori[0][0];
		glow_hori_frames[1] = glow_hori[1][0];
		glow_hori_frames[2] = glow_hori[2][0];
		glow_hori_frames[3] = glow_hori[3][0];
		
		TextureRegion[][] glow_vert = TextureRegion.split(texture_glow_vert, texture_glow_vert.getWidth()/5, texture_glow_vert.getHeight());
		glow_vert_frames[0] = glow_vert[0][0];
		glow_vert_frames[1] = glow_vert[0][1];
		glow_vert_frames[2] = glow_vert[0][2];
		glow_vert_frames[3] = glow_vert[0][3];
		
		glow_horizon_ani = new Animation(0.1f, glow_hori_frames);
		glow_horizon_ani.setPlayMode(Animation.PlayMode.NORMAL);
		
		glow_vertical_ani = new Animation(0.1f, glow_vert_frames);
		glow_vertical_ani.setPlayMode(Animation.PlayMode.NORMAL);
		
		initialize();
		resetGame();
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
		shape.dispose();
		batch.dispose();
		hit_bottom.dispose();
		score.dispose();
		bubble_pop_sound.dispose();
		charm.dispose();
	}
	
}
