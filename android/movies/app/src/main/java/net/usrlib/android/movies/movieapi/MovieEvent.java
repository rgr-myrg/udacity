package net.usrlib.android.movies.movieapi;

import net.usrlib.pattern.TinyEvent;

public final class MovieEvent {

	public static final TinyEvent DiscoverFeedLoaded   = new TinyEvent();
	public static final TinyEvent RequestLimitReached  = new TinyEvent();
	public static final TinyEvent MovieTrailersLoaded  = new TinyEvent();
	public static final TinyEvent MovieReviewsLoaded   = new TinyEvent();
	public static final TinyEvent MovieSetAsFavorite   = new TinyEvent();
	public static final TinyEvent MovieUnsetAsFavorite = new TinyEvent();
	public static final TinyEvent LoadDetailFragment   = new TinyEvent();

}
