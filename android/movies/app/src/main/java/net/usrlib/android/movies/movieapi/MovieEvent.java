package net.usrlib.android.movies.movieapi;

import net.usrlib.android.event.Event;

public final class MovieEvent {

	public static final Event DiscoverFeedLoaded   = new Event();
	public static final Event RequestLimitReached  = new Event();
	public static final Event MovieTrailersLoaded  = new Event();
	public static final Event MovieReviewsLoaded   = new Event();
	public static final Event MovieSetAsFavorite   = new Event();
	public static final Event MovieUnsetAsFavorite = new Event();
	public static final Event LoadDetailFragment   = new Event();

}
