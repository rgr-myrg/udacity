package net.usrlib.android.movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import net.usrlib.android.event.Listener;
import net.usrlib.android.movies.adapter.GridItemAdapter;
import net.usrlib.android.util.SharedPref;
import net.usrlib.android.movies.movieapi.MovieApi;
import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieVars;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

	private MovieApi mMovieApi = new MovieApi();
	private GridView mGridView = null;
	private GridItemAdapter mGridItemAdapter = null;
	private String mCurrentSortBy;

	private boolean mIsFirstPageRequest;
	private boolean mHasEventListeners;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		// Ensure onOptionsItemSelected is triggered
		setHasOptionsMenu(true);

		if (!mHasEventListeners) {
			mHasEventListeners = true;
			// Wire up Event Listeners
			addEventListeners();
		}

		// Init and Set Up Grid View
		initGridView(
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
				mGridItemAdapter.getMovieItems()
		);
	}

	public void addEventListeners() {
		MovieEvent.DiscoverFeedLoaded.addListener(new Listener() {
			@Override
			public void onComplete(Object eventData) {
				onMovieFeedLoaded(
						(ArrayList<MovieItemVO>) eventData
				);
			}

			@Override
			public void onError(Object eventData) {
				// Handle gracefully
			}
		});
	}

	public void initGridView(final GridView gridView) {
		mGridView = gridView;

		mGridView.setOnItemClickListener(new AbsListView.OnItemClickListener() {
			final Activity activity = getActivity();

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GridItemAdapter gridItemAdapter = (GridItemAdapter) parent.getAdapter();
				MovieItemVO movieItemVO = (MovieItemVO) gridItemAdapter.getItem(position);

				if (movieItemVO == null) {
					return;
				}

				Intent intent = new Intent(activity, DetailActivity.class);
				intent.putExtra(MovieItemVO.NAME, movieItemVO);

				activity.startActivity(intent);
			}
		});

		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// onScroll is triggered spuriously while GridView is created.
				// Guard clause: Do nothing when totalItemCount is zero.
				if (totalItemCount == 0) {
					return;
				}

				int lastItemCount = firstVisibleItem + visibleItemCount;

				if (lastItemCount == totalItemCount) {
					mMovieApi.fetchNextPageSortedBy(mCurrentSortBy);
				}
			}
		});
	}

	public void getMostPopularMovies() {
		fetchMoviesAndSetViewTitle(MovieVars.MOST_POPULAR, R.string.title_most_popular);
	}

	public void getHighestRatedMovies() {
		fetchMoviesAndSetViewTitle(MovieVars.HIGHEST_RATED, R.string.title_highest_rated);
	}

	private void getFavoriteMovies() {

	}

	private void getMovieListFromBundle(final Bundle bundle) {
		final Activity activity = getActivity();
		final String viewTitle = SharedPref.getViewTitle(activity);

		activity.setTitle(viewTitle);

		onMovieFeedLoaded(
				(ArrayList<MovieItemVO>) bundle.get(MovieVars.MOVIE_LIST_KEY)
		);
	}

	private void fetchMoviesAndSetViewTitle(final String sortBy, final int resource) {
		mCurrentSortBy = sortBy;
		mIsFirstPageRequest = true;

		final Activity activity = getActivity();
		final String viewTitle = activity.getString(resource);

		activity.setTitle(viewTitle);
		SharedPref.setViewTitle(activity, viewTitle);

		mMovieApi.fetchFirstPageSortedBy(sortBy);
	}

	private void onMovieFeedLoaded(final ArrayList<MovieItemVO> arrayList){
		if (mGridItemAdapter == null || mIsFirstPageRequest) {

			mGridItemAdapter = new GridItemAdapter(getContext(), arrayList);
			mGridView.setAdapter(mGridItemAdapter);
			mIsFirstPageRequest = false;

		} else {
			mGridItemAdapter.updateItemsList(arrayList);
		}
	}

	private void onMovieLimitReached() {
		//MovieEvent.RequestLimitReached.notifyComplete();
	}

}
