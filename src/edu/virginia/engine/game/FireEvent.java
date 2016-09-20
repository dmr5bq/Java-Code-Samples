package edu.virginia.engine.game;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventDispatcher;

public class FireEvent extends Event {
	public static final String FIRE_EVENT = "fire";
	
	public FireEvent(EventDispatcher source) {
		super("fire", source);
	} 
}
