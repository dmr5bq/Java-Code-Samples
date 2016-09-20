package edu.virginia.engine.managers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.game.EndGameEvent;
import edu.virginia.engine.game.Parameters;
import edu.virginia.engine.game.Prototype;
import edu.virginia.engine.layers.BottomLayer;
import edu.virginia.engine.layers.MidLayer;
import edu.virginia.engine.layers.TopLayer;
import edu.virginia.engine.tween.TweenJuggler;

public class EndManager extends DisplayObject implements IEventListener {

	public Prototype parent;
	public boolean drawn;
	public boolean updated;
	
	public EndManager( Prototype parent ) {
		super("end_manager");
		this.parent = parent;
		this.drawn = false;
		this.updated = false;
		
		this.setScaleX(3);
		this.setScaleY(3);
	}
	
	@Override
	public void handleEvent ( Event event ) {
		if ( event instanceof EndGameEvent ) {
			this.drawn = true;
			this.updated = true;
		}
	}
	
	public void reset() {
		this.parent.topLayer = new TopLayer(this.parent);
		this.parent.midLayer = new MidLayer(this.parent);
		this.parent.bottomLayer = new BottomLayer(this.parent);
		this.parent.difficultyManager = new DifficultyManager(this.parent.bottomLayer, this.parent);
		this.parent.listenerManager = new ListenerManager(this.parent);
		this.parent.tweenManager = new TweenJuggler();
		this.parent.flipManager = new FlipManager( this.parent );
		
		this.parent.listenerManager.initialize();
		
		
		this.parent.start_time = System.currentTimeMillis();
		
		this.drawn = false;
		this.updated = false;
	}
	
	public void draw(Graphics g) {
		
		Graphics g_copy = g.create( );
		Graphics2D g2d = ( Graphics2D ) g_copy;
		
		g2d.setColor ( Color.BLACK );
		g2d.fillRect(0, 0, Parameters.DIMENSION, Parameters.DIMENSION);
		
		applyTransformations ( g2d );
		
		g2d.setColor ( Color.RED );
		g2d.drawString ( "Final Score: \t " + this.parent.difficultyManager.getKillCount( ),(int) ((Parameters.DIMENSION / 2)/ this.scaleX) - 50, (int) ((Parameters.DIMENSION / 2) /this.scaleX));
		
		g2d.setColor ( Color.WHITE);
		g2d.drawString ( "To try again, press the \"S\" key." ,(int) ((Parameters.DIMENSION / 2)/ this.scaleX) - 100	, (int) ((Parameters.DIMENSION / 2) /this.scaleX + 35));
		
		g2d.drawString ( "To quit, press the \"A\" key." ,(int) ((Parameters.DIMENSION / 2)/ this.scaleX) - 100, (int) ((Parameters.DIMENSION / 2) /this.scaleX + 70));
		
		reverseTransformations ( g2d );
		
	}
	
	public void update ( ArrayList<String> pressedKeys ) {
		
		String s_key = KeyEvent.getKeyText( KeyEvent.VK_S );
		String a_key = KeyEvent.getKeyText( KeyEvent.VK_A );
		
		if ( pressedKeys.contains ( s_key ) ) 
			reset( );
		if ( pressedKeys.contains ( a_key ) ) {
			this.parent.stop( );
			this.parent.closeGame(); 
		}
	}
	
	
	

}
