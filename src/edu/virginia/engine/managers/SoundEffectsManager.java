package edu.virginia.engine.managers;

import java.util.ArrayList;
import java.util.HashMap;

import edu.virginia.engine.display.SoundManager;
import edu.virginia.engine.game.*;

public class SoundEffectsManager {

	private SoundManager soundManager;
	public Prototype prototype;
	
	public HashMap<String, Boolean> toPlay;
	
	public SoundEffectsManager(Prototype parent) {
		this.prototype = parent;
		this.soundManager = this.prototype.soundManager;
	}
	
	public void loadAllSoundEffects() {
		this.soundManager.loadSound("gameover", "horror-go.wav");
		this.soundManager.loadSound("enemy", "i_see_you_voice.wav");
	}
	
	public void update ( ArrayList<String> pressedKeys ) {
		
		if (this.toPlay != null)
		for ( String key : this.toPlay.keySet() ) {
			if ( this.toPlay.get(key))
			this.soundManager.playSoundEffect(key);
			this.toPlay.put(key, false);
			
		}
		
	}
	
	public void playOnce ( String id ) {
		this.toPlay.put(id, true);
	}
	
	
	
}
