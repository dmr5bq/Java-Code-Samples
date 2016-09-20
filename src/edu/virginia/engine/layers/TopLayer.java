package edu.virginia.engine.layers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.game.EndGameEvent;
import edu.virginia.engine.game.EnergyBar;
import edu.virginia.engine.game.FireEvent;
import edu.virginia.engine.game.HealthBar;
import edu.virginia.engine.game.KillCountDisplay;
import edu.virginia.engine.game.Parameters;
import edu.virginia.engine.game.Player;
import edu.virginia.engine.game.Prototype;
import edu.virginia.engine.game.Rect;


public class TopLayer extends DisplayObject implements Layer {

	// class and library locals:
	public static int len  = Parameters.DIMENSION; // defined for readability
	public static int player_x = len / 4;
	public static int player_y = len / 2;
	public static int player_x_reversed = len - player_x;
	public static int player_y_reversed = len - player_y;

	public double lastHealth;
	
	// drawn fields:
	//		dynamics:
	public Player player; // may be other subclass
	private Rect top;
	private Rect bottom;
	private EnergyBar energyBar;
	private KillCountDisplay killCount;
	private HealthBar healthBar;
	//		statics:
	private Rectangle back_cover;
	private Rectangle floor_cover;

	// other fields
	private double base_angle;
	private double currentEnergyAngle;
	private double energyLevel;
	private double health;
	public Rectangle generationZone;
	


	//tree pointers
	public Prototype parent;

	// Constructor, takes pointer to Prototype
	public TopLayer(Prototype parent) {
		super("top");
		
		lastHealth = Parameters.MAX_HEALTH;

		// initialize drawn fields
		// 		dynamics initializations:
		//			class-controlled structures
		this.player = new Player("player");
		this.top = new Rect(0 , 0 , len  *  5 ,  len );
		this.bottom = new Rect(0,0, len  * 5,  len );
		//			linked independent structures
		this.energyBar = new EnergyBar ( this );
		this.killCount = new KillCountDisplay ( this );
		this.healthBar = new HealthBar ( this );
		//		statics construction
		this.back_cover = new Rectangle ( 0 , 0 ,  player_x,  len );			// covers behind the player with black
		this.floor_cover = new Rectangle(0, len - player_y,  len ,  player_y);	// covers under the player with black

		// initialize other fields
		//		note: negative angles are above the + x-axis
		this.base_angle = - Parameters.HIGH_ENERGY_ANGLE; 	// makes "flashlight" point in the direction of and parallel to the + x-axis
		this.energyLevel = Parameters.MAX_ENERGY; 		   	// initialize the player energy to the max value
		this.currentEnergyAngle = Parameters.HIGH_ENERGY_ANGLE;	// set the beginning angle (separation from base_angle axis) to the maximum value
		this.health = Parameters.MAX_HEALTH;
		this.generationZone = new Rectangle(-len, 0, 3 * len , player_y - this.player.getHitBox().height * 2); 
		
		// initialize tree pointer 
		this.parent = parent; 	// pointer refers to the instance of the Prototype class that created this TopLayer

		// move rectangles into correct position and pivot by character point
		this.top.setPosition(new Point( player_x ,  player_y )); // shift to player's location
		this.bottom.setPosition(new Point( player_x , player_y )); // shift to player's location

		this.top.setPivotPoint(new Point( player_x,  len )); // set along bottom edge, shift whole shape up by -len 
		this.bottom.setPivotPoint(new Point( player_x, 0)); // set along top edge

		// move killCountDisplay to bottom left
		this.killCount.setPosition(new Point (  player_x / 4 , 5 *  player_x / 2 ) ); // move to position to bottom left of character

		//////////////////////////////////////////////////////////////////////////////////////////
		// *NOTE*: this  block can be replaced once a font-size manipulation technique is found //
		this.killCount.setScaleX( 3 );////////////////////////////////////////////////////////////
		this.killCount.setScaleY( 3 );////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////


		// move the energy bar to the top right, but off of the margins, in clear view
		this.energyBar.setPosition(new Point( len / 16, len / 16));
		this.healthBar.setPosition(new Point( len / 16, len / 8 ));
		
	}


	public double getHealth() {
		return health;
	}


	public void setHealth(double health) {
		this.health = health;
	}

	

