package edu.virginia.engine.game;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

public class EnergyPickupEvent extends Event {

	public static final String ENERGY_PICKUP_KEY = "energy_pickup";
	
	public EnergyPickupEvent ( IEventDispatcher source ) {
		super(ENERGY_PICKUP_KEY, source);
	}
	
}
