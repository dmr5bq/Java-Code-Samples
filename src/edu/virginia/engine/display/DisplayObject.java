package edu.virginia.engine.display;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.tween.TweenableParameter;

import javax.imageio.ImageIO;

/**
 * A very basic display object for a java based gaming engine
 * 
 * */
public class DisplayObject extends EventDispatcher {
	int cycleCount = 0;
	int lastSuccessfulClick = 0;
	int lastAttemptedClick = 0;
	public int timeOfLastFrame = 0;
	public int timeOfThisFrame = 0;

	protected Game gamePtr;

	protected ArrayList collisions;

	// parent reference
	protected DisplayObject parent;

	// determines draw location of top right corner
	protected Point position;

	// determines if item will be drawn
	protected boolean visible;

	// pivotPoint defined relative to position point
	protected Point pivotPoint;

	// determines scale
	protected double scaleX, scaleY;

	//determines rotation in radians around pivotPoint
	protected double rotation;

	//determines opacity on a scale from 0 to 1
	protected double alpha;

	/* All DisplayObject have a unique id */
	protected String id;

	/* The image that is displayed by this object */
	protected BufferedImage displayImage;

	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		this.setId(id);

		//defaults
		this.rotation = 0.0;
		this.scaleX = 1;
		this.scaleY = 1;
		this.alpha = 1.0;
		this.visible = true;
		this.position = new Point (0,0);
		this.pivotPoint = new Point (0,0);
		this.gamePtr = null;
		this.collisions = new ArrayList<String>();
	}

	public DisplayObject(String id, String fileName) {
		this.setId(id);
		this.setImage(fileName);

		// defaults
		this.rotation = 0;
		this.scaleX = 1;
		this.scaleY = 1;
		this.alpha = 1;
		this.visible = true;
		this.position = new Point (0,0);
		this.pivotPoint = new Point (0,0);
		this.gamePtr = null;
		this.collisions = new ArrayList<String>();
	}

	public void linkGame(Game g) {
		this.gamePtr = g;
		g.addDisplayObject(this);
		if (g.clock != null) {
			this.timeOfLastFrame = (int) this.gamePtr.clock.getElapsedTime();
			this.timeOfThisFrame = (int) this.gamePtr.clock.getElapsedTime();
		}
	}

	// all getters and setters
	public int getHeight() {
		return (int) (this.getUnscaledHeight() * this.scaleY);
	}

	public int getWidth() {
		return (int) (this.getUnscaledHeight() * this.scaleY);
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public boolean isVisible() {
		return visible;
	}

	public Point getPivotPoint() {
		return pivotPoint;
	}

	public double getScaleX() {
		return scaleX;
	}

	public double getScaleY() {
		return scaleY;
	}

	public double getRotation() {
		return rotation;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setPivotPoint(Point pivotPoint) {
		this.pivotPoint = pivotPoint;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public int getUnscaledWidth() {
		if(displayImage == null) return 0;
		return displayImage.getWidth();
	}

	public int getUnscaledHeight() {
		if(displayImage == null) return 0;
		return displayImage.getHeight();
	}

	public BufferedImage getDisplayImage() {
		return this.displayImage;
	}

	public DisplayObject getParent() {
		return parent;
	}

	public void setParent(DisplayObject parent) {
		this.parent = parent;
	}

	protected void setImage(String imageName) {
		if (imageName == null) {
			return;
		}
		displayImage = readImage(imageName);
		if (displayImage == null) {
			System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
		}
	}


	/**
	 * Helper function that simply reads an image from the given image name
	 * (looks in resources\\) and returns the bufferedimage for that filename
	 * */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			String file = ("resources" + File.separator + imageName);
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image == null) return;
		displayImage = image;
	}


	/**
	 * Invoked on every frame before drawing. Used to update this display
	 * objects state before the draw occurs. Should be overridden if necessary
	 * to update objects appropriately.
	 * */
	public void update(ArrayList<String> pressedKeys) {
		
	}

	/**
	 * Draws this image. This should be overloaded if a display object should
	 * draw to the screen differently. This method is automatically invoked on
	 * every frame.
	 * */
	public void draw(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		if (displayImage != null) {
			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */

			applyTransformations(g2d);
			/* Actually draw the image, perform the pivot point translation here */
			if (this.visible) {

				g2d.drawImage(this.displayImage,0,0,
						(int) (this.getUnscaledWidth()),
						(int) (this.getUnscaledHeight()), null);
				g2d.drawOval(this.pivotPoint.x, this.pivotPoint.y, 3, 3);
			}

			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d);

		}



	}


	/**
	 * Applies transformations for this display object to the given graphics
	 * object
	 * */
	protected void applyTransformations(Graphics2D g2d) {	
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) this.alpha);
		g2d.setComposite(ac);
		g2d.scale(this.scaleX, this.scaleY);
		g2d.translate(this.position.x / this.scaleX - this.pivotPoint.x, this.position.y / this.scaleY - this.pivotPoint.y);		
		g2d.rotate(this.rotation, this.pivotPoint.x, this.pivotPoint.y);

	}


	/**
	 * Reverses transformations for this display object to the given graphics
	 * object
	 * */
	protected void reverseTransformations(Graphics2D g2d) {

		g2d.rotate(-this.rotation, -this.pivotPoint.x, -this.pivotPoint.y);
		g2d.translate(- this.position.x / this.scaleX + this.pivotPoint.x, - this.position.y / this.scaleY + this.pivotPoint.y);
		g2d.scale(1/this.scaleX, 1/this.scaleY);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		g2d.setComposite(ac);
	}

	public Rectangle getLocalHitBox() {
		return new Rectangle(0,0,this.getUnscaledWidth(),this.getUnscaledHeight());
	}
	public Rectangle getGlobalHitBox() {
		return new Rectangle((int) (this.getPosition().x/this.scaleX + this.getPivotPoint().x),(int) (this.getPosition().y/this.scaleY + this.getPivotPoint().y),this.getUnscaledWidth(),this.getUnscaledHeight());
	}

	public boolean collidesWith(DisplayObject other) {
		Rectangle thisHitBox = this.getGlobalHitBox();
		Rectangle otherHitBox = other.getGlobalHitBox();

		// Left x 
		int leftX = Math.max(thisHitBox.x, otherHitBox.x);
		// Right x
		int rightX = Math.min(thisHitBox.x + thisHitBox.width, otherHitBox.x + otherHitBox.width);
		// Bottom y
		int botY = Math.max(thisHitBox.y, otherHitBox.y);
		// TopY
		int topY = Math.min(thisHitBox.y + thisHitBox.width, otherHitBox.y + otherHitBox.width);

		if (rightX > leftX && topY > botY) {
			return true;
		}
		else return false;
	}

	public ArrayList<String> getAllCollisions() {
		ArrayList<String> output = new ArrayList<String>();
		Game g = this.gamePtr;
		if (this.gamePtr != null) 
			for (DisplayObject d : g.getDisplayObjects() ) {
				if (this == d) continue;
				if (this.collidesWith(d)) {
					this.collisions.add(d.id);
					this.dispatchEvent(new Event(Event.COLLISION_EVENT, this));
					System.out.println("Event dispatched by " + this.id);
				}
			}

		return output;
	}
	
	public Object getValueByType(TweenableParameter p) {

		switch(p) {
		case X:
			return this.position.x;
		case Y:
			return this.position.y;
		case ALPHA:
			return this.alpha;
		case ROTATION:
			return this.rotation;
		case SCALE_X:
			return this.scaleX;
		case SCALE_Y:
			return this.scaleY;
		default:
			return null;
		}
		
	}





}