package edu.virginia.engine.tween;

import java.util.ArrayList;

import edu.virginia.engine.display.Game;


public class TweenJuggler {

	private ArrayList<Tween> tweenList;
	public Game gamePtr;
	
	public TweenJuggler() {
		this.tweenList = new ArrayList<Tween>();
		this.gamePtr = null;
	}
	
	public void linkGame(Game g) {
		this.gamePtr = g;
	}
	
	public void add (Tween t) {
		this.tweenList.add(t);
	}
	
	public void update ( ) {
		//update loop
		for ( Tween t : this.tweenList ) {
			t.update();
		}
	
		ArrayList<Tween> tmp = new ArrayList<Tween>();
		tmp.addAll(this.tweenList);
		
		//cleanup loop
		for (Tween t : tmp)
			if (t.isComplete())
				this.tweenList.remove(t);
	}
	
	
	public double calculatePercentDone(TweenParameter t) {
		if (this.gamePtr.clock != null) 
			return (this.gamePtr.clock.getElapsedTime() - t.getStartTime()) / (t.getDuration());
		else 
			return 0;
	}
}
