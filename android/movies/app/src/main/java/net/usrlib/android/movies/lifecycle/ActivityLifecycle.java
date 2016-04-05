package net.usrlib.android.movies.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/*
 * ActivityLifecycle
 * Mocking this class instead of implementing ActivityLifecycleCallbacks.
 * ActivityLifecycleCallbacks requires API 14.
*/

public class ActivityLifecycle {

	// Just Logging lifecycle to become more familiar with app flow
	public static void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		Log.d("ActivityLifecycle", "onActivityCreated");
	}

	public static void onActivityDestroyed(Activity activity) {
		Log.d("ActivityLifecycle", "onActivityDestroyed");
	}

	public static void onActivityPaused(Activity activity) {
		Log.d("ActivityLifecycle", "onActivityPaused");
	}

	public static void onActivityResumed(Activity activity) {
		Log.d("ActivityLifecycle", "onActivityResumed");
	}

	public static void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		Log.d("ActivityLifecycle", "onActivitySaveInstanceState");
	}

	public static void onActivityStarted(Activity activity) {
		Log.d("ActivityLifecycle", "onActivityStarted");
	}

	public static void onActivityStopped(Activity activity) {
		Log.d("ActivityLifecycle", "onActivityStopped");
	}

}
