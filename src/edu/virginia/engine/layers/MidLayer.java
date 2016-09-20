package edu.virginia.engine.layers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.DisplayObjectContainer;
import edu.virginia.engine.game.Parameters;
import edu.virginia.engine.game.Prototype;

public class MidLayer extends DisplayObject implements Layer {

	// drawn fields
	private Rectangle background;

	// other fields
	public Color color;

	// tree pointers
	public Prototype parent;

	public MidLayer(Prototype p) {
		super("mid");

		// initialize drawn fields
		this.background = new Rectangle(0,0,Parameters.DIMENSION, Parameters.DIMENSION);

		// initialize other fields
		this.color = Parameters.COLOR_NORMAL;

		// initialize tree pointers
		this.parent = p; // set parent pointer to instance of Prototype that created the layer

		// set initial alpha
		this.setAlpha(Parameters.HIGH_ENERGY_ALPHA);
	}

	public void draw ( Graphics g ) {

		// get an int alpha from the 0.0 - 1.0 range alpha in DisplayObject
		int alpha = ( int ) Math.floor ( this.alpha * 255 ); // converts between the 0.0 - 1.0 and the 0 - 255 style alphas

		// set the color to a matching color at the alpha described
		Color c = new Color( this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha );
		g.setColor(c); 	// sets brush color to the current color field

		// apply the transforms to the copy Graphics instance
		Graphics g2 = g.create();
		Graphics2D g2d = (Graphics2D) g2;

		this.applyTransformations(g2d);

		// draw rectangle that covers screen underneath the top layer
		g.fillRect(0, 0, this.background.width, this.background.height);

		this.reverseTransformations(g2d);

		// get rid of instance
		g2d.dispose();
	}

	public void update(ArrayList pressedKeys) {


		String space_bar = KeyEvent.getKeyText(KeyEvent.VK_SPACE);

		TopLayer tl_ptr = this.parent.topLayer;

		double energy_level = tl_ptr.getEnergyLevel();

		if 	(pressedKeys.contains(space_bar)) {
			this.color = Parameters.COLOR_FIRE;
			this.alpha = Parameters.FIRE_ALPHA;
		} else 
			this.color = Parameters.COLOR_NORMAL;

		if		( energy_level > Parameters.HIGH_ENERGY_THRESHOLD ) 
			this.alpha = Parameters.HIGH_ENERGY_ALPHA;
		else if ( energy_level > Parameters.MID_ENERGY_THRESHOLD )  
			this.alpha = Parameters.MID_ENERGY_ALPHA;
		else if ( energy_level > Parameters.LOW_ENERGY_THRESHOLD ) 
			this.alpha = Parameters.LOW_ENERGY_ALPHA;


	}


}
