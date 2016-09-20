package edu.virginia.engine.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.layers.Layer;
import edu.virginia.engine.layers.TopLayer;
import edu.virginia.engine.managers.DifficultyManager;

public class KillCountDisplay extends Sprite {

	// tree pointer
	private TopLayer parent;

	public KillCountDisplay ( Layer l ) {
		super("kill_count");

		// initialize tree pointer
		this.parent = ( TopLayer ) l; 
	}

	public void draw (Graphics g) {

		// locals:
		DifficultyManager dm_ptr = this.parent.parent.difficultyManager; 	// for better readability
		String tmp_s = "";
		
		if (dm_ptr != null )
			tmp_s = new Integer(dm_ptr.getKillCount()).toString(); 		 	// for better readability


		// initialize copy instance of the graphics and apply relevant transformations
		Graphics g2 = g.create();
		Graphics2D g2d = (Graphics2D) g2;

		applyTransformations(g2d);

		// specific drawing instructions
		g2d.setColor(Color.RED); // change pen color
		g2d.drawString(tmp_s, 0, 0);

		reverseTransformations(g2d);

		// delete instance
		g2.dispose();
	}
}
