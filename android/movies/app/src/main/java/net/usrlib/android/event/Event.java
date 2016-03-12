package net.usrlib.android.event;

import java.util.Observable;

public class Event extends Observable {

	public void notifyError(Object data) {
		notify(new EventPacket(EventType.ERROR, data));
	}

	public void notifyComplete(Object data) {
		notify(new EventPacket(EventType.COMPLETE, data));
	}

	public void notifyComplete() {
		notify(new EventPacket(EventType.COMPLETE));
	}

	public void addListener(Listener listener) {
		super.addObserver(listener);
	}

	public void addListenerOnce(Listener listener) {
		listener.runOnce = true;
		super.addObserver(listener);
	}

	public synchronized void deleteListener(Listener listener) {
		super.deleteObserver(listener);
	}

	private void notify(Object data) {
		setChanged();
		notifyObservers(data);
		clearChanged();
	}

}
