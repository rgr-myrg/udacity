package net.usrlib.android.movies.movieapi;

import net.usrlib.android.event.Event;

public class MovieEvent {

	public static final Event DiscoverFeedLoaded  = new Event();
	public static final Event RequestLimitReached = new Event();
	public static final Event MovieTrailersLoaded = new Event();
	public static final Event ActivityResultReady = new Event();

}