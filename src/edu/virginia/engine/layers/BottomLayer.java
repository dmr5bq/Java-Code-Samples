package edu.virginia.engine.layers;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.game.Enemy;
import edu.virginia.engine.game.EnemyDeathEvent;
import edu.virginia.engine.game.Energy;
import edu.virginia.engine.game.FireEvent;
import edu.virginia.engine.game.Parameters;
import edu.virginia.engine.game.Prototype;

public class BottomLayer extends DisplayObject implements IEventListener, Layer {

	// local statics:
	public static int len = TopLayer.len;
	public static int player_x = TopLayer.player_x;
	public static int player_y = TopLayer.player_y;
	public static double hit_tolerance = Parameters.ENEMY_HIT_TOLERANCE;
	public static int reverse_shift = len / 2;
	public static int normal_shift = 0;
	

	// fields:
	public ArrayList<Enemy> enemies; 	// stores all on screen Enemy instances
	public Energy active_energy;		// points to an energy object that will be displayed

	// tree pointer field:
	public Prototype parent;

	public BottomLayer( Prototype parent ) {
		super ( "bottom" ); // call to superclass DisplayObject

		// initialize fields
		this.enemies = new ArrayList<Enemy>( );	// creates empty list of Enemy objects

		// initialize tree pointer
		this.parent = parent;	// set parent pointer to instance of Prototype that created the bottom layer
	}

	@Override
	public void handleEvent( Event event ) {

		// Firing events handling, shots fired!
		if ( event instanceof FireEvent ) {
			// pull pointer to top by casting event source
			TopLayer top = (TopLayer) event.getSource();

			double base = top.getBase_angle( );
			double spread =  top.getCurrentEnergyAngle( );

			// define angles for collision detection
			double beam_lower_angle = base - spread;
			double beam_upper_angle = base + spread;


			// Generate ArrayList copy to avoid co-modificiation by event handler
			ArrayList<Enemy> copy = new ArrayList<Enemy> ( this.enemies );

			// check location of each on-screen enemy
			for ( Enemy e : copy ) {
				
				double min_angle = e.getAngleAboveCharacter( ) + Parameters.HIT_ANGLE_SHIFT;
				double max_angle = min_angle + hit_tolerance + Parameters.HIT_ANGLE_SHIFT; 

				boolean min_pass = min_angle >= beam_lower_angle;
				boolean max_pass = max_angle <= beam_upper_angle;

				if ( max_pass || min_pass ) 
						e.damage( );
			}
		}

		// Death events management, RIP
		if ( event instanceof EnemyDeathEvent ) {
			// Get a casted alias of the source of the event (the dead enemy)
			Enemy e = ( Enemy ) ( event.getSource( ) );		// assuming the source will always be an Enemy object
			this.enemies.remove(e);		// remove the dead enemy from the list of enemies to be monitored

			if (e.getDiameter() <= 0)
				// Register the kill in the DifficultyManager 
				this.parent.difficultyManager.registerKill( ); // updates counts and difficulty mechanisms
		}
	}

	public void draw( Graphics g ) {
		// call Enemy class draw method on all on-screen enemies
		for ( Enemy e : this.enemies )
			e.draw( g );
		if (this.active_energy != null)
			this.active_energy.draw(g);
	}

	public void update( ArrayList<String> pressedKeys ) {
		
		ArrayList<Enemy> copy = new ArrayList<Enemy> ( this.enemies );
		String right_key = KeyEvent.getKeyText ( KeyEvent.VK_RIGHT );
		String left_key = KeyEvent.getKeyText ( KeyEvent.VK_LEFT );

		for ( Enemy enemy: copy ) {
			enemy.update( pressedKeys );
		}

		if ( this.active_energy != null )
			this.active_energy.update( pressedKeys );

		

		if ( pressedKeys.contains(right_key) ) {
			
			int x_cur = this.position.x;

			int x_next = x_cur - Parameters.RUN_STEP;

			this.position.x = x_next;
		}
		
		if ( pressedKeys.contains(left_key) ) {
			
			int x_cur = this.position.x;

			int x_next = x_cur + Parameters.RUN_STEP;

			this.position.x = x_next;
		}
	}


}







