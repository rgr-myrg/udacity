package net.usrlib.android.movies.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import net.usrlib.android.event.Listener;
import net.usrlib.android.movies.BuildConfig;
import net.usrlib.android.movies.DetailActivity;
import net.usrlib.android.movies.R;
import net.usrlib.android.movies.adapter.GridItemAdapter;
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieVars;
import net.usrlib.android.movies.viewholder.ResourceHolder;
import net.usrlib.android.util.HttpRequest;
import net.usrlib.android.util.UiViewUtil;

import java.util.ArrayList;

public class MainActivityFragment extends BaseFragment {

	public static final String NAME = MainActivityFragment.class.getSimpleName();

	private static final int ITEM_SCROLL_BUFFER = 10;
	private static final int FAVORITES_REQUEST_CODE = 5;

	private View mRootView = null;
	private GridView mGridView = null;
	private GridItemAdapter mGridItemAdapter = null;
	private String mCurrentSortBy;
	private String mCurrentTitle;

	private boolean mIsFirstPageRequest;
	private boolean mHasEventListeners;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Investigating an issue where the context is null on the fragment.
		// Save the context to debug later. Not intended for production.
		Facade.setAppContext(getContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState) {
		mRootView = inflater.inflate(R.layout.fragment_main, container, false);

		// Ensure onOptionsItemSelected is triggered
		setHasOptionsMenu(true);

		// Init and Set Up Grid View
		initGridView(
				(GridView) mRootView.findViewById(R.id.movie_grid_view)
		);

		if (!mHasEventListeners) {
			mHasEventListeners = true;
			addEventListeners();
		}

		if (instanceState == null) {
			// Start up with Most Popular Movies
			getMostPopularMovies();
		} else if (instanceState.containsKey(MovieVars.MOVIE_LIST_KEY)) {
			// Restore Movie List
			restoreValuesFromBundle(instanceState);
		}

		return mRootView;
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

		if (BuildConfig.DEBUG) Log.d(NAME, "onSaveInstanceState");

		if (mGridItemAdapter != null) {
			outState.putParcelableArrayList(
					MovieVars.MOVIE_LIST_KEY,
					mGridItemAdapter.getMovieItems()
			);
		}

		outState.putString(MovieVars.VIEW_TITLE_KEY, mCurrentTitle);
		outState.putString(MovieVars.SORT_PARAM_KEY, mCurrentSortBy);
		outState.putInt(MovieVars.PAGE_PARAM_KEY, Facade.getMovieApi().getPageNumber());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (BuildConfig.DEBUG) Log.d(NAME, "onActivityResult mCurrentTitle: " + mCurrentTitle);

		// Determine if Favorites should be refreshed.
		if (data != null
				&& data.hasExtra(MovieVars.IS_DETAIL_ACTIVITY)
				&& data.getBooleanExtra(MovieVars.IS_DETAIL_ACTIVITY, false)
				&& mCurrentTitle != null
				&& mCurrentTitle == ResourceHolder.getTitleFavorites())  {

			getFavoriteMovies();
		}
	}

