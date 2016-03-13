package net.usrlib.android.movies.movieapi;

import android.util.Log;

import net.usrlib.android.event.Event;
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

	private enum RequestType {
		DISCOVER_FEED,
		TRAILERS,
		REVIEWS
	};

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

		makeHttpRequest(MovieEvent.MovieTrailersLoaded, RequestType.TRAILERS);

		mHttpRequest.fetchJsonObjectWithUrl(MovieUrl.getTrailersUrl(id));
	}

	public void fetchMovieReviewsWithId(final int id) {
		mIsFetchingData = true;

		makeHttpRequest(MovieEvent.MovieReviewsLoaded, RequestType.REVIEWS);

		mHttpRequest.fetchJsonObjectWithUrl(MovieUrl.getReviewsUrl(id));
	}

	private void loadJsonFeedSortedBy(final String sortBy) {
		// Cap off requests when the limit is reached
		if (mPageNumber >= MovieVars.PAGE_COUNT_LIMIT) {
			MovieEvent.RequestLimitReached.notifyComplete();
			return;
		}

		mIsFetchingData = true;
		mCurrentSortBy = sortBy;

		makeHttpRequest(MovieEvent.DiscoverFeedLoaded, RequestType.DISCOVER_FEED);

		Log.d(NAME, "Fetching page: " + mPageNumber + " sort by: " + mCurrentSortBy);

		mHttpRequest.fetchJsonObjectWithUrl(
				MovieUrl.getDiscoverUrl(mCurrentSortBy, mPageNumber)
		);
	}

	private void makeHttpRequest(final Event event, final RequestType requestType) {
		// Create a new task each request. AsyncTask runs once and terminates.
		mHttpRequest = new HttpRequest(
				new HttpRequest.Delegate() {
					@Override
					public void onPostExecuteComplete(Object object) {
						// Flag this request as completed
						mIsFetchingData = false;

						// Trigger Movie Event
						event.notifyComplete(
								parseJsonData((JSONObject) object, requestType)
						);
					}

					@Override
					public void onError(String message) {
						Log.e(NAME, message);

						mIsFetchingData = false;
						event.notifyError(message);
					}
				}
		);
	}

	private ArrayList<?> parseJsonData(final JSONObject jsonObject, final RequestType type) {
		// List can be of different types, i.e., MovieTrailerVO, MovieItemVO
		ArrayList<?> items = null;

		switch (type) {
			case DISCOVER_FEED:
				items = parseDiscoverJsonData(jsonObject);
				break;

			case TRAILERS:
				items = parseTrailersJsonData(jsonObject);
				break;

			case REVIEWS:
				items = parseReviewsJsonData(jsonObject);
				break;
		}

		return items;
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

	private ArrayList<MovieReviewVO> parseReviewsJsonData(final JSONObject jsonObject) {
		JSONArray results = jsonObject.optJSONArray(MovieVars.RESULTS_KEY);

		ArrayList<MovieReviewVO> reviewItems = new ArrayList<MovieReviewVO>();

		for (int i = 0; i < results.length(); i++) {
			MovieReviewVO item = MovieReviewVO.fromJsonObject(results.optJSONObject(i));
			reviewItems.add(item);
		}

		return reviewItems;
	}

}
