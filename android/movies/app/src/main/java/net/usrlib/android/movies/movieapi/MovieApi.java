package net.usrlib.android.movies.movieapi;

import android.util.Log;

import net.usrlib.android.movies.data.MoviesDBHelper;
import net.usrlib.android.util.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieApi {

	public static final String NAME = MovieApi.class.getSimpleName();

	private MoviesDBHelper moviesDBHelper = null;
	private HttpRequest mHttpRequest;
	private String mCurrentSortBy = "";

	private int mPageNumber = 0;
	private boolean mIsFetchingData;

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

	public void setPageNumber(int pageNumber) {
		mPageNumber = pageNumber;
	}

	public int getPageNumber() {
		return mPageNumber;
	}

	public void fetchMovieTrailersWithId(int id) {
		mIsFetchingData = true;

		mHttpRequest = new HttpRequest(new HttpRequest.Delegate() {
			@Override
			public void onPostExecuteComplete(Object object) {
				mIsFetchingData = false;

				ArrayList<MovieTrailerVO> trailerItems = parseTrailersJsonData((JSONObject) object);
				MovieEvent.MovieTrailersLoaded.notifyComplete(trailerItems);
			}

			@Override
			public void onError(String message) {
				mIsFetchingData = false;

				MovieEvent.MovieTrailersLoaded.notifyError(message);
			}
		});

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

		// Create a new task each request. AsyncTask only runs once.
		startHttpRequest();
	}

	private void startHttpRequest() {
		mHttpRequest = new HttpRequest(
				new HttpRequest.Delegate() {
					@Override
					public void onPostExecuteComplete(Object object) {
						ArrayList<MovieItemVO> arrayList = parseDiscoverJsonData((JSONObject) object);

						// Trigger DiscoverFeedLoaded Event
						MovieEvent.DiscoverFeedLoaded.notifyComplete(arrayList);

						// Flag this request as completed
						mIsFetchingData = false;
					}

					@Override
					public void onError(String message) {
						Log.e(NAME, message);

						// Trigger DiscoverFeedLoaded Event Error
						MovieEvent.DiscoverFeedLoaded.notifyError(message);

						mIsFetchingData = false;
					}
				}
		);

		Log.d(NAME, "Fetching page: " + mPageNumber + " sort by: " + mCurrentSortBy);

		mHttpRequest.fetchJsonObjectWithUrl(
				MovieUrl.getDiscoverUrl(mCurrentSortBy, mPageNumber)
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
			Log.d(NAME, "site: " + item.getSite() + " : " + item.getYoutubeUrl());
		}

		return trailerItems;
	}

}
