package net.usrlib.android.event;

import java.util.Observable;
import java.util.Observer;

public final class Event extends Observable {

	public final void notifyError(final Object data) {
		notify(new EventPacket(EventType.ERROR, data));
	}

	public final void notifyComplete(final Object data) {
		notify(new EventPacket(EventType.COMPLETE, data));
	}

	public final void notifyComplete() {
		notify(new EventPacket(EventType.COMPLETE));
	}

	public final void addListener(final Listener listener) {
		super.addObserver(listener);
		listener.onRegister();
	}

	public final void addListenerOnce(final Listener listener) {
		listener.setRunOnce(true);
		super.addObserver(listener);
	}

	public final synchronized void removeListener(final Listener listener) {
		super.deleteObserver(listener);
		listener.onRemove();
	}

	private final void notify(final Object data) {
		setChanged();
		notifyObservers(data);
		clearChanged();
	}

	private final class EventPacket {

		private EventType mType;
		private Object mData;

		public EventPacket(final EventType type, final Object data) {
			this.mType = type;
			this.mData = data;
		}

		public EventPacket(final EventType type) {
			this.mType = type;
		}

		public final EventType getType() {
			return mType;
		}

		public final Object getData() {
			return mData;
		}

	}

	public enum EventType {
		COMPLETE,
		ERROR
	}

	public static class Listener implements Observer {

		private boolean mRunOnce;

		@Override
		public void update(final Observable observable, final Object data) {
			final EventPacket packet = (EventPacket) data;
			final EventType eventType = packet.getType();
			final Object eventData = packet.getData();

			switch (eventType) {
				case COMPLETE:
					onComplete(eventData);
					break;
				case ERROR:
					onError(eventData);
					break;
				default:
					break;
			}

			if (mRunOnce) {
				observable.deleteObserver(this);
			}
		}

		public final void setRunOnce(boolean runOnce) {
			this.mRunOnce = runOnce;
		}

		// Must override in each instance.
		public void onComplete(Object eventData) {}
		public void onError(Object eventData) {}
		public void onRegister() {}
		public void onRemove() {}

	}

}
