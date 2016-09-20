package edu.virginia.engine.game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.SoundManager;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.layers.BottomLayer;
import edu.virginia.engine.layers.MidLayer;
import edu.virginia.engine.layers.TopLayer;
import edu.virginia.engine.managers.DifficultyManager;
import edu.virginia.engine.managers.EndManager;
import edu.virginia.engine.managers.FlipManager;
import edu.virginia.engine.managers.ListenerManager;
import edu.virginia.engine.managers.SoundEffectsManager;
import edu.virginia.engine.tween.TweenJuggler;

/*
 * Display/Pointer Components Tree:
 * 
 * Prototype
 * 		^
 * 		|	
 * 	   /|\
 * 	  / | \
 * 	 /  |  \
 *	/	|	\
 *	 TopLayer \
 * 	|	|	  |
 * 	|	 MidLayer
 * 	|	|	  |
 * 	|	|	   BottomLayer
 *	^   ^     ^
 *	|	|	  |
 *	 Blocking Rectangles
 *	 Shifting Rectangles
 *	 Player's Sprite
 *	 KillCountDisplay
 *	 EnergyBar|
 *	 DifficultyManager
 *	|	|	 |
 *	|	 Color|
 *	|	 Background
 *	|		 |
 *	|		  ArrayList<Enemies>
 *			  Active Energy		
 *	|	
 *	 Current Time
 *	 KillCount
 *			 
 *		------------------------------------------	 
 *		| -------- |                             |
 *		| -------- |                             |
 *		| -------- |         Generation Zone     |
 *		| -------- |                             | 
 *		| -------- |                             |
 *		| -------- |                             |
 *		| -------- |                     Energy Generation
 *		| ------Player---------------------------|
 *		------------------------------------------
 *		------------------------------------------
 *		------------------------------------------
 *		------------------------------------------
 *		------------------------------------------
 *		------------------------------------------
 *		------------------------------------------
 *
 *
 *	 
 */

public class Prototype extends Game {


	// drawn fields
	public TopLayer topLayer;
	public MidLayer midLayer;
	public BottomLayer bottomLayer;

	// manager fields
	public DifficultyManager difficultyManager;
	public TweenJuggler tweenManager;
	public ListenerManager listenerManager;
	public EndManager endManager;
	public FlipManager flipManager;
	public SoundManager soundManager;

	public double start_time;
	

	public Prototype () {
		super ( "prototype" , Parameters.DIMENSION , Parameters.DIMENSION ); 

		// initialize drawn fields
		this.topLayer = new TopLayer ( this ); // top layer --> prototype 
		this.midLayer = new MidLayer ( this ); // mid layer --> prototype
		this.bottomLayer = new BottomLayer ( this ); // bottom layer --> prototype

		// set up difficulty manager
		this.difficultyManager = new DifficultyManager( this.bottomLayer , this ); // difficultyManager --> bottomLayer --> Prototype

		// register the bottom layer as a listener to firing by the top layer
		this.topLayer.addEventListener (  this.bottomLayer, FireEvent.FIRE_EVENT );

		//set up tween manager
		this.tweenManager = new TweenJuggler( );

		//set up end manager
		this.endManager = new EndManager ( this );
		
		this.listenerManager = new ListenerManager ( this );
		
		this.flipManager = new FlipManager ( this );
		
		this.soundManager = new SoundManager();
		this.soundManager.loadSound("music", "a_new_beginning.wav");

		
		

		this.start_time = System.currentTimeMillis( );
	}

	public void update( ArrayList pressedKeys ) {
		boolean tl_init = this.topLayer != null;
		boolean ml_init = this.midLayer != null;
		boolean bl_init = this.bottomLayer != null;
		boolean dm_init = this.difficultyManager != null;
		boolean tm_init = this.tweenManager != null;
		boolean em_init = this.endManager != null;
		boolean fm_init = this.flipManager != null;
		boolean full_init = tl_init && ml_init && bl_init && dm_init && tm_init && em_init && fm_init;

		//children/field updates
		if ( tl_init ) 
			this.topLayer.update(pressedKeys);

		if ( ml_init  ) 
			this.midLayer.update(pressedKeys);

		if ( bl_init  ) 
			this.bottomLayer.update(pressedKeys);

		if ( dm_init  )
			this.difficultyManager.update();

		if ( tm_init  )
			this.tweenManager.update();

		if ( em_init )
			if (this.endManager.updated) {
				this.endManager.update(pressedKeys);	
			}
		if ( fm_init )
			this.flipManager.update(pressedKeys);
		
		if ( full_init )
			if ( ! this.listenerManager.initialized ) {
				this.listenerManager.initialize();
			}
		
	}


	public void draw ( Graphics g ) {

		//draw the layers from bottom up

		if ( this.bottomLayer != null ) 
			this.bottomLayer.draw(g);

		if ( this.midLayer != null ) 
			this.midLayer.draw(g);

		if ( this.topLayer != null ) 
			this.topLayer.draw(g);

		if( this.endManager != null && this.endManager.drawn)
			this.endManager.draw(g);
		
		
	}

	public static void main ( String[] args ) {
		// create an instance of the game and begin the game loop
		Prototype p = new Prototype();
		
		p.start();
		p.soundManager.playMusic("music");
		

	}




}
