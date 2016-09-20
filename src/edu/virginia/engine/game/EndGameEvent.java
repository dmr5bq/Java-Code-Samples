package edu.virginia.engine.game;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

public class EndGameEvent extends Event{
	
	public static final String END_GAME_KEY = "end_game";
	
	public EndGameEvent( IEventDispatcher source ) {
		super(END_GAME_KEY, source);
	}
	
	
}
