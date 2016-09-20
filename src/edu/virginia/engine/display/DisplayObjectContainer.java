package edu.virginia.engine.display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DisplayObjectContainer extends DisplayObject {

	private ArrayList<DisplayObject> children;

	public DisplayObjectContainer(String id) {
		super(id);
		this.children = new ArrayList<DisplayObject>();
	}

	public DisplayObjectContainer(String id, String fileName) {
		super(id, fileName);
		this.children = new ArrayList<DisplayObject>();
	}

	public ArrayList<DisplayObject> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<DisplayObject> children) {
		this.children = children;
	}

	public void addChild(DisplayObject child) {
		child.setParent(this);
		child.setPosition(this.pivotPoint);
		this.children.add(child);
	}

	public void removeChild(DisplayObject child) {
		if (this.children.contains(child))
			this.children.remove(child);
		else System.out.println("DOC.removeChild: Child not found!");
	}

	public void removeAllChildren() {
		for (DisplayObject d: this.children)
			this.removeChild(d);
	}

	public boolean contains(Object o) {
		if (o instanceof DisplayObject) {
			DisplayObject e = (DisplayObject) o;
			for (DisplayObject d : this.children )
				if ( d.equals(e) )
					return true;
		}
		return false;
	}

	public DisplayObject getDisplayObject ( String id ) {
		for (DisplayObject d : this.children )
			if (d.getId() == id)
				return d;
		return null;
	}

	public boolean hasChildren() {
		return !(this.children == null);
	}

	public void draw(Graphics g) {
		super.draw(g);
		DisplayObjectContainer doc = (DisplayObjectContainer) this;
		Graphics2D g2d = (Graphics2D) g;
		if (!doc.getChildren().isEmpty() && this.visible)
			for (DisplayObject d : doc.getChildren()) {
				applyTransformations(g2d);
				d.draw(g);
				reverseTransformations(g2d);
			}
	}

	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);
		for (DisplayObject d : this.children ) 
			d.update(pressedKeys);
	}
	/** 
	@Override
	public boolean collidesWith(DisplayObject d) {
		boolean retVal = ((DisplayObject) this).collidesWith(d);
		for (DisplayObject child : this.children ) {
			retVal = retVal || child.collidesWith(d);
		}
		
		return retVal;
	} **/
}

