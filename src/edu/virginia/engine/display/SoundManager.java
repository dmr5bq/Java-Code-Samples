package edu.virginia.engine.display;

import java.io.*;
import javax.sound.sampled.*;

/* SoundManager Class
 * 
 * NOTE:
 * Each instance of the SoundManager class can point to one sound file in the resource/sounds directory.
 * 
 * Fields:
 * - String id: string the identifies the sound being played
 * - File soundFile: object that holds the sound file
 * - AudioInputStream stream: 
 * - AudioFormat format: gets the format of the file (although only *.wav files will work for this)
 * - DataLine.Info info
 * - Clip clip: object that plays the sound
 * - boolean playCompleted: indicates whether or not the entire clip has been played.
 * 
 * Constructors:
 * - N/A
 *
 * Methods:
 * - void loadSound(String id, String filename)
 *   	* takes in the id and file name of the sound you want to play
 *   	* the object will hold initialize the stream and format fields 
 * - void playSoundEffect(String id)
 *   	* takes in the string id, plays the loaded sound file once all the way through.
 * - void playMusic(String id)
 *   	* take in the string id, continuously loops the loaded sound file
 * - @Override void update(LineEvent e)
 * 	* method from LineListener interface, checks the progress of the clip being played
 */

public class SoundManager implements LineListener {
	
	private String id;
	private File soundFile;
	private AudioInputStream stream;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip clip;
	
	private boolean playCompleted;

	public void loadSound(String id, String filename) {
		String f = ("resources/sounds/" + filename);	
		try {
			this.id = id;
			this.soundFile = new File(f);	
			System.out.println("Sound chosen: " + this.soundFile);
			this.stream = AudioSystem.getAudioInputStream(this.soundFile);
			this.format = this.stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
		} 
		catch (UnsupportedAudioFileException e) {
			System.err.println("Error in loadSoundEffect method!");
			e.printStackTrace();
		}

		catch (IOException e) {
			System.err.println("Error in loadSoundEffect method!");
			e.printStackTrace();
		}
	}

	public void playSoundEffect(String id) {
		try {	
			this.clip = (Clip) AudioSystem.getLine(this.info);
			this.clip.addLineListener(this);
			this.clip.open(this.stream);	
			this.clip.start();

			while (!playCompleted) {
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}

		catch (IllegalArgumentException e){ 
			System.err.println("Error in playSoundEffect method!");
			e.printStackTrace();
		}

		catch (LineUnavailableException e){ 
			System.err.println("Error in playSoundEffect method!");
			e.printStackTrace();
		}

		catch (IOException e) {
			System.err.println("Error in loadSoundEffect method!");
			e.printStackTrace();
		}
	}

	public void playMusic(String id) {
		try {	
			this.clip = (Clip) AudioSystem.getLine(this.info);
			this.clip.addLineListener(this);
			this.clip.open(this.stream);	
			this.clip.loop(Clip.LOOP_CONTINUOUSLY);
			while (!playCompleted) {
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}

		catch (IllegalArgumentException e){ 
			System.err.println("Error in playSoundEffect method!");
			e.printStackTrace();
		}

		catch (LineUnavailableException e){ 
			System.err.println("Error in playSoundEffect method!");
			e.printStackTrace();
		}

		catch (IOException e) {
			System.err.println("Error in loadSoundEffect method!");
			e.printStackTrace();
		}	
	}


	@Override
	public void update (LineEvent e) {
		LineEvent.Type type = e.getType();
		if (type == LineEvent.Type.START) {
			//System.out.println("playback started");
		} else if (type == LineEvent.Type.STOP){
			playCompleted = true;
			//System.out.println("playback ended");
		}
	}
}
