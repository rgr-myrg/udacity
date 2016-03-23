package net.usrlib.android.movies.viewholder;

import android.content.Context;

import net.usrlib.android.movies.R;

public final class ResourceHolder {

	private static String sTitleMostPopular = null;
	private static String sTitleHighestRated = null;
	private static String sTitleFavorites = null;

	public static String getTitleMostPopular() {
		return sTitleMostPopular;
	}

	public static String getTitleHighestRated() {
		return sTitleHighestRated;
	}

	public static String getTitleFavorites() {
		return sTitleFavorites;
	}

	public static final void onCreate(Context context) {
		sTitleMostPopular = context.getString(R.string.title_most_popular);
		sTitleHighestRated = context.getString(R.string.title_highest_rated);
		sTitleFavorites = context.getString(R.string.title_favorites);
	}

}
