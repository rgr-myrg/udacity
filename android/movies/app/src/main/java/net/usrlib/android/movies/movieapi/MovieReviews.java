package net.usrlib.android.movies.movieapi;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.usrlib.android.event.Listener;
import net.usrlib.android.movies.R;
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.fragment.BaseFragment;
import net.usrlib.android.movies.parcelable.MovieReviewVO;
import net.usrlib.android.movies.viewholder.ReviewViewHolder;
import net.usrlib.android.util.ColorUtil;
import net.usrlib.android.util.HttpRequest;
import net.usrlib.android.util.UiViewUtil;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class MovieReviews {

	public static final String NAME = MovieReviews.class.getSimpleName();

	private BaseFragment mFragment = null;
	private ViewGroup mReviewsContainer = null;
	private ArrayList<MovieReviewVO> mMovieReviewVOs = null;

	public MovieReviews(BaseFragment fragment) {
		mFragment = fragment;

		MovieEvent.MovieReviewsLoaded.addListener(
				new Listener() {
					@Override
					public void onComplete(Object eventData) {
						onMovieReviewsLoaded((ArrayList<MovieReviewVO>) eventData);
					}

					@Override
					public void onError(Object eventData) {
						onMovieReviewsError();
					}
				}
		);
	}

	public final void onMovieReviewsLoaded(final ArrayList<MovieReviewVO> movieReviews) {
		if (movieReviews == null || movieReviews.size() == 0 || mFragment.getActivity() == null) {
			UiViewUtil.setText(mFragment.getView(), R.id.movie_reviews_label, MovieVars.NO_REVIEWS_MSG);

			return;
		}

		mMovieReviewVOs = movieReviews;

		if (mReviewsContainer == null) {
			mReviewsContainer = (ViewGroup) mFragment.getRootView().findViewById(R.id.movie_reviews_container);
		}

		final LayoutInflater inflater = LayoutInflater.from(mFragment.getActivity());

		for (final MovieReviewVO movieReviewVO : mMovieReviewVOs) {
			final View reviewView = inflater.inflate(R.layout.item_review, mReviewsContainer, false);
			final ReviewViewHolder reviewViewHolder = new ReviewViewHolder(reviewView);

			reviewView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse(
									movieReviewVO.getUrl()
							)
					);

					mFragment.startActivity(intent);
				}
			});

			final TextView circleView = reviewViewHolder.circleView;
			final TextView authorView = reviewViewHolder.authorView;
			final TextView contentView = reviewViewHolder.contentView;

			circleView.setText(String.valueOf(movieReviewVO.getAuthor().charAt(0)).toUpperCase());
			circleView.getBackground().setColorFilter(
					Color.parseColor(ColorUtil.getNextColor()),
					PorterDuff.Mode.SRC
			);

			authorView.setText(movieReviewVO.getAuthor());

			final String content = movieReviewVO.getContent();
			int previewLength = MovieVars.CONTENT_PREVIEW_LENGTH;

			// Try to display more preview text depending on device orientation
			if (mFragment.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				previewLength = previewLength * 2;
			}

			contentView.setText(
					content.length() > previewLength
							? content.substring(0, previewLength) + MovieVars.DOTTED
							: content
			);

			mReviewsContainer.addView(reviewView);
		}
	}

	public final ArrayList<MovieReviewVO> getReviews() {
		return mMovieReviewVOs;
	}

	public final void fetchMovieReviewsWithId(int movieId) {
		Facade.getMovieApi().fetchMovieReviewsWithId(movieId);
	}

	public final void onMovieReviewsError() {
		UiViewUtil.displayToastMessage(mFragment.getActivity(), HttpRequest.CONNECTIVY_ERROR);
		UiViewUtil.setText(mFragment.getView(), R.id.movie_reviews_label, MovieVars.NO_REVIEWS_MSG);
	}

}
