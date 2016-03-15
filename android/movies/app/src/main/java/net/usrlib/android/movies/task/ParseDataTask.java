package net.usrlib.android.movies.task;

import android.os.AsyncTask;

import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieReviewVO;
import net.usrlib.android.movies.movieapi.MovieTrailerVO;
import net.usrlib.android.movies.movieapi.MovieVars;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseDataTask {

	public static void parseMoviesFromJson(final JSONObject jsonObject) {
		new AsyncTask<Void, Void, ArrayList<MovieItemVO>>() {

			@Override
			protected ArrayList<MovieItemVO> doInBackground(Void... params) {
				JSONArray results = jsonObject.optJSONArray(MovieVars.RESULTS_KEY);
				ArrayList<MovieItemVO> movieItems = new ArrayList<MovieItemVO>();

				for (int i = 0; i < results.length(); i++) {
					MovieItemVO item = MovieItemVO.fromJsonObject(results.optJSONObject(i));
					movieItems.add(item);
				}

				return movieItems;
			}

			@Override
			protected void onPostExecute(ArrayList<MovieItemVO> movieItemVOs) {
				// Notify Observers Discover Feed was Loaded
				MovieEvent.DiscoverFeedLoaded.notifyComplete(movieItemVOs);
			}
		}.execute();
	}

	public static void parseTrailersFromJson(final JSONObject jsonObject) {
		new AsyncTask<Void, Void, ArrayList<MovieTrailerVO>>() {

			@Override
			protected ArrayList<MovieTrailerVO> doInBackground(Void... params) {
				JSONArray results = jsonObject.optJSONArray(MovieVars.RESULTS_KEY);
				ArrayList<MovieTrailerVO> trailerItems = new ArrayList<MovieTrailerVO>();

				for (int i = 0; i < results.length(); i++) {
					MovieTrailerVO item = MovieTrailerVO.fromJsonObject(results.optJSONObject(i));
					trailerItems.add(item);
				}

				return trailerItems;
			}

			@Override
			protected void onPostExecute(ArrayList<MovieTrailerVO> movieTrailerVOs) {
				// Notify Observers Trailers were Loaded
				MovieEvent.MovieTrailersLoaded.notifyComplete(movieTrailerVOs);
			}
		}.execute();
	}

	public static void parseReviewsFromJson(final JSONObject jsonObject) {
		new AsyncTask<Void, Void, ArrayList<MovieReviewVO>>() {

			@Override
			protected ArrayList<MovieReviewVO> doInBackground(Void... params) {
				JSONArray results = jsonObject.optJSONArray(MovieVars.RESULTS_KEY);
				ArrayList<MovieReviewVO> reviewItems = new ArrayList<MovieReviewVO>();

				for (int i = 0; i < results.length(); i++) {
					MovieReviewVO item = MovieReviewVO.fromJsonObject(results.optJSONObject(i));
					reviewItems.add(item);
				}

				return reviewItems;
			}

			@Override
			protected void onPostExecute(ArrayList<MovieReviewVO> movieReviewVOs) {
				// Notify Observers Reviews were Loaded
				MovieEvent.MovieReviewsLoaded.notifyComplete(movieReviewVOs);
			}

		}.execute();
	}

}
