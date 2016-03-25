package net.usrlib.android.movies.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.usrlib.android.event.Listener;
import net.usrlib.android.movies.BuildConfig;
import net.usrlib.android.movies.DetailActivity;
import net.usrlib.android.movies.R;
import net.usrlib.android.movies.movieapi.MovieDetails;
import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.movieapi.MovieReviews;
import net.usrlib.android.movies.movieapi.MovieTrailers;
import net.usrlib.android.movies.movieapi.MovieVars;
import net.usrlib.android.movies.parcelable.MovieItemVO;
import net.usrlib.android.movies.parcelable.MovieReviewVO;
import net.usrlib.android.movies.parcelable.MovieTrailerVO;
import net.usrlib.android.movies.viewholder.ResourceHolder;
import net.usrlib.android.util.UiViewUtil;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class DetailActivityFragment extends BaseFragment {

	public static final String NAME = DetailActivity.class.getSimpleName();

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
		}

		else if (instanceState == null) {
			addEventListeners();
			parseIntentAndFetchData();

		} else {
			restoreValuesFromBundle(instanceState);
		}

		Intent intent = new Intent();
		intent.putExtra(MovieVars.IS_DETAIL_ACTIVITY, true);

		// Set Result Code and Intent for onActivityResult()
		getActivity().setResult(MovieVars.FAVORITED_RESULT_CODE, intent);

		return mRootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (BuildConfig.DEBUG) Log.d("MAIN", "onSaveInstanceState");
		super.onSaveInstanceState(outState);

		if (mMovieItemVO != null) {
			outState.putParcelable(MovieVars.DETAIL_KEY, mMovieItemVO);
		}

		if (mMovieReviews.getReviews() != null) {
			outState.putParcelableArrayList(MovieVars.REVIEWS_KEY, mMovieReviews.getReviews());
		}

		if (mMovieTrailers.getTrailers() != null) {
			outState.putParcelableArrayList(MovieVars.TRAILERS_KEY, mMovieTrailers.getTrailers());
		}
	}

	private void addEventListeners() {
		MovieEvent.MovieSetAsFavorite.addListener(new Listener() {
			@Override
			public void onComplete(Object eventData) {
				UiViewUtil.displayToastMessage(getActivity(), ResourceHolder.getSavedFavoriteMsg());
			}
		});

		MovieEvent.MovieUnsetAsFavorite.addListener(new Listener() {
			@Override
			public void onComplete(Object eventData) {
				UiViewUtil.displayToastMessage(getActivity(), ResourceHolder.getRemovedFavoriteMsg());
			}
		});
	}

	private void parseBundleAndFetchData() {
		if (BuildConfig.DEBUG) Log.d("MAIN", "parseBundleAndFetchData");
		final Bundle bundle = getArguments();

		if (bundle == null) {
			return;
		}

		mMovieItemVO = (MovieItemVO) bundle.getParcelable(MovieItemVO.NAME);

		if (mMovieItemVO == null) {
			return;
		}

		// Populate Detail Fragment
		mMovieDetails.loadMovieDetail(mMovieItemVO);

		// Fetch Movie Trailers and Reviews as early as possible
		mMovieTrailers.fetchMovieTrailersWithId(mMovieItemVO.getId());
		mMovieReviews.fetchMovieReviewsWithId(mMovieItemVO.getId());
	}

	private void parseIntentAndFetchData() {
		if (BuildConfig.DEBUG) Log.d("MAIN", "parseIntentAndFetchData");
		final Intent intent = getActivity().getIntent();

		if (intent == null || !intent.hasExtra(MovieItemVO.NAME)) {
			return;
		}

		final Bundle data = intent.getExtras();
		mMovieItemVO = (MovieItemVO) data.getParcelable(MovieItemVO.NAME);

		// Populate Detail Fragment
		mMovieDetails.loadMovieDetail(mMovieItemVO);

		// Fetch Movie Trailers and Reviews as early as possible
		mMovieTrailers.fetchMovieTrailersWithId(mMovieItemVO.getId());
		mMovieReviews.fetchMovieReviewsWithId(mMovieItemVO.getId());
	}

	private void restoreValuesFromBundle(final Bundle bundle) {
		if (BuildConfig.DEBUG) Log.d("MAIN", "restoreValuesFromBundle");

		if (bundle.containsKey(MovieVars.DETAIL_KEY)) {
			mMovieItemVO = (MovieItemVO) bundle.get(MovieVars.DETAIL_KEY);
		}

		// Populate Detail Fragment
		mMovieDetails.loadMovieDetail(mMovieItemVO);

		// Populate Trailers
		if (bundle.containsKey(MovieVars.TRAILERS_KEY)) {
			mMovieTrailers.onMovieTrailersLoaded((ArrayList<MovieTrailerVO>) bundle.get(MovieVars.TRAILERS_KEY));
		}

		// Populate Reviews
		if (bundle.containsKey(MovieVars.REVIEWS_KEY)) {
			mMovieReviews.onMovieReviewsLoaded(
					(ArrayList<MovieReviewVO>) bundle.get(MovieVars.REVIEWS_KEY)
			);
		}
	}

}
