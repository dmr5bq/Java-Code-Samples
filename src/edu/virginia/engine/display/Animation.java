package edu.virginia.engine.display;

import java.awt.image.BufferedImage;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/* Animation Class
 *
 * Fields:
 * - List<Frame> frames: arraylist that stores of frames of animation
 * - int frameCount: count number of updates
 * - int frameDelay: should be between 1-12, test to see which works best
 * - int currFrame: current frame of animation
 * - int currDirection: moving forward -> + ; moving backward -> -
 * - int totalFrames: total amount of frames for your animation
 * - boolean stopped: check to see if the animation has stopped moving 
 *
 * Constructors:
 * - Animation(BufferedImage[] f, int fd)
 *
 * Methods: 
 * - accessors/mutators for all fields
 * - void start()
 *	* start the animation
 * - void stop()
 *   	* stop the animation
 * - void restart()
 *   	* restart the animation from the first frame
 * - void reset()
 * 	* stop and return animation to beginning state
 * - BufferedImage getSprite()
 *   	* returns the sprite at current array index
 * - void addFrame(BufferedImage, int d)
 *   	* create a new Frame object, add the Frame to the frames arraylist
 * - void update()
 *   	* iterate through the frames arraylist
 */


public class Animation {

	private List<Frame> frames = new ArrayList<Frame>();

	private int frameCount;
	private int frameDelay;
	private int currFrame;
	private int currDirection;
	private int totalFrames;
	private boolean stopped;

	public Animation(BufferedImage[] f, int fd) {

		for (int i = 0; i < f.length; i++) {
			addFrame(f[i], fd);
		}

		this.frameCount = 0;
		this.frameDelay = fd;
		this.currFrame = 0;
		this.currDirection = 1;
		this.totalFrames = this.frames.size();
		this.stopped = true;

	}

	public int getFrameCount() { return this.frameCount; } 
	public void setFrameCount(int fc) { this.frameCount = fc; }

	public int getFrameDelay() { return this.frameCount; }
	public void setFramedDelay(int fd) { this.frameDelay = fd; }

	public int getCurrFrame() { return this.currFrame; }
	public void setCurrFrame(int cd) { this.currFrame = cd; }

	public int getCurrDirection() { return this.currDirection; }
	public void setCurrDirection(int d) { this.currDirection = d; }

	public int getTotalFrames() { return this.totalFrames; }
	public void setTotalFrames(int tf) { this.totalFrames = tf; }

	public boolean getStopped() { return this.stopped; }
	public void setStopped(boolean s) { this.stopped = s; }

	public List<Frame> getFrames() { return this.frames; }
	
	public void start() {
		if (this.frames.size() == 0) {
			return;	
		}

		if (!this.stopped) {
			return;
		}

		this.stopped = false;
	}

	public void stop() {
		if (this.frames.size() == 0) {
			return;	
		}
		this.stopped = true;
	}
	
	public void restart() {
		if (this.frames.size() == 0) {
			return;	
		}

		this.stopped = false;
		this.currFrame = 0;
	}

	public void reset() {
		this.stopped = true;
		this.frameCount = 0;
		this.currFrame = 0;
	}

	public BufferedImage getSprite() {
		return this.frames.get(this.currFrame).getFrame();
	}

	private void addFrame(BufferedImage f, int d) {
		if (d < 0) {
			System.err.println("Invalid duration: " + d);
			throw new RuntimeException("Invalid duration: " + d);	
		}
		this.frames.add(new Frame(f, d));
		this.currFrame = 0;
	}

	public void update() {
		if (!this.stopped) {
			frameCount++;
			if (this.frameCount > this.frameDelay) {
				this.frameCount = 0; 
				this.currFrame += this.currDirection;

				if (this.currFrame > this.totalFrames - 1) {
					currFrame = 0 ; 
				}

				else if (currFrame < 0) {
					currFrame = totalFrames - 1;
				}
			}
		}

		
	}

}
