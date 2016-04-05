package net.usrlib.android.movies.viewholder;

import android.content.Context;

import net.usrlib.android.movies.R;

public final class ResourceHolder {

	private static String sTitleMostPopular = null;
	private static String sTitleHighestRated = null;
	private static String sTitleFavorites = null;
	private static String sSavedFavoriteMsg = null;
	private static String sRemovedFavoriteMsg = null;
	private static String sNoInternetMsg = null;
	private static String sNoFavoritesMsg = null;

	public static String getTitleMostPopular() {
		return sTitleMostPopular;
	}

	public static String getTitleHighestRated() {
		return sTitleHighestRated;
	}

	public static String getTitleFavorites() {
		return sTitleFavorites;
	}

	public static String getSavedFavoriteMsg() {
		return sSavedFavoriteMsg;
	}

	public static String getRemovedFavoriteMsg() {
		return sRemovedFavoriteMsg;
	}

	public static String getNoFavoritesMsg() {
		return sNoFavoritesMsg;
	}

	public static String getNoInternetMsg() {
		return sNoInternetMsg;
	}

	public static final void onCreate(Context context) {
		sTitleMostPopular = context.getString(R.string.title_most_popular);
		sTitleHighestRated = context.getString(R.string.title_highest_rated);
		sTitleFavorites = context.getString(R.string.title_favorites);
		sSavedFavoriteMsg = context.getString(R.string.saved_to_favorites_message);
		sRemovedFavoriteMsg = context.getString(R.string.removed_from_favorites_message);
		sNoInternetMsg = context.getString(R.string.no_internet_message);
		sNoFavoritesMsg = context.getString(R.string.no_favorites_message);
	}

}
