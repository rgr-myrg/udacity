package net.usrlib.android.movies.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.usrlib.android.movies.BuildConfig;
import net.usrlib.android.movies.R;
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.movieapi.MovieDetails;
import net.usrlib.android.movies.movieapi.MovieReviews;
import net.usrlib.android.movies.movieapi.MovieTrailers;
import net.usrlib.android.movies.movieapi.MovieVars;
import net.usrlib.android.movies.parcelable.MovieItemVO;
import net.usrlib.android.movies.parcelable.MovieReviewVO;
import net.usrlib.android.movies.parcelable.MovieTrailerVO;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class DetailActivityFragment extends BaseFragment {

	public static final String NAME = DetailActivityFragment.class.getSimpleName();

	private MovieDetails mMovieDetails = null;
	private MovieTrailers mMovieTrailers = null;
	private MovieReviews mMovieReviews = null;
	private MovieItemVO mMovieItemVO = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle instanceState) {
		mRootView = inflater.inflate(R.layout.fragment_detail, container, false);

		if (mMovieDetails == null) {
			mMovieDetails = new MovieDetails(this);
		}

		if (mMovieTrailers == null) {
			mMovieTrailers = new MovieTrailers(this);
		}

		if (mMovieReviews == null) {
			mMovieReviews = new MovieReviews(this);
		}

		if (getArguments() != null) {
			parseBundleAndFetchData();

		} else if (instanceState == null) {
			parseIntentAndFetchData();

		} else {
			restoreMovieItemFromBundle(instanceState);
		}

		Intent intent = new Intent();
		intent.putExtra(MovieVars.IS_DETAIL_ACTIVITY, true);

		// Set Result Code and Intent for onActivityResult()
		getActivity().setResult(MovieVars.FAVORITED_RESULT_CODE, intent);

		return mRootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) Log.d(NAME, "onSaveInstanceState");

		if (mMovieItemVO != null) {
			outState.putParcelable(MovieItemVO.NAME, mMovieItemVO);
		}

		if (mMovieReviews.getReviews() != null) {
			outState.putParcelableArrayList(MovieReviews.NAME, mMovieReviews.getReviews());
		}

		if (mMovieTrailers.getTrailers() != null) {
			outState.putParcelableArrayList(MovieTrailers.NAME, mMovieTrailers.getTrailers());
		}
	}

	private void parseBundleAndFetchData() {
		if (BuildConfig.DEBUG) Log.d(NAME, "parseBundleAndFetchData");

		final Bundle bundle = getArguments();

		if (bundle == null) {
			return;
		}

		loadMovieItemFromBundle(bundle);
	}

	private void parseIntentAndFetchData() {
		if (BuildConfig.DEBUG) Log.d(NAME, "parseIntentAndFetchData");

		final Intent intent = getActivity().getIntent();

		if (intent == null || !intent.hasExtra(MovieItemVO.NAME)) {
			return;
		}

		final Bundle bundle = intent.getExtras();

		loadMovieItemFromBundle(bundle);
	}

	private void loadMovieItemFromBundle(final Bundle bundle) {
		mMovieItemVO = (MovieItemVO) bundle.getParcelable(MovieItemVO.NAME);

		// Unhide detail layout only if we have data
		if(mMovieItemVO != null) {
			displayDetailLayout();
		}

		// Populate Detail Fragment
		mMovieDetails.loadMovieDetail(mMovieItemVO);

		// Fetch Movie Trailers and Reviews as early as possible
		mMovieTrailers.fetchMovieTrailersWithId(mMovieItemVO.getId());
		mMovieReviews.fetchMovieReviewsWithId(mMovieItemVO.getId());
	}

	private void restoreMovieItemFromBundle(final Bundle bundle) {
		if (BuildConfig.DEBUG) Log.d(NAME, "restoreMovieItemFromBundle");

		if (bundle.containsKey(MovieItemVO.NAME)) {
			mMovieItemVO = (MovieItemVO) bundle.get(MovieItemVO.NAME);
		}

		// Unhide detail layout only if we have data
		if(mMovieItemVO != null) {
			displayDetailLayout();
		}

		// Populate Detail Fragment
		mMovieDetails.loadMovieDetail(mMovieItemVO);

		// Populate Trailers
		if (bundle.containsKey(MovieTrailers.NAME)) {
			mMovieTrailers.onMovieTrailersLoaded(
					(ArrayList<MovieTrailerVO>) bundle.get(MovieTrailers.NAME)
			);
		}

		// Populate Reviews
		if (bundle.containsKey(MovieReviews.NAME)) {
			mMovieReviews.onMovieReviewsLoaded(
					(ArrayList<MovieReviewVO>) bundle.get(MovieReviews.NAME)
			);
		}
	}

	private void displayDetailLayout() {
		if (Facade.isTablet()) {
			((LinearLayout) mRootView.findViewById(R.id.fragment_detail_layout))
					.setVisibility(View.VISIBLE);
			hideDefaultMessage();
		}
	}

	private void hideDefaultMessage() {
		((TextView) getActivity()
				.findViewById(R.id.detail_container_default_message))
				.setVisibility(View.INVISIBLE);
	}
}
