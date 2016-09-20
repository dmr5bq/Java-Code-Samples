package edu.virginia.engine.tween;

import java.awt.Point;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObject;

public class Tween {

	private DisplayObject object;
	private ArrayList<TweenParameter> list;
	
	public Tween(DisplayObject d) {
		this.object = d;
		this.list = new ArrayList<TweenParameter>();
	}
	
	public Tween(DisplayObject d, TweenTransition t) {
		this.object = d;
	}
	
	public void animate(TweenableParameter fieldToAnimate, double startVal, double endVal, double time) {
		TweenParameter tmp = new TweenParameter( fieldToAnimate , startVal , endVal , time );
		tmp.setStartTime( System.currentTimeMillis() );
		this.list.add( tmp );
	}
	
	public void update() {
		for (TweenParameter p : this.list ) {
			p.update();
			this.setValue ( p.getParameter() , p.getTransition().getMultiplier() * ( p.getEndValue() - p.getStartValue() ) + p.getStartValue() );
		}
	}
	
	public DisplayObject getObject() {
		return object;
	}


	public ArrayList<TweenParameter> getList() {
		return list;
	}

	public void setObject(DisplayObject object) {
		this.object = object;
	}


	public void setList(ArrayList<TweenParameter> list) {
		this.list = list;
	}

	private void setValue( TweenableParameter param , double value ) {
		switch (param) {
		case ALPHA:
			this.object.setAlpha( value );
			break;
		case SCALE_X:
			this.object.setScaleX( value );
			break;
		case SCALE_Y:
			this.object.setScaleY( value );
			break;
		case ROTATION:
			this.object.setRotation( value );
			break;
		case X:
			this.object.setPosition( new Point( (int) value, this.object.getPosition().y ));
			break;
		case Y:
			this.object.setPosition(new Point( this.object.getPosition().x , (int) value ));
			break;
		}
		
	}

	public boolean isComplete() {
		boolean retVal = false;
		
		for ( TweenParameter p : this.list ) {
			retVal &= (p.isComplete() && (this.object.getValueByType(p.getParameter()).equals(p.getEndValue())));
		}
		return retVal;
	}

	
}
