package net.usrlib.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.usrlib.android.popularmovies.movieapi.MovieUrl;
import net.usrlib.android.popularmovies.movieapi.MovieVars;
import net.usrlib.android.popularmovies.util.HttpRequest;

import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		browseMovies();
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	private void browseMovies() {
		HttpRequest httpRequest = new HttpRequest(new HttpRequest.Delegate() {
			@Override
			public void onPostExecuteComplete(Object object) {
				JSONObject jsonObject = (JSONObject) object;
			}

			@Override
			public void onError(String message) {

			}
		});

		httpRequest.fetchJsonObjectWithUrl(
				MovieUrl.getUrl(MovieVars.MOST_POPULAR, 1)
		);
	}
}
