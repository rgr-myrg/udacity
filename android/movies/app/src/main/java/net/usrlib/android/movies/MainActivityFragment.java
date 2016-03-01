package net.usrlib.android.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import net.usrlib.android.movies.movieapi.MovieBrowser;
import net.usrlib.android.movies.movieapi.MovieBrowser.MovieEventDelegate;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieVars;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment implements MovieEventDelegate {

	private MovieBrowser mMovieBrowser = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		// Ensure onOptionsItemSelected is triggered
		setHasOptionsMenu(true);

		// Create only one MovieBrowser instance
		if (mMovieBrowser == null) {
			mMovieBrowser = new MovieBrowser(this);
		}

		// Init and Set Up Grid View
		mMovieBrowser.initGridView(
				getActivity(),
				(GridView) rootView.findViewById(R.id.grid_view)
		);

		if (instanceState == null) {
			// Start up with Most Popular Movies
			getMostPopularMovies();
		} else if (instanceState.containsKey(MovieVars.MOVIE_LIST_KEY)) {
			// Restore Movie List
			getMovieListFromBundle(instanceState);
		}

		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();

		switch (itemId) {
			case R.id.action_most_popular:
				getMostPopularMovies();
				break;

			case R.id.action_highest_rated:
				getHighestRatedMovies();
				break;

			case R.id.action_favorites:
				getFavoriteMovies();
				break;

			case R.id.action_settings:
				// Not implemented Yet
				break;

			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelableArrayList(
				MovieVars.MOVIE_LIST_KEY,
				mMovieBrowser.getMovieItems()
		);
	}

	private void getMostPopularMovies() {
		mMovieBrowser.getMostPopularMovies(getActivity(), R.string.title_most_popular);
	}

	private void getHighestRatedMovies() {
		mMovieBrowser.getHighestRatedMovies(getActivity(), R.string.title_highest_rated);
	}

	private void getFavoriteMovies() {

	}

	private void getMovieListFromBundle(Bundle bundle) {
		mMovieBrowser.getMovieListFromBundle(bundle);
	}

	@Override
	public void onMovieFeedLoaded(ArrayList<MovieItemVO> arrayList) {

	}

	@Override
	public void onMovieLimitReached() {

	}

}
