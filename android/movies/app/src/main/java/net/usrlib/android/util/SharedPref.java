package net.usrlib.android.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import net.usrlib.android.movies.movieapi.MovieVars;

public final class SharedPref {

	public static final String VIEW_TITLE_KEY = "viewTitle";

	public static final String getViewTitle(Activity activity) {
		return getSharedPref(activity).getString(VIEW_TITLE_KEY, MovieVars.MOST_POPULAR);
	}

	public static final void setViewTitle(Activity activity, String title) {
		Editor editor = getSharedPref(activity).edit();
		editor.putString(VIEW_TITLE_KEY, title);
		editor.commit();
	}

	public static final SharedPreferences getSharedPref(Activity activity) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(
						activity.getBaseContext()
				);

		return prefs;
	}

}
