package edu.virginia.engine.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObject;

public class Rect extends DisplayObject {
	
	// class fields
	private Rectangle rectangle;

	// Default constructor
	public Rect ( ) {
		super("rect");
		
		// initialize empty rectangle
		this.rectangle = new Rectangle ( );
	}
	
	// Specific constructor
	public Rect (int x, int y, int width, int height) {
		super ( "rect" );
		this.rectangle = new Rectangle ( x , y , width , height );
	}
	
	
	public void draw ( Graphics g) {
		g.setColor(Color.BLACK);
		
		
		// create copy instance of the graphics object and apply transformations
		Graphics g2 = g.create();
		Graphics2D g2d = (Graphics2D) g2;
		
		this.applyTransformations(g2d);		
		
		g2d.fillRect(0,0, this.rectangle.width, this.rectangle.height);
		this.reverseTransformations(g2d);	
		
		//delete instance
		g2d.dispose();

	}
	
	
	public void update( ArrayList<String> pressedKeys ) {
		
	}
 	
}
