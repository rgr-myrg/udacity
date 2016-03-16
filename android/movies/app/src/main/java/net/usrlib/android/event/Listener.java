package net.usrlib.android.event;

import java.util.Observable;
import java.util.Observer;

public class Listener implements Observer {

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

}
