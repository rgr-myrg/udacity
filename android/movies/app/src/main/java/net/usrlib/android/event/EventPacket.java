package net.usrlib.android.event;

public final class EventPacket {

	private EventType mType;
	private Object mData;

	public EventPacket(EventType type, Object data) {
		this.mType = type;
		this.mData = data;
	}

	public EventPacket(EventType type) {
		this.mType = type;
	}

	public final EventType getType() {
		return mType;
	}

	public final Object getData() {
		return mData;
	}

}
