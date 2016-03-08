package net.usrlib.android.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public final class SharedPref {

	private static SharedPreferences preferences = null;

	public static final SharedPreferences getSharedPref(Activity activity) {
		if (preferences == null) {
			preferences = PreferenceManager.getDefaultSharedPreferences(
					activity.getBaseContext()
			);
		}

		return preferences;
	}

	public static final void setString(Activity activity, String key, String value) {
		Editor editor = getSharedPref(activity).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static final String getString(Activity activity, String key) {
		return getSharedPref(activity).getString(key, null);
	}

	public static final void setInt(Activity activity, String key, int value) {
		Editor editor = getSharedPref(activity).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static final int getInt(Activity activity, String key) {
		return getSharedPref(activity).getInt(key, 0);
	}

}
