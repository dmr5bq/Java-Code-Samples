package edu.virginia.engine.util;


public class Force {
	
	public double x, y;
	public int expiration;
	
	public Force(double x, double y) {
		this.x = x;
		this.y = y;
		this.expiration = 0xfffffad;
	}
	
	public void setExpiration( int ex ) {
		this.expiration = ex;
	}

	public int getExpiration() {
		System.out.println("exp: " + this.expiration);
		return this.expiration;
	}
	
}
