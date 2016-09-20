package edu.virginia.engine.game;

import java.awt.Point;
import java.awt.Rectangle;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.layers.TopLayer;

public class Player extends AnimatedSprite implements IEventListener {
	
	
	// class statics:
	public static int len = Parameters.DIMENSION;
	public static int player_x = TopLayer.player_x;
	public static int player_y = TopLayer.player_y;
	public static int player_x_reversed = len - player_x;
	
	private Rectangle hitBox;
	public int x_position;

	public Player ( String id ) {
		super ( id );
		this.x_position = player_x;
		this.hitBox = new Rectangle( this.x_position , player_y - Parameters.HIT_BOX_HEIGHT , Parameters.HIT_BOX_WIDTH , Parameters.HIT_BOX_HEIGHT );
	}
	
	public Player ( String id, String filename ) {
		super ( id, filename );
		this.x_position = player_x;
		this.hitBox = new Rectangle( this.x_position , player_y - Parameters.HIT_BOX_HEIGHT , Parameters.HIT_BOX_WIDTH , Parameters.HIT_BOX_HEIGHT );
	}

	public Rectangle getHitBox( ) {
		return this.hitBox;
	}
	
	public boolean checkHit ( Point p ) { // in Prototype, p is the position of some enemy
		
		Rectangle rect_ref = this.hitBox; // runtime opt
		
		// define bounds of hit-box for comparisons
		int this_x_min = rect_ref.x;					// for readability
		int this_x_max = rect_ref.x + rect_ref.width;	// for readability
		
		
		int this_y_min = rect_ref.y - rect_ref.height - Parameters.HIT_TOLERANCE;	// for readability
		int this_y_max = rect_ref.y + Parameters.HIT_TOLERANCE ;					// for readability
		
		
		boolean in_x = (this_x_min <= p.x) && (this_x_max >= p.x);
		boolean in_y = (this_y_min <= p.y) && (this_y_max >= p.y);
		
		return in_y && in_x;
	}
	
	@Override
	public void handleEvent( Event event ) {
		// TODO Auto-generated method stub	
	}
	
	

}
