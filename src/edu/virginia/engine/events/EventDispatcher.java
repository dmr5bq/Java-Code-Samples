package edu.virginia.engine.events;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDispatcher implements IEventDispatcher {

	private HashMap<String,ArrayList<IEventListener>> listeners;

	public EventDispatcher() {
		this.listeners = new HashMap<String,ArrayList<IEventListener>>();
	}

	@Override
	public void addEventListener(IEventListener listener, String eventType) {
		if (this.listeners.containsKey(eventType)) {
			this.listeners.get(eventType).add(listener);
		}
		else {
			this.listeners.put(eventType, new ArrayList<IEventListener>());
			this.listeners.get(eventType).add(listener);
		}
	}

	@Override
	public void removeEventListener(IEventListener listener, String eventType) {
		if (this.listeners.containsKey(eventType))
			this.listeners.get(eventType).remove(listener);	
	}

	@Override
	public void dispatchEvent(Event event) {
		String key = event.getEventType();
		if (this.listeners.containsKey(key)) {
			ArrayList<IEventListener> mapEntry = this.listeners.get(key);
			//call handleEvent on every entry in the ArrayList at that key
			for ( IEventListener i : mapEntry ) 
				i.handleEvent(event);
		}
	}

	@Override
	public boolean hasEventListener(IEventListener listener, String eventType) {
		return (this.listeners.get(eventType).contains(listener));
	}

}
