package edu.virginia.engine.managers;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.game.Enemy;
import edu.virginia.engine.game.Energy;
import edu.virginia.engine.game.Parameters;
import edu.virginia.engine.game.Player;
import edu.virginia.engine.game.PlayerDamageEvent;
import edu.virginia.engine.game.Prototype;
import edu.virginia.engine.layers.BottomLayer;
import edu.virginia.engine.layers.Layer;
import edu.virginia.engine.layers.TopLayer;

public class DifficultyManager implements IEventListener {



	public static int len = Parameters.DIMENSION;
	public static int player_x = len / 4;
	public static int player_y = len / 2;

	// other fields
	private double currentTime;
	private int killCount;


	//computation-used fields
	private int totalEnergy;
	private int totalEnemies;					// used to count how many Enemy objects are on the screen
	private double enemyGenerationRate;			// helps restrict how quickly Enemy objects may generate, sets rate of generation (Enemy generations / millisecond)
	private int energyDistance;		// helps restrict how quickly Energy objects may generate, sets rate of generation (Enemy generations / millisecond)
	private double timeOfLastDamage;
	private int damageRate;
	private String difficulty;

	// tree pointer field
	public BottomLayer bottomLayer;
	public Prototype parent;

	// Constructor
	public DifficultyManager(BottomLayer bottomLayer, Prototype parent) {
		// field initializations:
		this.currentTime = System.currentTimeMillis(); 	// set the current time field to the system time
		this.killCount = 0;								// start off with a kill count of zero
		this.enemyGenerationRate = Parameters.EASY_SPAWN_RATE;		// helps restrict how quickly Enemy objects may generate, set to easiest version
		this.energyDistance = Parameters.EASY_ENERGY_DISTANCE;
		this.bottomLayer = bottomLayer; 	// sets the bottomLayer pointer to the instance of a bottom layer that generated the enemy
		this.parent = parent;
		this.timeOfLastDamage = currentTime;
		this.damageRate = Parameters.HIT_DAMAGE_MAX_EASY;
		this.totalEnemies = 0;
		this.totalEnergy = 0;
		this.difficulty = Parameters.DIFFICULTY_EASY;
	}


	public void registerKill( ) {
		++this.killCount;		// increases number of kills tracked
	}

	public void update( ) {
		/*
		 *  This method runs all services related to the DifficultyManager class with each
		 *  update() of the game loop, so this is all executed once per frame.
		 *  
		 *  Layout:
		 *  * Definitions
		 *  * Procedural Code:
		 *  * 	* 1. Update the current time field.
		 *  *	* 2. Check damage on the player and prune enemies that have run off the screen.
		 *  *	* 3. Check if new Energy should be generated and if the last has already disappeared 
		 *  *	*	  and add it to bottom layer.
		 *  *	* 4. Check if new Enemy should be generated based on current time.
		 */

		// locals and optimizations
		//		pointers
		Player player_ptr = this.parent.topLayer.getPlayer();		// for readability
		BottomLayer bottom_ptr = this.bottomLayer;					// for runtime opt
		ArrayList<Enemy> enemies_ptr = bottom_ptr.enemies;			// for runtime opt
		//		local copies
		ArrayList<Enemy> copy = new ArrayList<Enemy>( enemies_ptr );// local enemy list copy to prevent co-modification

		// conditions booleans:
		//		null check(s)
		boolean energy_alias_null = (bottom_ptr.active_energy == null); //for readability
		//		time check(s)
		
		boolean is_time_for_enemy = this.timeforEnemy();				//for readability

		////////////////////////////////////////
		// // // begin procedural code: // // //
		////////////////////////////////////////

		// update time field 
		this.currentTime = System.currentTimeMillis( );

		// check for damage on Player and prune enemies	
		for ( Enemy enemy : copy )  {
			
			Point pos  = enemy.getPosition(); // runtime opt and for readability
			
			// check if enemy is in the hit-box of the player, damage the player if it is
			if ( player_ptr.checkHit ( pos ) )						
				enemy.handleCollision( );
		}

		
		
		// check to see if a new Enemy should be generated, i.e. if no energy is aliased by active_energy and enough time has passed
		if ( energy_alias_null  ) {	
			
			// generate an energy at the end of the screen
			Energy next_to_add = generateEnergy();

			// reissue alias to the new energy so that it is drawn and updated
			bottom_ptr.active_energy = next_to_add;
			
			++this.totalEnergy;

			// tell the object what time the last energy was generated
		}



		//check if it is time for an enemy to be generated
		if ( is_time_for_enemy ) {

			// generate a randomly located enemy in the generation zone
			Enemy next_to_add = generateEnemy();
			
			// add new enemy to the bottom layer's list for monitoring
			enemies_ptr.add ( next_to_add );
		}
	}


