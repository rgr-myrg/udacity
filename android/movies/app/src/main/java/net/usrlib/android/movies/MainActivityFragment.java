package net.usrlib.android.movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import net.usrlib.android.event.Listener;
import net.usrlib.android.movies.adapter.GridItemAdapter;
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieVars;

import java.util.ArrayList;

public class MainActivityFragment extends BaseFragment {

	private static final int FAVORITES_REQUEST_CODE = 5;

	private GridView mGridView = null;
	private GridItemAdapter mGridItemAdapter = null;
	private String mCurrentSortBy;
	private String mCurrentTitle;

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
			restoreValuesFromBundle(instanceState);
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

		outState.putString(MovieVars.VIEW_TITLE_KEY, mCurrentTitle);
		outState.putString(MovieVars.SORT_PARAM_KEY, mCurrentSortBy);
		outState.putInt(MovieVars.PAGE_PARAM_KEY, Facade.getMovieApi().getPageNumber());
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

		MovieEvent.RequestLimitReached.addListener(new Listener() {
			@Override
			public void onComplete(Object eventData) {
				onMovieLimitReached();
			}
		});

		MovieEvent.ActivityResultReady.addListener(new Listener() {
			@Override
			public void onComplete(Object eventData) {
				onActivityResultReady((int) eventData);
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

				//activity.startActivity(intent);
				// Use a request code to trigger onActivityResult
				activity.startActivityForResult(intent, FAVORITES_REQUEST_CODE);
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

				if (lastItemCount == totalItemCount && !mCurrentTitle.contentEquals("Favorites")) {
					Facade.getMovieApi().fetchNextPageSortedBy(mCurrentSortBy);
				}
			}
		});
	}

	public void getMostPopularMovies() {
		fetchMoviesAndSetValues(MovieVars.MOST_POPULAR, R.string.title_most_popular);
	}

	public void getHighestRatedMovies() {
		fetchMoviesAndSetValues(MovieVars.HIGHEST_RATED, R.string.title_highest_rated);
	}

	private void getFavoriteMovies() {
		mIsFirstPageRequest = true;
		setViewTitle(getActivity().getString(R.string.title_favorites));

		onMovieFeedLoaded(
			Facade.getMoviesDBHelper().selectFromFavorites()
		);
	}

	private void restoreValuesFromBundle(final Bundle bundle) {
		final String viewTitle = bundle.getString(MovieVars.VIEW_TITLE_KEY);

		if (viewTitle != null) {
			setViewTitle(viewTitle);
		}

		// Restore last sortBy value for the next fetchNextPageSortedBy() request
		mCurrentSortBy = bundle.getString(MovieVars.SORT_PARAM_KEY);

		// Restore last page to continue browsing where we left off
		Facade.getMovieApi().setPageNumber(bundle.getInt(MovieVars.PAGE_PARAM_KEY));

		onMovieFeedLoaded(
				(ArrayList<MovieItemVO>) bundle.get(MovieVars.MOVIE_LIST_KEY)
		);

	}

	private void fetchMoviesAndSetValues(final String sortBy, final int resource) {
		mCurrentSortBy = sortBy;
		mIsFirstPageRequest = true;

		setViewTitle(getActivity().getString(resource));

		Facade.getMovieApi().fetchFirstPageSortedBy(sortBy);
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

	private void setViewTitle(String viewTitle) {
		getActivity().setTitle(viewTitle);
		mCurrentTitle = viewTitle;
	}

	private void onMovieLimitReached() {
		// Toast Friendly Message to UI
	}

	private void onActivityResultReady(int resultCode) {
		// Triggered by MainActivity.onActivityResult
		// If there was a change to Favorites get a fresh list of movies
		if (resultCode == MovieVars.FAVORITED_RESULT_CODE
				&& mCurrentTitle.contentEquals(
				getActivity().getString(R.string.title_favorites))
				) {
			getFavoriteMovies();
		}

	}
}
