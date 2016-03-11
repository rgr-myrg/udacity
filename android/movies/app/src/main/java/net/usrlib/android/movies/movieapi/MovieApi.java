package net.usrlib.android.movies.movieapi;

import android.util.Log;

import net.usrlib.android.util.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieApi {

	public static final String NAME = MovieApi.class.getSimpleName();

	private HttpRequest mHttpRequest;
	private String mCurrentSortBy = "";

	private int mPageNumber = 0;
	private boolean mIsFetchingData;

	public void setPageNumber(final int pageNumber) {
		mPageNumber = pageNumber;
	}

	public int getPageNumber() {
		return mPageNumber;
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

	public void fetchMovieTrailersWithId(final int id) {
		mIsFetchingData = true;

		// Set 'true' for fetching Movie Trailers
		makeHttpRequest(true);

		mHttpRequest.fetchJsonObjectWithUrl(MovieUrl.getTrailersUrl(id));
	}

	private void loadJsonFeedSortedBy(final String sortBy) {
		// Cap off requests when the limit is reached
		if (mPageNumber >= MovieVars.PAGE_COUNT_LIMIT) {
			MovieEvent.RequestLimitReached.notifyComplete();
			return;
		}

		mIsFetchingData = true;
		mCurrentSortBy = sortBy;

		// Set to false for fetching Movies in Discovery mode
		makeHttpRequest(false);

		Log.d(NAME, "Fetching page: " + mPageNumber + " sort by: " + mCurrentSortBy);

		mHttpRequest.fetchJsonObjectWithUrl(
				MovieUrl.getDiscoverUrl(mCurrentSortBy, mPageNumber)
		);
	}

	private void makeHttpRequest(final boolean hasMovieTrailers) {
		// Create a new task each request. AsyncTask runs once and terminates.
		mHttpRequest = new HttpRequest(
				new HttpRequest.Delegate() {
					@Override
					public void onPostExecuteComplete(Object object) {
						// Flag this request as completed
						mIsFetchingData = false;

						if (hasMovieTrailers) {
							ArrayList<MovieTrailerVO> trailerItems = parseTrailersJsonData((JSONObject) object);

							// Trigger MovieTrailersLoaded Event
							MovieEvent.MovieTrailersLoaded.notifyComplete(trailerItems);
						} else {
							ArrayList<MovieItemVO> arrayList = parseDiscoverJsonData((JSONObject) object);

							// Trigger DiscoverFeedLoaded Event
							MovieEvent.DiscoverFeedLoaded.notifyComplete(arrayList);
						}
					}

					@Override
					public void onError(String message) {
						Log.e(NAME, message);
						mIsFetchingData = false;

						if (hasMovieTrailers) {
							MovieEvent.MovieTrailersLoaded.notifyError(message);
						} else {
							MovieEvent.DiscoverFeedLoaded.notifyError(message);
						}
					}
				}
		);
	}

	private ArrayList<MovieItemVO> parseDiscoverJsonData(final JSONObject jsonObject) {
		JSONArray results = jsonObject.optJSONArray(MovieVars.RESULTS_KEY);

		ArrayList<MovieItemVO> movieItems = new ArrayList<MovieItemVO>();

		Log.d(NAME, "Retrieved Movie Items with length: " + String.valueOf(results.length()) );

		for (int i = 0; i < results.length(); i++) {
			MovieItemVO item = MovieItemVO.fromJsonObject(results.optJSONObject(i));
			movieItems.add(item);
		}

		return movieItems;
	}

	private ArrayList<MovieTrailerVO> parseTrailersJsonData(final JSONObject jsonObject) {
		JSONArray results = jsonObject.optJSONArray(MovieVars.RESULTS_KEY);

		ArrayList<MovieTrailerVO> trailerItems = new ArrayList<MovieTrailerVO>();

		Log.d(NAME, "Retrieved Trailers with length: " + String.valueOf(results.length()));

		for (int i = 0; i < results.length(); i++) {
			MovieTrailerVO item = MovieTrailerVO.fromJsonObject(results.optJSONObject(i));
			trailerItems.add(item);
		}

		return trailerItems;
	}

}
