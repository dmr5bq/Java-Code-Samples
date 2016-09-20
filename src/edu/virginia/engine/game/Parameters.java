package edu.virginia.engine.game;

import java.awt.Color;

public class  Parameters {
	// Prototype
	//		Game frame constraint
	public static final int DIMENSION = 900;
	//		Determines how BottomLayer shifts upon movement
	public static final int RUN_STEP = 3;
	// TopLayer
	//		angle bound constants
	public static final double ANGLE_LOWER_BOUND = - Math.PI/2; // high angle restriction
	public static final double ANGLE_UPPER_BOUND = 0;			// low angle restriction
	//		angle level constants
	public static final double HIGH_ENERGY_ANGLE = Math.PI/16; 
	public static final double MID_ENERGY_ANGLE = Math.PI/24;
	public static final double LOW_ENERGY_ANGLE = Math.PI/32;
	//		angle update constants
	public static final double ANGLE_STEP = Math.toRadians(1);
	//		energy bound constants
	public static final double MAX_ENERGY = 100;
	//		energy level constants
	public static final double HIGH_ENERGY_THRESHOLD = MAX_ENERGY / 2;
	public static final double MID_ENERGY_THRESHOLD = MAX_ENERGY / 4;
	public static final double LOW_ENERGY_THRESHOLD = MAX_ENERGY / 0;
	//		energy update constants
	public static final double NORMAL_ENERGY_STEP = 0.03;
	public static final double FIRE_ENERGY_STEP = 0.1;
	// 		health bound constraints
	public static final int MAX_HEALTH = 100;
	// MidLayer
	//		color definitions:
	public static final Color COLOR_NORMAL = new Color(0,0,0);
	public static final Color COLOR_FIRE = new Color(255,50,0);
	//		 alpha definitions:
	public static final double HIGH_ENERGY_ALPHA = 0.4;
	public static final double MID_ENERGY_ALPHA = 0.6;
	public static final double LOW_ENERGY_ALPHA = 0.8;
	public static final double FIRE_ALPHA = 0.3;
	// BottomLayer
	public static final double HIT_ANGLE_SHIFT = Math.toRadians(0.5);
	// DifficultyManager
	//		enemy generation rate constants
	public static final double EASY_SPAWN_RATE = 0.004;
	public static final double MID_SPAWN_RATE = 0.005;
	public static final double HARD_SPAWN_RATE = 0.006;
	//		movement speed steps
	public static final int X_STEP = 200;
	public static final int Y_STEP = 100;
	//		energy generation rate constants
	public static final double EASY_ENERGY_RATE = 0.001;
	public static final double MID_ENERGY_RATE = 0.0005;
	public static final double HARD_ENERGY_RATE = 0.00025;
	
	public static final int EASY_ENERGY_DISTANCE = 1000;
	public static final int MID_ENERGY_DISTANCE = 1000;
	public static final int HARD_ENERGY_DISTANCE = 1000;
	//		determines the height of the Enemy above the horizontal baseline
	public static final int ENERGY_HOVER_HEIGHT = 10;
	//		time between damages
	public static final int DAMAGE_LATENCY = 500; // set to 1/2 sec by default
	//		hit damages
	public static final int HIT_DAMAGE_MAX_EASY = 10;
	public static final int HIT_DAMAGE_MAX_MID = 15;
	public static final int HIT_DAMAGE_MAX_HARD = 18;
	//		difficulty strings
	public static final String DIFFICULTY_EASY = "easy";
	public static final String DIFFICULTY_MID = "mid";
	public static final String DIFFICULTY_HARD = "hard";
	// Player
	public static final int HIT_BOX_WIDTH = 100;
	public static final int HIT_BOX_HEIGHT = 100;
	public static final int HIT_TOLERANCE = 10;
	// Enemy
	//		updates constants:
	public static final int DAMAGE_STEP = 5;	// controls rate of diameter decrease and health decrease
	public static final double GROW_STEP = 1;	// controls rate of diameter increase 
	public static final double GROW_TIME = 0.1; // controls rate of growth updates
	public static final int MOVE_STEP = 5;		// controls rate of movement
	public static final int FADE_OUT_TIME = 300;
	//		diameter constraints:
	public static final int MAX_DIAMETER = 100;	// sets maximum diameter of an Enemy drawing
	public static final int MIN_DIAMETER = 5;	// sets minimum diameter of an Enemy drawing
	//		alpha constants:
	public static final double Parameters = 0.3;// sets Enemy transparency
	public static final int ENEMY_HOVER_DISTANCE = 0;
	public static final int BUFFER = 100;
	//		hit-angle constant
	public static final double ENEMY_HIT_TOLERANCE = Math.toRadians(0.5);
	//		color-constants
	public static final Color HIT_COLOR = new Color(100,0,0); 
	public static final Color NORMAL_COLOR = new Color(0,0,0); 
	//		tween constants
	public static final int TWEEN_LOCATION_BASE = 100;
	public static final int TWEEN_LOCATION_RANGE = 200;
	public static final double TWEEN_FINAL_SCALE = 5;
	// Energy
	public static final Color ENERGY_COLOR = new Color(50,255,0);
	public static final int ENERGY_DIAMETER = 40;
	public static final double ENERGY_ALPHA_1 = 0.9;
	public static final double ENERGY_ALPHA_2 = 0.7;
	public static final double ENERGY_ALPHA_3 = 0.5;
	public static final double PULSE_TIME = 300;
	public static final double ENERGY_WORTH = MAX_ENERGY / 5;
	// EnergyBar
	//		color constants:
	public static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	public static final Color HIGH_ENERGY_COLOR = Color.GREEN;
	public static final Color MID_ENERGY_COLOR = Color.YELLOW;
	public static final Color LOW_ENERGY_COLOR = Color.RED;
	//		dimension constants:
	public static final int BACK_WIDTH = 200;
	public static final int BACK_HEIGHT = 30;
	// HealthBar
	//		color constants:
	public static final Color HIGH_HEALTH_COLOR = Color.GREEN;
	public static final Color MID_HEALTH_COLOR = Color.YELLOW;
	public static final Color LOW_HEALTH_COLOR = Color.RED;
	//		energy-level constants:
	public static final double HIGH_HEALTH_THRESHOLD = MAX_HEALTH / 2;
	public static final double MID_HEALTH_THRESHOLD = MAX_HEALTH / 4;
	public static final double LOW_HEALTH_THRESHOLD = 0;
	public static final double ENEMY_ALPHA = 0.3;
	


}
