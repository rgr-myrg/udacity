package net.usrlib.android.popularmovies.movieapi;

import android.util.Log;

import net.usrlib.android.popularmovies.util.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieBrowser {

	public static final String NAME = MovieBrowser.class.getSimpleName();

	private Delegate mDelegate;
	private HttpRequest mHttpRequest;
	private int mPageNumber = 1;
	private boolean mIsFetchingData;

	public interface Delegate {
		void onJsonFeedLoaded(ArrayList<MovieItemVO> arrayList);
	}

	public MovieBrowser(Delegate delegate) {
		mDelegate = delegate;
	}

	public void loadJsonFeedSortedBy(String sortBy) {
		// Prevent multiple requests
		if (mIsFetchingData) {
			return;
		}

		mIsFetchingData = true;

		// Create a new task each request. AsyncTask only runs once.
		mHttpRequest = new HttpRequest(
				new HttpRequest.Delegate() {
					@Override
					public void onPostExecuteComplete(Object object) {
						ArrayList<MovieItemVO> arrayList = parseJsonData((JSONObject) object);
						mDelegate.onJsonFeedLoaded(arrayList);

						// Flag this request as completed
						mIsFetchingData = false;

						// Increment page number for the next request
						mPageNumber++;
					}

					@Override
					public void onError(String message) {
						Log.e(NAME, message);
						mIsFetchingData = false;
					}
				}
		);

		mHttpRequest.fetchJsonObjectWithUrl(
				MovieUrl.getUrl(sortBy, mPageNumber)
		);
	}

	private ArrayList<MovieItemVO> parseJsonData(JSONObject jsonObject) {
		JSONArray results = jsonObject.optJSONArray(MovieVars.RESULTS_KEY);

		ArrayList<MovieItemVO> movieItems = new ArrayList<MovieItemVO>();

		Log.d(NAME, "Retrieved results. length: " + String.valueOf(results.length()) );

		for (int i = 0; i < results.length(); i++) {
			MovieItemVO item = MovieItemVO.fromJsonObject(results.optJSONObject(i));
			movieItems.add(item);
		}

		return movieItems;
	}

}
