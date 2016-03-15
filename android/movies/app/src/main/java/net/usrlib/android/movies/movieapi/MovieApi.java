package net.usrlib.android.movies.movieapi;

import android.util.Log;

import net.usrlib.android.event.Event;
import net.usrlib.android.movies.task.ParseDataTask;
import net.usrlib.android.util.HttpRequest;

import org.json.JSONObject;

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
						final JSONObject jsonObject = (JSONObject) object;

						switch (requestType) {
							case DISCOVER_FEED:
								ParseDataTask.parseMoviesFromJson(jsonObject);
								break;

							case TRAILERS:
								ParseDataTask.parseTrailersFromJson(jsonObject);
								break;

							case REVIEWS:
								ParseDataTask.parseReviewsFromJson(jsonObject);
								break;
						}
					}

					@Override
					public void onError(String message) {
						Log.e(NAME, message);

						mIsFetchingData = false;
						// Notify Observers an Error occurred.
						event.notifyError(message);
					}
				}
		);
	}

}
