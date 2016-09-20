package edu.virginia.engine.managers;

import edu.virginia.engine.game.EndGameEvent;
import edu.virginia.engine.game.Enemy;
import edu.virginia.engine.game.EnemyDeathEvent;
import edu.virginia.engine.game.PlayerDamageEvent;
import edu.virginia.engine.game.Prototype;

public class ListenerManager {

	private Prototype prototype;
	public boolean initialized;

	public ListenerManager ( Prototype prototype ) {
		this.prototype = prototype;
	}

	public void initialize( ) {

		this.prototype.topLayer.addEventListener(this.prototype.endManager, EndGameEvent.END_GAME_KEY);

		for (Enemy enemy : this.prototype.bottomLayer.enemies ) {
			// register the bottom layer as a listener for its death events
			enemy.addEventListener( this.prototype.bottomLayer , EnemyDeathEvent.ENEMY_DEATH_KEY );

			// register the top layer as a listener for its damage events
			enemy.addEventListener( this.prototype.difficultyManager , PlayerDamageEvent.PLAYER_DAMAGE_KEY );
		}

		this.initialized = true;
	}

	public void registerNewEnemy( Enemy enemy ) {
		// register the bottom layer as a listener for its death events
		enemy.addEventListener( this.prototype.bottomLayer , EnemyDeathEvent.ENEMY_DEATH_KEY );

		// register the top layer as a listener for its damage events
		enemy.addEventListener( this.prototype.difficultyManager , PlayerDamageEvent.PLAYER_DAMAGE_KEY );
	}

}
