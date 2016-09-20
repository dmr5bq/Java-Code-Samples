package edu.virginia.engine.managers;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.game.Enemy;
import edu.virginia.engine.game.Parameters;
import edu.virginia.engine.game.Prototype;
import edu.virginia.engine.layers.*;

public class FlipManager {

	public static int len = Parameters.DIMENSION;
	public static Point origin_normal = new Point(0,0);
	public static Point origin_reverse = new Point(len, 0);

	public TopLayer topLayer;
	public BottomLayer bottomLayer;
	public Prototype prototype;

	public boolean orientation;


	public FlipManager( Prototype prototype ) {
		this.prototype = prototype;
		this.topLayer = this.prototype.topLayer;
		this.bottomLayer = this.prototype.bottomLayer;	
		this.orientation = true;
	}

	public void flipNormal( ) {

		int shift_x = - BottomLayer.reverse_shift;
		BottomLayer bl_ptr = this.prototype.bottomLayer;
		Point bl_position = bl_ptr.getPosition();

		bl_position.x += BottomLayer.reverse_shift;

		for ( Enemy enemy : this.prototype.bottomLayer.enemies )
			enemy.getPosition().x += shift_x;

		if (bl_ptr.active_energy != null) 
			bl_ptr.getPosition().x += shift_x;



		this.topLayer.setScaleX ( 1 );
		this.topLayer.setPivotPoint( origin_normal );
		this.topLayer.player.x_position = TopLayer.player_x;
		for ( Enemy enemy : this.bottomLayer.enemies )
			enemy.x_target = Enemy.max_x;

	}

	public void flipReverse( ) {

		int shift_x = BottomLayer.reverse_shift;
		BottomLayer bl_ptr = this.prototype.bottomLayer;
		Point bl_position = bl_ptr.getPosition();

		bl_position.x += BottomLayer.reverse_shift;

		for ( Enemy enemy : this.prototype.bottomLayer.enemies )
			enemy.getPosition().x += shift_x;

		if (bl_ptr.active_energy != null) 
			bl_ptr.getPosition().x += shift_x;


		this.topLayer.setScaleX ( - 1 );
		this.topLayer.setPivotPoint( origin_reverse );
		this.topLayer.player.x_position = TopLayer.player_x_reversed;
		for ( Enemy enemy : this.bottomLayer.enemies )
			enemy.x_target = Enemy.max_x_reverse;
	}

	public void update ( ArrayList<String> pressedKeys ) {

		String right_key = KeyEvent.getKeyText ( KeyEvent.VK_RIGHT );
		String left_key = KeyEvent.getKeyText ( KeyEvent.VK_LEFT );

		if ( pressedKeys.contains(right_key) ) {
			if (! this.orientation) {
				this.orientation = true;
				flipNormal( );
			}
		}	else if ( pressedKeys.contains ( left_key )  ) {	
			if (this.orientation) {
				this.orientation = false;
				flipReverse( );
			}
		}





	}





}
