package edu.virginia.engine.tween;

public class TweenTransition {
	
	public static final String F_QUADRATIC = "quad";
	public static final String F_BACK = "back";
	private static final String F_LINEAR = "line";
	
	
	private double percentDone;
	private double multiplier;
	
	public TweenTransition() {
		this.percentDone = 0;
		this.multiplier = 0;
	}
	
	
	public double getPercentDone() {
		return percentDone;
	}
	


	public double getMultiplier() {
		return multiplier;
	}


	public void setPercentDone(double percentDone) {
		this.percentDone = percentDone;
	}


	public void setMultiplier(double value) {
		this.multiplier = value;
	}


	public void applyTranstion ( double percentDone, String transition ) {
		switch (transition) {
		case F_QUADRATIC:
			this.multiplier = easeQuadratic(percentDone);
			break;
		case F_BACK:
			this.multiplier = easeBack(percentDone);
			break;
		case F_LINEAR:
			this.multiplier = easeLinear(percentDone);
			break;
		default: 
			this.multiplier = easeLinear(percentDone);
		}
	}
	
	
	// specific functions
	public double easeQuadratic ( double percentDone ) {
		return (percentDone * percentDone); 
	}
	
	public double easeLinear ( double percentDone ) {
		return ( percentDone ); 
	}
	
	public double easeBack ( double percentDone ) {
		return 0;
	}
	
	
	
}
