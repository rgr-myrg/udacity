package net.usrlib.android.movies;

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

import net.usrlib.android.movies.adapter.GridItemAdapter;
import net.usrlib.android.movies.movieapi.MovieApi;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieVars;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

	private GridView mGridView = null;
	private GridItemAdapter mGridItemAdapter = null;
	private String mCurrentSortBy;
	private boolean mIsFirstPageRequest;

	private MovieApi mMovieApi = new MovieApi(
			new MovieApi.Delegate() {
				@Override
				public void onFeedLoaded(ArrayList<MovieItemVO> arrayList) {
					onMovieFeedLoaded(arrayList);
				}

				@Override
				public void onPageLimitReached() {
					onMovieLimitReached();
				}
			}
	);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		// Ensure onOptionsItemSelected is triggered
		setHasOptionsMenu(true);

		// Init and Set Up Grid View
		initGridView(rootView);

		// Start up with Most Popular Movies
		getMostPopularMovies();

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

			case R.id.action_settings:
				// Not implemented Yet
				break;

			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void getMostPopularMovies() {
		getActivity().setTitle(getString(R.string.title_most_popular));
		startNewRequestSortedBy(MovieVars.MOST_POPULAR);
	}

	private void getHighestRatedMovies() {
		getActivity().setTitle(getString(R.string.title_highest_rated));
		startNewRequestSortedBy(MovieVars.HIGHEST_RATED);
	}

	private void startNewRequestSortedBy(String sortBy) {
		mCurrentSortBy = sortBy;
		mIsFirstPageRequest = true;

		mMovieApi.fetchFirstPageSortedBy(sortBy);
	}

	private void initGridView(View rootView) {
		mGridView = (GridView) rootView.findViewById(R.id.grid_view);

		mGridView.setOnItemClickListener(new AbsListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GridItemAdapter gridItemAdapter = (GridItemAdapter) parent.getAdapter();
				MovieItemVO movieItemVO = (MovieItemVO) gridItemAdapter.getItem(position);

				if (movieItemVO == null) {
					return;
				}

				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra(MovieItemVO.NAME, movieItemVO);

				getActivity().startActivity(intent);
			}
		});

		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

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

	private void onMovieFeedLoaded(ArrayList<MovieItemVO> arrayList){
		if (mGridItemAdapter == null || mIsFirstPageRequest) {
			mGridItemAdapter = new GridItemAdapter(getContext(), arrayList);
			mGridView.setAdapter(mGridItemAdapter);
			mIsFirstPageRequest = false;

		} else {
			mGridItemAdapter.updateItemsList(arrayList);
		}
	}

	private void onMovieLimitReached() {

	}

}
