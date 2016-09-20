package edu.virginia.engine.display;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenableParameter;
import edu.virginia.engine.util.GameClock;



/**
 * Highest level class for creating a game in Java.
 * 
 * */
public class Game extends DisplayObject implements ActionListener, KeyListener {


	protected TweenJuggler tweenJuggler;
	private ArrayList<DisplayObject> displayObjects;

	/* Frames per second this game runs at */
	private int FRAMES_PER_SEC = 60;

	/* The main JFrame that holds this game */
	private JFrame mainFrame;

	/* Timer that this game runs on */
	private Timer gameTimer;
	public GameClock clock;

	/* The JPanel for this game */
	private GameScenePanel scenePanel;

	public Game(String gameId, int width, int height) {
		super(gameId);
		this.tweenJuggler = new TweenJuggler();
		this.tweenJuggler.linkGame(this);
		setUpMainFrame(gameId, width, height);

		setScenePanel(new GameScenePanel(this));
		this.displayObjects = new ArrayList<DisplayObject>();

		/* Use an absolute layout */
		scenePanel.setLayout(null);
	}


	public void setFramesPerSecond(int fps){
		if(fps > 0) this.FRAMES_PER_SEC = fps;
	}

	public double getElapsedTime() {
		return clock.getElapsedTime();
	}

	public void setUpMainFrame(String gameId, int width, int height) {
		this.mainFrame = new JFrame();
		getMainFrame().setTitle(gameId);
		getMainFrame().setResizable(false);
		getMainFrame().setVisible(true);
		getMainFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getMainFrame().setBounds(0, 0, width, height);
		getMainFrame().addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		getMainFrame().addKeyListener(this);
	}

	/**
	 * Starts the game
	 */
	public void start() {
		if (gameTimer == null) {
			clock = new GameClock();
			gameTimer = new Timer(1000 / FRAMES_PER_SEC, this);
			gameTimer.start();
		} else {
			gameTimer.start();
		}
	}

	/**
	 * Stops the animation.
	 */
	public void stop() {
		pause();
		gameTimer = null;
	}

	public void pause() {
		if (gameTimer != null) {
			gameTimer.stop();
		}
	}

	public void exitGame(){
		stop();
		this.mainFrame.setVisible(false);
		this.mainFrame.dispose();
	}

	/**
	 * Close the window
	 * */
	public void closeGame(){
		this.stop();
		if(this.getMainFrame() != null){
			this.getMainFrame().setVisible(false);
			this.getMainFrame().dispose();
		}
	}


	/**
	 * Called once per frame. updates the game, redraws the screen, etc. May
	 * need to optimize this if games get too slow.
	 * */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaintGame();
	}

	/**
	 * Forces a repaint
	 * */
	public void repaint() { repaintGame(); }
	public void repaintGame(){
		if(getScenePanel() != null){
			getScenePanel().validate();
			getScenePanel().repaint();
		}
	}

	protected void nextFrame(Graphics g) {

		try {
			/* Update all objects on the stage */
			this.update(pressedKeys);
			/* Draw everything on the screen */
			this.draw(g);
		} catch (Exception e) {
			System.out
			.println("Exception in nextFrame of game. Stopping game (no frames will be drawn anymore");
			stop();
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics g){
		/* Start with no transparency */
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));
		super.draw(g);
	}

	public JFrame getMainFrame() {
		return this.mainFrame;
	}

	public void setScenePanel(GameScenePanel scenePanel) {
		this.scenePanel = scenePanel;
		this.getMainFrame().add(this.scenePanel);
		getMainFrame().setFocusable(true);
		getMainFrame().requestFocusInWindow();
	}

	public GameScenePanel getScenePanel() {
		return scenePanel;
	}

	ArrayList<String> pressedKeys = new ArrayList<String>();
	@Override
	public void keyPressed(KeyEvent e) {
		if(!pressedKeys.contains(KeyEvent.getKeyText(e.getKeyCode())))
			pressedKeys.add(KeyEvent.getKeyText(e.getKeyCode()));
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(pressedKeys.contains(KeyEvent.getKeyText(e.getKeyCode())))
			pressedKeys.remove(KeyEvent.getKeyText(e.getKeyCode()));

	}

	public void addDisplayObject(DisplayObject d) {
		this.displayObjects.add(d);
	}

	public void removeDisplayObject(DisplayObject d) {
		this.displayObjects.add(d);
	}

	public boolean contains(String id) {
		for ( DisplayObject d : this.displayObjects ) 
			if (d.getId().equals(id))
				return true;
		return false;
	}

	public DisplayObject getById(String id) {
		if (this.contains(id)) {
			
			for (DisplayObject e : this.displayObjects)
				if (e.getId().equals(id))
					return e;
		}
		return null;	
	}

	public ArrayList<DisplayObject> getDisplayObjects() {
		return displayObjects;
	}


	public void setDisplayObjects(ArrayList<DisplayObject> displayObjects) {
		this.displayObjects = displayObjects;
	}
	
	


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	


}
