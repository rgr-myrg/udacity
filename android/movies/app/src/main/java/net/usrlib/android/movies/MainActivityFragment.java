package net.usrlib.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	private GridView mGridView = null;
	private GridItemAdapter mGridItemAdapter = null;
	int mPreviousVisibleItem = 0;

	private MovieApi mMovieApi = new MovieApi(
			new MovieApi.Delegate() {
				@Override
				public void onFeedLoaded(ArrayList<MovieItemVO> arrayList) {
					onMovieFeedLoaded(arrayList);
				}
			}
	);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		// Init and Set Up Grid View
		initGridView(rootView);

		// Start up with Most Popular Movies
		mMovieApi.fetchNextPageSortedBy(MovieVars.MOST_POPULAR);

		return rootView;
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
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItemCount = firstVisibleItem + visibleItemCount;
//				Log.d("MAIN", "firstVisibleItem: " + String.valueOf(firstVisibleItem));
//				Log.d("MAIN", "visibleItemCount: " + String.valueOf(visibleItemCount));
//				Log.d("MAIN", "totalItemCount: " + String.valueOf(totalItemCount));
//				Log.d("MAIN", "lastItemCount: " + String.valueOf(lastItemCount));
//				Log.d("MAIN", "-------------------------------");

				if (mPreviousVisibleItem < firstVisibleItem) {
					Log.d("MAIN", "Scrolling Down");
				} else if (mPreviousVisibleItem > firstVisibleItem) {
					Log.d("MAIN", "Scrolling Up");
				} else {
					Log.d("MAIN", "NOT Scrolling???");
				}
				//if scrolling Up and item is zero page back. send to previous page.

//				mPreviousVisibleItem = firstVisibleItem;

				if (lastItemCount == totalItemCount) {
					mMovieApi.fetchNextPageSortedBy(MovieVars.MOST_POPULAR);
				} else if (mPreviousVisibleItem > firstVisibleItem && firstVisibleItem == 0) {
					Log.d("MAIN", "PREVIOUS PAGE??? firstVisibleItem:" + String.valueOf(firstVisibleItem));
					//mMovieApi.fetchPreviousPageSortedBy(MovieVars.MOST_POPULAR);
				}
				mPreviousVisibleItem = firstVisibleItem;
			}
		});
	}

	private void onMovieFeedLoaded(ArrayList<MovieItemVO> arrayList){
		Log.d("MAIN", ((MovieItemVO) arrayList.get(0)).getImageUrl());
//		if (mGridItemAdapter == null) {
			mGridItemAdapter = new GridItemAdapter(getContext(), arrayList);
			mGridView.setAdapter(mGridItemAdapter);
//		} else {
//			mGridItemAdapter.updateItemsList(arrayList);
//		}
	}

}
