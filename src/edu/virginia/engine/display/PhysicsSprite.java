package edu.virginia.engine.display;

import java.awt.Point;
import java.util.ArrayList;

import edu.virginia.engine.util.Force;
import edu.virginia.engine.util.GameClock;

public class PhysicsSprite extends AnimatedSprite {
	public static final int PRUNE_TIME = 100;
	public static final double SPRITE_GRAVITY = 1;
	public static final Force GRAVITY = new Force(0, SPRITE_GRAVITY);
	public static final double COEFFICIENT_OF_RESTITUTION = 90;
	public static final double COEFFICIENT_OF_FRICTION = 100;

	private int mass;
	private double acceleration_x, acceleration_y;
	private double velocity_x, velocity_y;
	private ArrayList<Force> forces;
	private boolean gravity;
	private boolean movable;
	private boolean friction;



	public PhysicsSprite(String id, String filename) {
		super(id, filename);

		this.mass = 1;
		this.acceleration_x = 0;
		this.acceleration_y = 0;
		this.velocity_x = 0;
		this.velocity_y = 0;
		this.gravity = false;
		this.forces = new ArrayList<Force>();
		this.movable = false;
		this.friction = false;
	}

	
	
	public boolean hasFriction() {
		return friction;
	}


	public void setFriction(boolean friction) {
		this.friction = friction;
			
	}



	public int getMass() {
		return mass;
	}

	public double getAcceleration_x ( ) {
		return acceleration_x;
	}

	public double getAcceleration_y ( ) {
		return acceleration_y;
	}

	public double getVelocity_x ( ) {
		return velocity_x;
	}

	public double getVelocity_y ( ) {
		return velocity_y;
	}

	public ArrayList<Force> getForces ( ) {
		return forces;
	}

	public boolean hasGravity ( ) {
		return gravity;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}

	public void setAcceleration_x(double acceleration_x) {
		this.acceleration_x = acceleration_x;
	}

	public void setAcceleration_y(double acceleration_y) {
		this.acceleration_y = acceleration_y;
	}

	public void setVelocity_x(double velocity_x) {
		this.velocity_x = velocity_x;
	}

	public void setVelocity_y(double velocity_y) {
		this.velocity_y = velocity_y;
	}

	public void setForces(ArrayList<Force> forces) {
		this.forces = forces;
	}

	public void setGravity(boolean gravity) {
		this.gravity = gravity;
	}

	public void addForce( Force f , int duration ) {
		f.setExpiration(this.timeOfThisFrame + duration);
		this.forces.add ( f );
	}
	public void removeForce( Force f ) {
		this.forces.remove ( f );
	}

	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);

		
		GameClock gc = this.getClock();
		if (gc != null) 
			this.timeOfThisFrame = (int) gc.getElapsedTime();
		this.collisions = this.getAllCollisions();
		this.applyPhysics(this.timeOfLastFrame, this.timeOfThisFrame);

		this.timeOfLastFrame = this.timeOfThisFrame;
	}

	public void applyPhysics (int startTime, int endTime) {
		double f_x = 0, f_y = 0;
		double dt = 0.016; 
		
		
		// System.out.println("a: " + this.acceleration_x + " " + this.acceleration_y + "\n" + "v: " + this.velocity_x + " " + this.velocity_y + "\n p:" + this.getPosition().x + " " + this.getPosition().y + "\n" );

		this.pruneForces();
		
		Force friction = new Force(-this.velocity_x / 500,0);
		if (this.friction && Math.abs(this.velocity_x) > 0) 
			this.forces.add(friction);
		
		if(this.gravity)
			this.forces.add(GRAVITY);
		
		for (Force f : this.forces) {
			f_x += f.x;
			f_y += f.y;
		}
		

		System.out.println("Sum of forces in x: " + f_x + "\nSum of forces in y: " + f_y);
		this.acceleration_x = f_x/this.mass;
		this.acceleration_y = f_y/this.mass;
		System.out.println("Acceleration in x: " + this.acceleration_x + " , Acceleration in y: " + this.acceleration_y );

		int p_x = this.getPosition().x;
		int p_y = this.getPosition().y;
		
		
		System.out.println(dt + " dt");
		
		System.out.println(this.id  + " V in x: " + this.velocity_x + " , V in y: " + this.velocity_y );
		
		p_x += (int) Math.ceil(this.velocity_x * dt + this.acceleration_x * (dt * dt) / 2.0);
		p_y += (int) Math.ceil(this.velocity_y * dt + this.acceleration_y * (dt * dt) / 2.0);
		System.out.println(p_x + " " + p_y);
		this.setPosition ( new Point( p_x , p_y ) );
		System.out.println(this.id  + "P in x: " + this.getPosition().x + " , P in y: " + this.getPosition().y );
		
		this.forces.remove(GRAVITY);
		this.forces.remove(friction);
		this.velocity_x += this.acceleration_x * dt;
		this.velocity_y += this.acceleration_y * dt;
	}

	public GameClock getClock( ) {
		if (this.gamePtr != null) return this.gamePtr.clock;
		else return null;
	}

	public void handleCollision ( PhysicsSprite p ) {
		if ( !p.movable ) {
			double f_x, f_y;

			f_x = - COEFFICIENT_OF_RESTITUTION * this.mass * this.velocity_x * 0.16;
			f_y = - COEFFICIENT_OF_RESTITUTION * this.mass * this.velocity_y * 0.16;
			System.out.println(f_y);
			Force f = new Force(f_x,f_y);
			
			f.setExpiration( this.timeOfThisFrame + PRUNE_TIME  );
			System.out.println("Added force to " + this.id + "\n");
			this.forces.add ( f );
		} else {
			double p_x, p_y;
			double f_x, f_y;

			p_x = this.mass * this.velocity_x + p.mass * p.velocity_x;
			p_y = this.mass * this.velocity_y + p.mass * p.velocity_y;

			f_x = COEFFICIENT_OF_RESTITUTION * p_x / (this.timeOfThisFrame - this.timeOfLastFrame);
			f_y = COEFFICIENT_OF_RESTITUTION * p_y / (this.timeOfThisFrame - this.timeOfLastFrame);

			Force f = new Force(f_x/2,f_y/2); // FIX FIX FIX
			Force g = new Force(-f_x/2,-f_y/2);
			f.setExpiration( this.timeOfThisFrame + PRUNE_TIME );
			g.setExpiration( this.timeOfThisFrame + PRUNE_TIME );
			System.out.println("adding " + f);
			System.out.println("adding " + g);
			this.forces.add (f);
			p.forces.add(g);
		}
	}

	public void handleAllCollisions() {
		Game g = this.gamePtr;
		System.out.println("Control reached HAC in PS");
		System.out.println(this.collisions.toString());
		for ( String s : this.collisions ) {
			DisplayObject d = g.getById(s);
			System.out.println("Got " + d + " from the game");
			if (d instanceof PhysicsSprite) {
				PhysicsSprite p = (PhysicsSprite) d;
				this.handleCollision(p);
			}	
		}
	}


	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public void pruneForces () {
		ArrayList<Force> upForRemoval = new ArrayList<Force>();
		for (Force f : this.forces ) 
			if  ( f.getExpiration() <= this.timeOfThisFrame )
				upForRemoval.add(f);
		for (Force f : upForRemoval) 
			this.forces.remove(f);
	}




}
