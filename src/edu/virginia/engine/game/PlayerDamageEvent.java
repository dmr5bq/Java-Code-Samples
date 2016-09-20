package edu.virginia.engine.game;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

public class PlayerDamageEvent extends Event {
	
	public static final String PLAYER_DAMAGE_KEY = "player_damage";

	public PlayerDamageEvent(IEventDispatcher source) {
		super(PLAYER_DAMAGE_KEY, source);
	}
	
}
