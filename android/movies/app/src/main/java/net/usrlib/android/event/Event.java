package net.usrlib.android.event;

import java.util.Observable;

public final class Event extends Observable {

	public final void notifyError(Object data) {
		notify(new EventPacket(EventType.ERROR, data));
	}

	public final void notifyComplete(Object data) {
		notify(new EventPacket(EventType.COMPLETE, data));
	}

	public final void notifyComplete() {
		notify(new EventPacket(EventType.COMPLETE));
	}

	public final void addListener(Listener listener) {
		super.addObserver(listener);
	}

	public final void addListenerOnce(Listener listener) {
		listener.setRunOnce(true);
		super.addObserver(listener);
	}

	public final synchronized void deleteListener(Listener listener) {
		super.deleteObserver(listener);
	}

	private final void notify(Object data) {
		setChanged();
		notifyObservers(data);
		clearChanged();
	}

}