	public void update(ArrayList pressedKeys) {
		
		if ( this.health <= 0 || this.energyLevel <= 0) {
			this.dispatchEvent ( new EndGameEvent ( this ) );
		}
		
		// call update on the dynamic drawable fields
		//this.player.update(pressedKeys);
		this.top.update(pressedKeys);
		this.bottom.update(pressedKeys);

		// rotation handling
		String up_key = KeyEvent.getKeyText(KeyEvent.VK_UP); 		// tracks UP arrow key-strokes
		String down_key = KeyEvent.getKeyText(KeyEvent.VK_DOWN);	// tracks DOWN arrow key-strokes
		String space_bar = KeyEvent.getKeyText(KeyEvent.VK_SPACE); 	// tracks SPACEBAR key-strokes


		if (pressedKeys.contains(up_key) && this.base_angle >= Parameters.ANGLE_LOWER_BOUND)
			this.base_angle -= Parameters.ANGLE_STEP;
		if (pressedKeys.contains(down_key) && this.base_angle <= Parameters.ANGLE_UPPER_BOUND)
			this.base_angle += Parameters.ANGLE_STEP;

		// position blocks to the current energy angle
		this.top.setRotation(this.base_angle - this.currentEnergyAngle); 	// sets beam angle above, an angle equal to the current energy angle off the base angle
		this.bottom.setRotation(this.base_angle + this.currentEnergyAngle);	// sets beam angle below, an angle equal to the current energy angle off the base angle

		// change the energy level if the SPACEBAR has been pressed
		if ( pressedKeys.contains( space_bar ) ) {
			this.dispatchEvent( new FireEvent ( this ) ); 
			this.energyLevel -= Parameters.FIRE_ENERGY_STEP;
		} else {
			this.energyLevel -= Parameters.NORMAL_ENERGY_STEP;
		}

		// change the angle on the current energy angle
		if ( this.energyLevel > Parameters.MAX_ENERGY / 2 )
			this.currentEnergyAngle = Parameters.HIGH_ENERGY_ANGLE;
		else if ( this.energyLevel > Parameters.MAX_ENERGY / 4 )
			this.currentEnergyAngle = Parameters.MID_ENERGY_ANGLE;
		else if (this.energyLevel > 0) 
			this.currentEnergyAngle = Parameters.LOW_ENERGY_ANGLE;
		else this.currentEnergyAngle = 0;
		
		lastHealth = health;
	}


	public double getCurrentEnergyAngle() {
		return currentEnergyAngle;
	}

	public void setCurrentEnergyAngle(double currentEnergy) {
		this.currentEnergyAngle = currentEnergy;
	}

	public void draw(Graphics g) {
		Graphics2D graph = ((Graphics2D) g);
		
		applyTransformations(graph);
		
		this.fillBackgroundRectangles(g);
		this.top.draw(g);
		this.bottom.draw(g);
	
		reverseTransformations(graph);
		
		this.energyBar.draw(g);
		this.killCount.draw(g); 
		this.healthBar.draw(g);
	}
	


	public void fillBackgroundRectangles(Graphics g) {
		g.setColor(Color.BLACK);

		// fill behind player 
		g.fillRect(back_cover.x, back_cover.y, back_cover.width, back_cover.height);

		// fill under player
		g.fillRect(floor_cover.x, floor_cover.y, floor_cover.width, floor_cover.height);
	}

	public double getEnergyLevel() {
		return energyLevel;
	}

	public void setEnergyLevel( double energyLevel ) {
		this.energyLevel = energyLevel;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Rect getTop() {
		return top;
	}

	public Rect getBottom() {
		return bottom;
	}

	public Rectangle getBack_cover() {
		return back_cover;
	}

	public Rectangle getFloor_cover() {
		return floor_cover;
	}

	public double getBase_angle() {
		return base_angle;
	}

	public void setPlayer( Player player ) {
		this.player = player;
	}

	public void setTop(Rect top) {
		this.top = top;
	}

	public void setBottom(Rect bottom) {
		this.bottom = bottom;
	}

	public void setBack_cover( Rectangle back_cover ) {
		this.back_cover = back_cover;
	}

	public void setFloor_cover( Rectangle floor_cover ) {
		this.floor_cover = floor_cover;
	}

	public void setBase_angle( double base_angle ) {
		this.base_angle = base_angle;
	}





}
