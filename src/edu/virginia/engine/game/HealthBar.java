package edu.virginia.engine.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import edu.virginia.engine.game.Parameters;
import edu.virginia.engine.layers.TopLayer;
import edu.virginia.engine.display.Sprite;

public class HealthBar extends Sprite {
	
	// tree pointer
	private TopLayer parent; // points to the Top Layer that instantiated the HEALTHBar

	public HealthBar ( TopLayer parent ) {
		super( "healthBar" ); // call to superclass constructor
		this.parent = parent; // sets parent pointer to the Top Layer that instantiated this HEALTH Bar
	}

	public void draw ( Graphics g ) {

		// Determine color of HEALTH bar by current HEALTH level
		Color bar_color;
		int health = (int) this.parent.getHealth();

		if 		(health > Parameters.HIGH_HEALTH_THRESHOLD) 
			bar_color = Parameters.HIGH_HEALTH_COLOR;

		else if (health > Parameters.MID_HEALTH_THRESHOLD) 
			bar_color = Parameters.MID_HEALTH_COLOR;

		else if (health > Parameters.LOW_HEALTH_THRESHOLD)
			bar_color = Parameters.LOW_HEALTH_COLOR;

		else 
			bar_color = Color.BLACK;


		// create clone instance of the graphics object
		Graphics g2 = g.create();
		Graphics2D g2d = (Graphics2D) g2;

		applyTransformations(g2d);

		// draw background bar
		g2d.setColor(Parameters.BACKGROUND_COLOR);
		g2d.fillRect(0, 0, Parameters.BACK_WIDTH, Parameters.BACK_HEIGHT );


		g2d.setColor(bar_color);

		// calculate the current proportional width of the HEALTH bar
		int currentWidth = (int) (health * Parameters.BACK_WIDTH / (2 * Parameters.HIGH_HEALTH_THRESHOLD));

		// draw overlaying rounded rectangle for HEALTH bar
		g2d.fillRect(0, 0, currentWidth, Parameters.BACK_HEIGHT);

		reverseTransformations(g2d);

		// delete instance
		g2.dispose();

	}


}
