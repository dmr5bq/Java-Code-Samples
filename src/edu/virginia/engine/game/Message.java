package edu.virginia.engine.game;

public class Message {

	public double start;
	public double duration;
	public String text;
	
	public Message(String message, double start, double duration) {
		this.start = start;
		this.duration = duration;
		this.text = message;
	}
}
