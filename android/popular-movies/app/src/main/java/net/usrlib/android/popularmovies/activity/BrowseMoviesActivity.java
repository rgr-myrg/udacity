package net.usrlib.android.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import net.usrlib.android.popularmovies.R;
import net.usrlib.android.popularmovies.adapter.GridViewAdapter;
import net.usrlib.android.popularmovies.movieapi.MovieBrowser;
import net.usrlib.android.popularmovies.movieapi.MovieItemVO;
import net.usrlib.android.popularmovies.movieapi.MovieVars;

import org.json.JSONObject;

import java.util.ArrayList;

public class BrowseMoviesActivity extends AppCompatActivity {

	private MovieBrowser mMovieBrowser;
	private GridView mGridView;
	private GridViewAdapter mGridAdapter;
	private ArrayList<MovieItemVO> mGridData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_movies);
//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
//
//		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
//			}
//		});
		createGridView();
		createMovieBrowser();
	}

	private void createGridView() {
		mGridView = (GridView) findViewById(R.id.grid_view);
		mGridData = new ArrayList<MovieItemVO>();
		mGridAdapter = new GridViewAdapter(this, R.layout.grid_view_item, mGridData);

		mGridView.setAdapter(mGridAdapter);
	}

	private void createMovieBrowser() {
		mMovieBrowser = new MovieBrowser(
				new MovieBrowser.Delegate() {
					@Override
					public void onJsonFeedLoaded(ArrayList<MovieItemVO> arrayList) {
						Log.d("MAIN", ((MovieItemVO) arrayList.get(0)).getImageUrl());

						mGridAdapter.setGridData(arrayList);
					}
				}
		);

		// Start up with Most Popular Movies
		browseMoviesSortedBy(MovieVars.MOST_POPULAR);
	}

	private void browseMoviesSortedBy(String sortBy) {
		mMovieBrowser.loadJsonFeedSortedBy(sortBy);
	}

	private void onMovieFeedLoaded(JSONObject jsonObject) {
		Log.d("MAIN", jsonObject.toString());

	}

}
