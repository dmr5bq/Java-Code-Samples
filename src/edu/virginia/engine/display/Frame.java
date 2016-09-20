package edu.virginia.engine.display;

import java.awt.image.BufferedImage;

/* Frame Class
 * Fields:
 * - BufferedImage frame
 * - int duration
 * 
 * Constructors: 
 * - Frame(BufferedImage f, int d) 
 * 
 * Methods:
 * - BufferedImage getFrame()
 * - void setFrame(BufferedImage f)
 * - int getDuration()
 * - void setDuration()
 *
 */
public class Frame {
	private BufferedImage frame;
	private int duration;

	public Frame(BufferedImage f, int d) {
		this.frame = f;
		this.duration = d;
	}

	public BufferedImage getFrame() {
		return this.frame;
	}

	public void setFrame(BufferedImage f) {
		this.frame = f;
	}

	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int d) {
		this.duration = d;
	}
}
