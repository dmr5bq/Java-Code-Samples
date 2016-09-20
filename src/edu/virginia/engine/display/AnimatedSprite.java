package edu.virginia.engine.display;

import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

/* AnimatedSprite Class
 *
 * Fields:
 * - int width: width of animation frame tile
 * - int height: height of animation frame tile
 * - Animation currAnimation
 * 
 * Constructors:
 * - AnimatedSprite(String id)
 * - AnimatedSprite(String id, String filename)
 * - AnimatedSprite(String id, String filename, int ts) -> for square animation tiles
 * - AnimatedSprite(String id, String filename, int w, int h)
 *
 * Methods: 
 * - BufferedImage getSprite(int x, int y):
 *   	* from the AnimatedSprite displayImage, get the sprite at coordinate passed through parameter
 *   	* method returns subimage from the spritesheet
 * - accessors/mutators for width, height, currAnimation
 * - @Override update(ArrayList<String> pressedKeys) 
 *   	* calls the Animation update() method
 * - @Override draw(Graphics g)
 *   	* draws the current sprite to the screen, adjust as needed according to the transformations done
 *   	  to the object
 *
 */

public class AnimatedSprite extends Sprite {

	private int width;
	private int height;
	private Animation currAnimation;
	
	public AnimatedSprite(String id) { super(id); }

	public AnimatedSprite(String id, String filename) { super(id, filename); }

	public AnimatedSprite(String id, String filename, int ts) { 
		super(id, filename); 
		this.width = ts;
		this.height = ts;
	}

	public AnimatedSprite(String id, String filename, int w, int h) { 
		super(id, filename); 
		this.width = w;
		this.height = h;
	}

	//get the current sprite subimage from the spritesheet
	public BufferedImage getSprite(int x, int y) {
		BufferedImage currImage = this.getDisplayImage();
		if (currImage == null) {
			System.err.println("No image found!");
			return null;
		}	
		return currImage.getSubimage(x * this.width, y * this.height, this.width, this.height);
	}

	public void setAnimation(Animation a) {
		this.currAnimation = a;
	}

	public Animation getAnimation() {
		return this.currAnimation;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int w) {
		this.width = w;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int h) {
		this.height = h;
	}
	
	@Override
	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);
		this.currAnimation.update();
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		Graphics2D g2d = (Graphics2D) g;
		this.applyTransformations(g2d);

		g2d.drawImage(this.currAnimation.getSprite(), 
				(int) this.getPosition().getX(), 
				(int) this.getPosition().getY(), 
				this.width, this.height, null);

		this.reverseTransformations(g2d);	
	}
}
