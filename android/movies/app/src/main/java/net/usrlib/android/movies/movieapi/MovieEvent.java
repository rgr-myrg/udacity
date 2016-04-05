package net.usrlib.android.movies.movieapi;

import net.usrlib.pattern.TinyEvent;

public final class MovieEvent {

	public static final TinyEvent DiscoverFeedLoaded   = new TinyEvent();
	public static final TinyEvent RequestLimitReached  = new TinyEvent();
	public static final TinyEvent MovieTrailersLoaded  = new TinyEvent();
	public static final TinyEvent MovieReviewsLoaded   = new TinyEvent();
	public static final TinyEvent MovieFavoriteChanged = new TinyEvent();
	public static final TinyEvent OptionsItemSelected  = new TinyEvent();

}
