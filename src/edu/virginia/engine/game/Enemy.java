package edu.virginia.engine.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import edu.virginia.engine.display.SoundManager;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.layers.BottomLayer;
import edu.virginia.engine.layers.Layer;
import edu.virginia.engine.layers.TopLayer;
import edu.virginia.engine.managers.DifficultyManager;
import edu.virginia.engine.managers.ListenerManager;
import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenCompleteEvent;
import edu.virginia.engine.tween.TweenableParameter;

public class Enemy extends Sprite {


	// local statics:
	public static int len = TopLayer.len; // for readability
	public static int player_x = TopLayer.player_x; // for clarity
	public static int player_y = TopLayer.player_y; // for clarity
	public static int max_y = player_y - ( Parameters.MAX_DIAMETER + Parameters.ENEMY_HOVER_DISTANCE ); 
	public static int max_x = player_x - Parameters.ENEMY_HOVER_DISTANCE;
	public static int max_x_reverse = len - player_x;


	// fields
	private int health;		// tracks Enemy's health
	private int diameter;	// tracks Enemy's drawing diameter
	private Color color;
	public int x_target;
	
	// controller fields
	private boolean controllable;

	//computation-used fields
	private boolean grown;	// determines whether or not Enemy has grown to full size


	// tree pointer
	private BottomLayer parent; // points to the BottomLayer object that instantiated it
	public DifficultyManager dm_ptr; // points to the top layer attribute of the active Prototype
	public SoundManager soundManager;


	// local variable for growth-rate
	double lastGrowth = 0;	// aids in growth speed limitation mechanism


