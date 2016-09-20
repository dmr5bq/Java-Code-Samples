package edu.virginia.engine.tween;

public class TweenParameter {
	private TweenableParameter parameter;
	private double startValue;
	private double endValue;
	private double duration;
	private double startTime;
	private double curTime;
	private TweenTransition transition; 
// startTime
// 	-1
	//update
		// if startTime is -1
		// use System.getCurrentTimeMillis
	
	public TweenParameter( TweenableParameter t , double s , double e , double d ) {
		this.parameter = t;
		this.startValue = s;
		this.endValue = e;
		this.duration = d;
		this.startTime = -1;
		this.transition = new TweenTransition();
	}
	
	public TweenableParameter getParameter() {
		return this.parameter;
	}

	public double getStartValue() {
		return this.startValue;
	}

	public double getCurTime() {
		return curTime;
	}

	public TweenTransition getTransition() {
		return transition;
	}

	public void setParameter(TweenableParameter parameter) {
		this.parameter = parameter;
	}

	public void setStartValue(double startValue) {
		this.startValue = startValue;
	}

	public void setEndValue(double endValue) {
		this.endValue = endValue;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public void setCurTime(double curTime) {
		this.curTime = curTime;
	}

	public void setTransition(TweenTransition transition) {
		this.transition = transition;
	}

	public double getEndValue() {
		return this.endValue;
	}

	public double getDuration() {
		return this.duration;
	}
	
	public void setStartTime(double time) {
		this.startTime = time;
	}

	public double getStartTime() {
		return this.startTime;
	}
	
	public void update() {
		curTime = System.currentTimeMillis();
		if ( this.startTime == -1 ) 
			this.startTime = curTime;
		
		if (!this.isComplete()) {
			this.curTime = curTime - this.startTime;
			System.out.println(this.curTime);
			this.transition.setPercentDone(this.curTime / this.duration);
			this.transition.applyTranstion(this.transition.getPercentDone(), "");
		}
	}

	public boolean isComplete() {
		return ( this.curTime >= ( this.startTime + this.duration ) || this.transition.getPercentDone() == 1.00 );
	}
}
