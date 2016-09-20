package edu.virginia.engine.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.layers.TopLayer;

public class EnergyBar extends Sprite {


	
	// tree pointer
	private TopLayer parent; // points to the Top Layer that instantiated the EnergyBar
	
	public EnergyBar( TopLayer parent ) {
		super( "energyBar" ); // call to superclass constructor
		this.parent = parent; // sets parent pointer to the Top Layer that instantiated this Energy Bar
	}
	
	public void draw( Graphics g ) {
		
		// Determine color of energy bar by current energy level
		Color bar_color;
		double energy = this.parent.getEnergyLevel();
		
		if 		(energy > Parameters.HIGH_ENERGY_THRESHOLD) 
			bar_color = Parameters.HIGH_ENERGY_COLOR;
		
		else if (energy > Parameters.MID_ENERGY_THRESHOLD) 
			bar_color = Parameters.MID_ENERGY_COLOR;
		
		 else if (energy > Parameters.LOW_ENERGY_THRESHOLD)
			bar_color = Parameters.LOW_ENERGY_COLOR;
		 else 
			 bar_color = Color.BLACK;

		
		// create clone instance of the graphics object
		Graphics g2 = g.create();
		Graphics2D g2d = (Graphics2D) g2;
		
		applyTransformations(g2d);
		
		// draw background bar
		g2d.setColor(Parameters.BACKGROUND_COLOR);
		g2d.fillRect (0, 0, Parameters.BACK_WIDTH, Parameters.BACK_HEIGHT );
		
		
		g2d.setColor(bar_color);
	
		// calculate the current proportional width of the energy bar
		int currentWidth = (int) (energy * Parameters.BACK_WIDTH / (2 * Parameters.HIGH_ENERGY_THRESHOLD));
		
		// draw overlaying rounded rectangle for energy bar
		g2d.fillRect(0, 0, currentWidth, Parameters.BACK_HEIGHT);
		
		reverseTransformations(g2d);
		
		// delete instance
		g2.dispose();
		
	}
	
}
