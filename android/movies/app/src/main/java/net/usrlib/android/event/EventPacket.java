package net.usrlib.android.event;

public class EventPacket {

	private EventType type;
	private Object data;

	public EventPacket(EventType type, Object data) {
		this.type = type;
		this.data = data;
	}

	public EventPacket(EventType type) {
		this.type = type;
	}

	public EventType getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

}