	public Enemy ( String id, Layer parent, DifficultyManager running_dm ) {
		super(id);
		
		// initialize fields
		this.diameter = Parameters.MIN_DIAMETER;
		this.dm_ptr = running_dm;
		this.controllable = true;

		// tree pointer initialized to parameter input
		this.parent = (BottomLayer) parent;
		
		// set transparency lower for the drawing of the Enemy
		this.alpha = Parameters.ENEMY_ALPHA ;
		
		Prototype prototype = this.parent.parent;
		ListenerManager lm_ptr = prototype.listenerManager;
		
		lm_ptr.registerNewEnemy(this);
		
		if ( this.parent.parent.flipManager.orientation )
			this.x_target = max_x;
		else
			this.x_target = max_x_reverse;
		

		this.soundManager = new SoundManager();
		
		this.soundManager.loadSound("enemy", "i_see_you_voice.wav");
		//this.soundManager.playSoundEffect("enemy");
	
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Point getPointRelativeToCharacter( ) {
		int rad = this.diameter / 2; 	// save on extraneous computation, opt
		Point pos = this.getPosition(); // reduce on call overhead, opt

		return new Point( pos.x - player_x + rad, - pos.y + player_y + rad );
	}

	public double getAngleAboveCharacter( ) {
		Point p = this.getPointRelativeToCharacter();
		return - Math.atan( ( double ) p.y / ( double ) p.x);
	}

	public void damage( ) {
		// decrease the diameter by the step
		this.diameter -= Parameters.DAMAGE_STEP;
		if (this.diameter <= 0)
			this.dispatchEvent(new EnemyDeathEvent(this));
		
		// set grown back to false so that it will grow back to full size when damage ceases
		this.grown = false;

		

	}

	public void draw( Graphics g ) {

		// create a cloned instance of the graphics object and draw the enemy
		Graphics g2 = g.create( );
		Graphics2D g2d = ( Graphics2D ) g2;

		applyTransformations(g2d);

		g2d.setColor( this.color );	// set pen color to black
		g2d.fillOval( 0 , 0 , this.diameter , this.diameter ); // draw enemy of diameter equal to its diameter field

		reverseTransformations(g2d);

		// delete instance
		g2.dispose();
	}

	public void update ( ArrayList<String> pressedKeys ) { 
		
		String right_key = KeyEvent.getKeyText ( KeyEvent.VK_RIGHT );
		DifficultyManager dm_ptr = this.parent.parent.difficultyManager;
		BottomLayer bl_ptr = this.parent;
		double cur_time = dm_ptr.getCurrentTime();
		boolean timeToGrow = ( lastGrowth < ( cur_time + Parameters.GROW_TIME ) );

		if ( this.controllable ) {
			// set the grown boolean to true if the maximum size has been attained
			if ( this.diameter >= Parameters.MAX_DIAMETER ) 
				this.grown = true;

			// grow if conditions met
			if ( timeToGrow && !this.grown ) 
				
				this.grow( );
			
			

			// move toward the player if fully grown
			if ( this.grown ) 
				this.move( ); 
			
		} else {
			this.dispatchEvent ( new EnemyDeathEvent ( this ) );
		}
		

		if ( pressedKeys.contains ( right_key ) ) {
			// shift with background when right key is pressed
			int x_cur = this.position.x;

			int x_next = x_cur - Parameters.RUN_STEP;

			this.position.x = x_next;
		}
		
		if ( ! this.inScreenRange( ) )
				bl_ptr.enemies.remove ( this );
			

	}

	public void grow( ) {
		
		double cur_time = this.parent.parent.difficultyManager.getCurrentTime( );
		
		// double-check that the Enemy is not fully grown
		if ( ! this.grown ) {
			// set the last growth time to the current time of the system
			lastGrowth = cur_time;

			// increase the diameter by the step
			this.diameter += Parameters.GROW_STEP;
		}
	}



	@Override
	public String toString( ) {
		// for debugging
		return "Enemy: " + this.getPosition( );
	}

	public void move() {

		// set current position values
		int cur_x = this.getPosition().x;
		int cur_y = this.getPosition().y;

		// set step to current value;
		int step = Parameters.MOVE_STEP;


		if (cur_x > this.x_target) 
			cur_x -= step;
		if (cur_x < this.x_target) 
			cur_x += step;
		if (cur_y < max_y ) 
			cur_y += step;
		

		this.position.x = cur_x;
		this.position.y = cur_y;
	}

	public boolean inScreenRange() {
		// boundary locals (for clarity)
		int frame_x_min = -len;
		int frame_x_max = 2 * len;
		int frame_y_min = 0;
		int frame_y_max = player_y;

		// locals to track position of this (Enemy)
		int this_x = this.getPosition().x;
		int this_y = this.getPosition().y;

		boolean in_x = (frame_x_min < this_x && frame_x_max > this_x);
		boolean in_y = (frame_y_min < this_y & frame_y_max > this_y);

		return in_x && in_y;
	}

	public void handleCollision() {

		Point cur = this.getPosition();
		Tween enemy_tween_out = new Tween ( this );
		Random point_gen = new Random();

		this.controllable = false;

		int d_x = Parameters.TWEEN_LOCATION_BASE + point_gen.nextInt(Parameters.TWEEN_LOCATION_RANGE) + player_x;
		int d_y = - Parameters.TWEEN_LOCATION_BASE - point_gen.nextInt(Parameters.TWEEN_LOCATION_RANGE) + player_y;

		this.dispatchEvent(new PlayerDamageEvent ( this ) ); // dispatches an event that indicates that health should go down

		enemy_tween_out.animate(TweenableParameter.ALPHA, Parameters.ENEMY_ALPHA, 0, Parameters.FADE_OUT_TIME);
		enemy_tween_out.animate(TweenableParameter.X, cur.x, d_x,  Parameters.FADE_OUT_TIME);
		enemy_tween_out.animate(TweenableParameter.Y, cur.y, d_y,  Parameters.FADE_OUT_TIME);
		enemy_tween_out.animate(TweenableParameter.SCALE_X, 1.0,  Parameters.TWEEN_FINAL_SCALE,  Parameters.FADE_OUT_TIME);
		enemy_tween_out.animate(TweenableParameter.SCALE_Y, 1.0,  Parameters.TWEEN_FINAL_SCALE,  Parameters.FADE_OUT_TIME);

		this.parent.parent.tweenManager.add(enemy_tween_out);

		// visual effect
		this.setColor( Parameters.HIT_COLOR);

	}

	public void handleDeath( ) {
		Point cur = this.getPosition();
		Tween enemy_tween_out = new Tween ( this );
		Random point_gen = new Random();

		this.controllable = false;

		int d_x =  Parameters.TWEEN_LOCATION_BASE + point_gen.nextInt( Parameters.TWEEN_LOCATION_RANGE) + player_x;
		int d_y = -  Parameters.TWEEN_LOCATION_BASE - point_gen.nextInt( Parameters.TWEEN_LOCATION_RANGE) + player_y;

		this.dispatchEvent(new PlayerDamageEvent ( this ) ); // dispatches an event that indicates that health should go down

		enemy_tween_out.animate(TweenableParameter.ALPHA,  Parameters.ENEMY_ALPHA, 0,  Parameters.FADE_OUT_TIME);
		enemy_tween_out.animate(TweenableParameter.X, cur.x, d_x,  Parameters.FADE_OUT_TIME);
		enemy_tween_out.animate(TweenableParameter.Y, cur.y, d_y,  Parameters.FADE_OUT_TIME);
		enemy_tween_out.animate(TweenableParameter.SCALE_X, 1.0,  Parameters.TWEEN_FINAL_SCALE,  Parameters.FADE_OUT_TIME);
		enemy_tween_out.animate(TweenableParameter.SCALE_Y, 1.0,  Parameters.TWEEN_FINAL_SCALE,  Parameters.FADE_OUT_TIME);

		this.parent.parent.tweenManager.add(enemy_tween_out);

		// visual effect
		this.setColor(Parameters.HIT_COLOR);
	}

	public int getDiameter() {
		return this.diameter;
	}






}
