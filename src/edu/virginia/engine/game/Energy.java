package edu.virginia.engine.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.layers.BottomLayer;
import edu.virginia.engine.layers.TopLayer;
import edu.virginia.engine.managers.DifficultyManager;

public class Energy extends Sprite  {
	
	// class statics:
	public static int len = TopLayer.len; // for readability
	public static int player_x = TopLayer.player_x; // for clarity
	public static int player_y = TopLayer.player_y; // for clarity
	public static int size_y = Parameters.ENERGY_DIAMETER;

	public BottomLayer parent;
	
	private double lastChange;
	
	public Energy(BottomLayer parent) {
		super("energy");
		this.parent = parent;
		this.setAlpha(Parameters.ENERGY_ALPHA_1);
		this.lastChange = 0;
	}

	
	public boolean inPickupRange() {
		
		Player player_ref = this.parent.parent.topLayer.player;
		
		Point cur = this.getPosition();
		
		
		Rectangle hit_box = player_ref.getHitBox();
		
		int x_max = hit_box.x + hit_box.width;
		int x_min = hit_box.x;
		
		return (cur.x < x_max && cur.x > x_min);
		
	}
	
	public void draw (Graphics g) {
		
		Point p = this.position;
		
		Graphics g_copy = g.create();
		
		Graphics2D g2d = (Graphics2D) g_copy;
		
		applyTransformations(g2d);
		
		g2d.setColor(Parameters.ENERGY_COLOR);
		g2d.fillOval (0 , 0 , Parameters.ENERGY_DIAMETER, Parameters.ENERGY_DIAMETER);
		
		reverseTransformations(g2d);
		
		g_copy.dispose();
		
		
 	}
	
	public void update( ArrayList<String> pressedKeys ) {
		
		DifficultyManager dm_ptr = this.parent.parent.difficultyManager;
		TopLayer tl_ptr = this.parent.parent.topLayer;
		double cur_time = dm_ptr.getCurrentTime();
		double cur_energy = tl_ptr.getEnergyLevel();
		String right_key = KeyEvent.getKeyText(KeyEvent.VK_RIGHT);
		
		
		if ( cur_time > this.lastChange + Parameters.PULSE_TIME ) {
			//cycle through alpha for animation
			if 		(this.alpha == Parameters.ENERGY_ALPHA_1) 
				this.alpha = Parameters.ENERGY_ALPHA_2;
			else if (this.alpha == Parameters.ENERGY_ALPHA_2) 
				this.alpha = Parameters.ENERGY_ALPHA_3;
			else if (this.alpha == Parameters.ENERGY_ALPHA_3)
				this.alpha = Parameters.ENERGY_ALPHA_1;
			
			this.lastChange = cur_time;
			
		}
		
		if ( this.inPickupRange() ) {
	
			if (cur_energy + Parameters.ENERGY_WORTH <= Parameters.MAX_ENERGY)
				tl_ptr.setEnergyLevel( cur_energy + Parameters.ENERGY_WORTH );
			else tl_ptr.setEnergyLevel(Parameters.MAX_ENERGY);
			
			this.parent.active_energy = null;
		}
		

		if ( pressedKeys.contains(right_key) ) {
			
			int x_cur = this.position.x;
			
			int x_next = x_cur - Parameters.RUN_STEP;
			
			this.position.x = x_next;
		}
	}
	
	

}
