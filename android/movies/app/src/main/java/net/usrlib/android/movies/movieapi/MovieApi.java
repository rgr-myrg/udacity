package net.usrlib.android.movies.movieapi;

import android.util.Log;

import net.usrlib.android.movies.util.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieApi {

	public static final String NAME = MovieApi.class.getSimpleName();

	private Delegate mDelegate;
	private HttpRequest mHttpRequest;
	//To Do: Save last sort by in preferences
	private String mCurrentSortBy = "";
	private int mPageNumber = 0;
	private boolean mIsFetchingData;

	public interface Delegate {
		void onFeedLoaded(ArrayList<MovieItemVO> arrayList);
		void onPageLimitReached();
	}

	public MovieApi(final Delegate delegate) {
		mDelegate = delegate;
	}

	public void fetchFirstPageSortedBy(final String sortBy) {
		mPageNumber = 1;
		loadJsonFeedSortedBy(sortBy);
	}

	public void fetchNextPageSortedBy(final String sortBy) {
		// Prevent multiple requests
		if (mIsFetchingData) {
			return;
		}

		// Increment page number for the next request
		mPageNumber++;

		loadJsonFeedSortedBy(sortBy);
	}

	private void loadJsonFeedSortedBy(final String sortBy) {
		// Cap off requests when the limit is reached
		if (mPageNumber >= MovieVars.PAGE_COUNT_LIMIT) {
			mDelegate.onPageLimitReached();
			return;
		}

		mIsFetchingData = true;
		mCurrentSortBy = sortBy;

		// Create a new task each request. AsyncTask only runs once.
		startHttpRequest();
	}

	private void startHttpRequest() {
		mHttpRequest = new HttpRequest(
				new HttpRequest.Delegate() {
					@Override
					public void onPostExecuteComplete(Object object) {
						ArrayList<MovieItemVO> arrayList = parseJsonData((JSONObject) object);

						mDelegate.onFeedLoaded(arrayList);

						// Flag this request as completed
						mIsFetchingData = false;
					}

					@Override
					public void onError(String message) {
						Log.e(NAME, message);
						mIsFetchingData = false;
					}
				}
		);

		Log.d(NAME, "Fetching page: " + mPageNumber + " sort by: " + mCurrentSortBy);

		mHttpRequest.fetchJsonObjectWithUrl(
				MovieUrl.getUrl(mCurrentSortBy, mPageNumber)
		);
	}

	private ArrayList<MovieItemVO> parseJsonData(final JSONObject jsonObject) {
		JSONArray results = jsonObject.optJSONArray(MovieVars.RESULTS_KEY);

		ArrayList<MovieItemVO> movieItems = new ArrayList<MovieItemVO>();

		Log.d(NAME, "Retrieved results with length: " + String.valueOf(results.length()) );

		for (int i = 0; i < results.length(); i++) {
			MovieItemVO item = MovieItemVO.fromJsonObject(results.optJSONObject(i));
			movieItems.add(item);
		}

		return movieItems;
	}

}
