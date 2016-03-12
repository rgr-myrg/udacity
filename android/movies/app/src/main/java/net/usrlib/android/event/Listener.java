package net.usrlib.android.event;

import java.util.Observable;
import java.util.Observer;

public class Listener implements Observer {

	protected boolean runOnce;

	@Override
	public void update(Observable observable, Object data) {
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

		if (runOnce) {
			observable.deleteObserver(this);
		}
	}

	// Must override in each instance.
	public void onComplete(Object eventData) {}
	public void onError(Object eventData) {}

}
