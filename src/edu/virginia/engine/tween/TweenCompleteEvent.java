package edu.virginia.engine.tween;

import edu.virginia.engine.events.*;

public class TweenCompleteEvent extends Event {
	
	public static final String TWEEN_COMPLETE_EVENT = "tween_complete";
	
	private Tween tween;
	
	public TweenCompleteEvent( String eventType , Tween tween ) {
		this.tween = tween;
		this.eventType = eventType;
	}
	
	public Tween getTween() {
		return this.tween;
	}
	

}