	private void addEventListeners() {
		if (BuildConfig.DEBUG){
			Log.d(NAME, "addEventListeners mHasEventListeners: "
					+ String.valueOf(mHasEventListeners));
		}

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
				onMovieFeedError();
			}
		});

		MovieEvent.RequestLimitReached.addListener(new Listener() {
			@Override
			public void onComplete(Object eventData) {
				onMovieLimitReached();
			}
		});

		mHasEventListeners = true;
	}

	private void initGridView(final GridView gridView) {
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

				// Use a request code to trigger onActivityResult on MainActivity
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
				// GridView seems to auto scroll a bit and jitter on some devices.
				// Guard clause: Do nothing when totalItemCount is zero.
				if (totalItemCount == 0) {
					return;
				}

				int lastItemCount = firstVisibleItem + visibleItemCount;

				if (lastItemCount > totalItemCount - visibleItemCount
				//if (firstVisibleItem > totalItemCount - ITEM_SCROLL_BUFFER
						&& !mCurrentTitle.contentEquals(ResourceHolder.getTitleFavorites())) {

					if (BuildConfig.DEBUG) Log.d(NAME, "onScroll invoking fetchNextPageSortedBy");
					Facade.getMovieApi().fetchNextPageSortedBy(mCurrentSortBy);
				}
			}
		});
	}

	private void getMostPopularMovies() {
		fetchMovieFeed(MovieVars.MOST_POPULAR, ResourceHolder.getTitleMostPopular());
	}

	private void getHighestRatedMovies() {
		fetchMovieFeed(MovieVars.HIGHEST_RATED, ResourceHolder.getTitleHighestRated());
	}

	private void getFavoriteMovies() {
		mIsFirstPageRequest = true;

		setActivityTitle(ResourceHolder.getTitleFavorites());

		onMovieFeedLoaded(
				Facade.getMoviesDBHelper().selectFromFavorites()
		);
	}

	private void restoreValuesFromBundle(final Bundle bundle) {
		if (BuildConfig.DEBUG) Log.d(NAME, "restoreValuesFromBundle");

		final String viewTitle = bundle.getString(MovieVars.VIEW_TITLE_KEY);

		if (viewTitle != null) {
			setActivityTitle(viewTitle);
		}

		// Restore last sortBy value for the next fetchNextPageSortedBy() request
		mCurrentSortBy = bundle.getString(MovieVars.SORT_PARAM_KEY);

		// Restore last page to continue browsing where we left off
		Facade.getMovieApi().setPageNumber(bundle.getInt(MovieVars.PAGE_PARAM_KEY));

		onMovieFeedLoaded(
				(ArrayList<MovieItemVO>) bundle.get(MovieVars.MOVIE_LIST_KEY)
		);

	}

	private void fetchMovieFeed(final String sortBy, final String title) {
		if (BuildConfig.DEBUG) Log.d(NAME, "fetchMovieFeed sortBy: " + sortBy);

		mCurrentSortBy = sortBy;
		mIsFirstPageRequest = true;

		//addEventListeners();
		setActivityTitle(title);

		Facade.getMovieApi().fetchFirstPageSortedBy(sortBy);
	}

	private void onMovieFeedLoaded(final ArrayList<MovieItemVO> arrayList) {
		Context context = getContext();

		if (BuildConfig.DEBUG) {
			Log.d(NAME, "onMovieFeedLoaded arrayList size: "
							+ String.valueOf(arrayList.size()) + "\n"
							+ "mIsFirstPageRequest: "
							+ String.valueOf(mIsFirstPageRequest) + "\n"
			);

			if (context == null) {
				Log.d(NAME, "Context is null. Facade: " + Facade.getAppContext().toString());
			}

			if (mGridItemAdapter == null) {
				Log.d(NAME, "mGridItemAdapter is null.");
			}
		}

		if (mGridItemAdapter == null || mIsFirstPageRequest) {
			mGridItemAdapter = new GridItemAdapter(
					context != null ? context : Facade.getAppContext(),
					arrayList
			);

			mGridView.setAdapter(mGridItemAdapter);
			mIsFirstPageRequest = false;

		} else {
			mGridItemAdapter.updateItemsList(arrayList);

			//mGridItemAdapter.addAll(arrayList);
			//mGridItemAdapter.notifyDataSetChanged();
		}

		// Movie Feed was Loaded. Hide "Connect to the Internet" Message.
		//UiViewUtil.setViewAsInvisible(getActivity(), mRootView.findViewById(R.id.user_message_box));
	}

	private void setActivityTitle(String viewTitle) {
		final Activity activity = getActivity();

		if (activity != null) {
			getActivity().setTitle(viewTitle);
		}

		// Clear items before loading new items
		if (mGridItemAdapter != null) {
			mGridItemAdapter.clear();
		}

		mCurrentTitle = viewTitle;
	}

	private void onMovieLimitReached() {
		// Toast Friendly Message to UI
		UiViewUtil.displayToastMessage(getActivity(), MovieVars.LIMIT_REACHED_MSG);
	}

	private void onMovieFeedError() {
		UiViewUtil.displayToastMessage(getActivity(), HttpRequest.CONNECTIVY_ERROR);
		UiViewUtil.setViewAsVisible(getActivity(), mRootView.findViewById(R.id.user_message_box));
	}

}
