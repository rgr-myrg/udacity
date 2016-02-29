package net.usrlib.android.movies.movieapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import net.usrlib.android.movies.DetailActivity;
import net.usrlib.android.movies.adapter.GridItemAdapter;

import java.util.ArrayList;

public class MovieBrowser {

	private MovieEventDelegate mDelegate;
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

	public MovieBrowser(MovieEventDelegate delegate) {
		mDelegate = delegate;
	}

	public interface MovieEventDelegate {
		public void onMovieFeedLoaded(ArrayList<MovieItemVO> arrayList);
		public void onMovieLimitReached();
	}

	public void initGridView(final Activity activity, final GridView gridView) {
		mGridView = gridView;

		mGridView.setOnItemClickListener(new AbsListView.OnItemClickListener() {
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

	public void getMostPopularMovies(final Activity activity, int title) {
		activity.setTitle(activity.getString(title));
		startNewRequestSortedBy(MovieVars.MOST_POPULAR);
	}

	public void getHighestRatedMovies(final Activity activity, int title) {
		activity.setTitle(activity.getString(title));
		startNewRequestSortedBy(MovieVars.HIGHEST_RATED);
	}

	public void getMovieListFromBundle(Bundle bundle) {
		onMovieFeedLoaded(
				(ArrayList<MovieItemVO>) bundle.get(MovieVars.MOVIE_LIST_KEY)
		);
	}

	public ArrayList<MovieItemVO> getMovieItems() {
		return mGridItemAdapter.getMovieItems();
	}

	private void startNewRequestSortedBy(String sortBy) {
		mCurrentSortBy = sortBy;
		mIsFirstPageRequest = true;

		mMovieApi.fetchFirstPageSortedBy(sortBy);
	}

	private void onMovieFeedLoaded(ArrayList<MovieItemVO> arrayList){
		if (mGridItemAdapter == null || mIsFirstPageRequest) {
			Fragment activityFragment = (Fragment) mDelegate;

			mGridItemAdapter = new GridItemAdapter(activityFragment.getContext(), arrayList);
			mGridView.setAdapter(mGridItemAdapter);
			mIsFirstPageRequest = false;

		} else {
			mGridItemAdapter.updateItemsList(arrayList);
		}

		mDelegate.onMovieFeedLoaded(arrayList);
	}

	private void onMovieLimitReached() {
		mDelegate.onMovieLimitReached();
	}

}
