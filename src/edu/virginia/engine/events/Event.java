package edu.virginia.engine.events;

public class Event {
	// common event type literals
	public static final String COLLISION_EVENT = "collision";

	
	protected String eventType;
	protected IEventDispatcher source;
	
	public Event() {
		this.eventType = "";
		this.source = null;
	}
	
	public Event( String type , IEventDispatcher source ) {
		this.eventType = type;
		this.source = source;
	}

	public String getEventType() {
		return eventType;
	}

	public IEventDispatcher getSource() {
		return source;
	}

	public void setEventType( String eventType ) {
		this.eventType = eventType;
	}

	public void setSource( IEventDispatcher source ) {
		this.source = source;
	}
}