	public boolean timeforEnemy() {
		// calculates how many enemies should be on screen and returns true if a new Enemy should be created
		return ( (this.currentTime - this.parent.start_time)  * this.enemyGenerationRate > this.totalEnemies);
	}



	public Enemy generateEnemy() {
		
		
		
		Enemy tmp_enemy = new Enemy("Enemy" + this.totalEnemies, this.bottomLayer, this );  // will be modified with new random coordinates
		Rectangle gen_zone = this.parent.topLayer.generationZone; // for readability

		int x_shift = Parameters.X_STEP; // determines how far the enemies should be generated from the player in the X-direction
		int y_shift = Parameters.Y_STEP; // determines how far the enemies should be generated from the player in the Y-direction

		Random x = new Random(); // generates coordinates

		int p_x = x.nextInt(gen_zone.width - x_shift) + x_shift; // generates a shifted random integer for the x-coordinate
		int p_y = x.nextInt(gen_zone.height) - y_shift;			 // generates a shifted random integer for the y-coordinate

		tmp_enemy.setPosition(new Point ( gen_zone.x + p_x, gen_zone.y + p_y ) );

		++this.totalEnemies; // include new Enemy in on screen count
		
		return tmp_enemy;
	}

	
	
	public Energy generateEnergy( ) {

		//locals:
		Energy tmp_energy = new Energy ( this.bottomLayer );  // will be modified with new random coordinates

		// calculate the correct position for the Energy on the screen
		
		Random x = new Random();
		
		int rand_x =  x.nextInt( this.energyDistance);
		
		int p_x = len + rand_x; // set to the x-bound of the screen - 200
		int p_y = Energy.size_y + Parameters.ENERGY_HOVER_HEIGHT ;	// set at constant height above baseline on screen

		tmp_energy.setPosition( new Point ( p_x , player_y - p_y ) );
		
		++this.totalEnergy;
		
		return tmp_energy;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public int getKillCount() {
		return killCount;
	}

	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}


	public Prototype getParent() {
		return parent;
	}


	public void setParent(Prototype parent) {
		this.parent = parent;
	}


	@Override
	public void handleEvent(Event event) {
		
		// locals:
		String event_key = event.getEventType( ); // gets string key from parameter event

		if ( event_key == PlayerDamageEvent.PLAYER_DAMAGE_KEY ) 
			handlePlayerDamage();	
	}

	public void handlePlayerDamage() {
		// method-scope locals: 
		double last_damage_time = this.timeOfLastDamage; 	// runtime opt
		double cur_time = this.currentTime; 				// runtime opt

		if ( cur_time >= last_damage_time + Parameters.DAMAGE_LATENCY ) {
			
			// if-scope locals:
			Random damage_decider = new Random();						// Random used to decide damage
			int damage_rate = damage_decider.nextInt ( this.damageRate );	// Picks a damage multiplier between 0 and the parameter
			TopLayer tl_ref = this.parent.topLayer;						// For readability

			// calculate new value for health
			double new_health = tl_ref.getHealth() - damage_rate;

			// set the Top Layer health attribute to the new value
			tl_ref.setHealth(new_health);
		}
	}
}
