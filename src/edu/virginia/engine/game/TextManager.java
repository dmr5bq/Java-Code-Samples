package edu.virginia.engine.game;

import java.awt.Graphics;
import java.util.ArrayList;



public class TextManager {
	
	
	public static double wait_time = 1000;
	public static String start_message_1 = "It's dark. \t Really dark.";
	
	
	public Prototype prototype;
	
	public ArrayList<Message> messages;
	
	
	public TextManager(Prototype prototype) {
		this.prototype = prototype;
		this.messages = new ArrayList<Message>();
		
		this.messages.add(new Message("Hello, World", 1000, 20000));
	}
	
	
	public void update ( ArrayList<String> pressedKeys )  {
		
		double current_time = System.currentTimeMillis();
		double start_time = this.prototype.start_time;
		
		double elapsed_time = current_time - start_time;
		
		
	}
	
	public void draw ( Graphics g ) {
		
	}
	
	

}
