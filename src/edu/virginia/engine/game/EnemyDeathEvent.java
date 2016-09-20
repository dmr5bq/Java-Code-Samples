package edu.virginia.engine.game;

import edu.virginia.engine.events.Event;

public class EnemyDeathEvent extends Event {
	
	public static final String ENEMY_DEATH_KEY = "enemy_death";
	
	public EnemyDeathEvent(Enemy e) {
		super(ENEMY_DEATH_KEY, e);
	}
}
