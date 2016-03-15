package net.usrlib.android.movies.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.android.event.Listener;
import net.usrlib.android.movies.DetailActivity;
import net.usrlib.android.movies.R;
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.fragment.BaseFragment;
import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieReviewVO;
import net.usrlib.android.movies.movieapi.MovieTrailerVO;
import net.usrlib.android.movies.movieapi.MovieVars;
import net.usrlib.android.util.ColorUtil;
import net.usrlib.android.util.HttpRequest;
import net.usrlib.android.util.UiViewUtil;

import java.util.ArrayList;

public class DetailActivityFragment extends BaseFragment {
	public static final String NAME = DetailActivity.class.getSimpleName();

	private View mRootView = null;
	private ViewGroup mTrailersContainer = null;
	private ViewGroup mReviewsContainer = null;

	private MovieItemVO mMovieItemVO = null;

	private ArrayList<MovieTrailerVO> mMovieTrailers = null;
	private ArrayList<MovieReviewVO> mMovieReviews = null;

	private Listener mMovieTrailersListener = new Listener() {
		@Override
		public void onComplete(Object eventData) {
			onMovieTrailersLoaded((ArrayList<MovieTrailerVO>) eventData);
		}

		@Override
		public void onError(Object eventData) {
			onFeedError((String) eventData);
		}
	};

	private Listener mMovieReviewsListener = new Listener() {
		@Override
		public void onComplete(Object eventData) {
			onMovieReviewsLoaded((ArrayList<MovieReviewVO>) eventData);
		}

		@Override
		public void onError(Object eventData) {
			onFeedError((String) eventData);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle instanceState) {
		mRootView = inflater.inflate(R.layout.fragment_detail, container, false);

		if (instanceState == null) {
			addEventListeners();
			parseIntentAndFetchData();

		} else {
			restoreValuesFromBundle(instanceState);
		}

		return mRootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mMovieItemVO != null) {
			outState.putParcelable(MovieVars.DETAIL_KEY, mMovieItemVO);
		}

		if (mMovieReviews != null) {
			outState.putParcelableArrayList(MovieVars.REVIEWS_KEY, mMovieReviews);
		}

		if (mMovieTrailers != null) {
			outState.putParcelableArrayList(MovieVars.TRAILERS_KEY, mMovieTrailers);
		}
	}

	private void addEventListeners() {
		MovieEvent.MovieTrailersLoaded.addListenerOnce(mMovieTrailersListener);
		MovieEvent.MovieReviewsLoaded.addListenerOnce(mMovieReviewsListener);
	}

	private void parseIntentAndFetchData() {
		final Intent intent = getActivity().getIntent();

		if (intent == null || !intent.hasExtra(MovieItemVO.NAME)) {
			return;
		}

		final Bundle data = intent.getExtras();
		mMovieItemVO = (MovieItemVO) data.getParcelable(MovieItemVO.NAME);

		// Populate Detail Fragment
		loadMovieDetail();

		// Fetch Movie Trailers and Reviews as early as possible
		Facade.getMovieApi().fetchMovieTrailersWithId(mMovieItemVO.getId());
		Facade.getMovieApi().fetchMovieReviewsWithId(mMovieItemVO.getId());
	}

	private void restoreValuesFromBundle(final Bundle bundle) {
		if (bundle.containsKey(MovieVars.DETAIL_KEY)) {
			mMovieItemVO = (MovieItemVO) bundle.get(MovieVars.DETAIL_KEY);
		}

		if (bundle.containsKey(MovieVars.REVIEWS_KEY)) {
			mMovieReviews = (ArrayList<MovieReviewVO>) bundle.get(MovieVars.REVIEWS_KEY);
		}

		if (bundle.containsKey(MovieVars.TRAILERS_KEY)) {
			mMovieTrailers = (ArrayList<MovieTrailerVO>) bundle.get(MovieVars.TRAILERS_KEY);
		}

		// Populate Detail Fragment
		loadMovieDetail();

		// Populate Trailers and Reviews
		onMovieTrailersLoaded(mMovieTrailers);
		onMovieReviewsLoaded(mMovieReviews);
	}

	private void loadMovieDetail() {
		if (mMovieItemVO == null) {
			UiViewUtil.setText(
					mRootView,
					R.id.movie_title,
					MovieVars.NO_MOVIES_MSG
			);

			return;
		}

		final ImageView posterImageView = (ImageView) mRootView.findViewById(R.id.movie_poster);
		final ImageView favBtnImageView = (ImageView) mRootView.findViewById(R.id.button_favorite);

		// Invoking placeholder causes the image to misalign. >:(
		Glide.with(getActivity())
				.load(mMovieItemVO.getImageUrl())
						//.placeholder(R.drawable.image_poster_placeholder)
						//.error(R.drawable.image_poster_placeholder)
						//.crossFade()
				.fitCenter()
				.into(posterImageView);

		int resourceId = R.drawable.heart_unselected;

		if (Facade.getMoviesDBHelper().isMovieSetAsFavorite(mMovieItemVO.getId())) {
			resourceId = R.drawable.heart_selected;
		}

		favBtnImageView.setImageResource(resourceId);

		// Set Tag to help toggle favorites icon on/off in setMovieIfLiked()
		favBtnImageView.setTag(resourceId);

		favBtnImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMovieIfLiked(mMovieItemVO, (ImageView) v);
			}
		});

		UiViewUtil.setText(
				mRootView,
				R.id.movie_title,
				mMovieItemVO.getOriginalTitle()
		);

		UiViewUtil.setText(
				mRootView,
				R.id.movie_release_date,
				mMovieItemVO.getReleaseDate()
		);

		UiViewUtil.setText(
				mRootView,
				R.id.movie_rating,
				String.valueOf(mMovieItemVO.getVoteAverage())
		);

		UiViewUtil.setText(
				mRootView,
				R.id.movie_overview,
				mMovieItemVO.getOverview()
		);
	}

	private void setMovieIfLiked(final MovieItemVO movieItemVO, final ImageView imageView) {
		final boolean hasSelected = (Integer) imageView.getTag() != R.drawable.heart_selected;

		final int imageResource = hasSelected
		 		? R.drawable.heart_selected
				: R.drawable.heart_unselected;

		Facade.getMoviesDBHelper().setMovieAsFavorite(movieItemVO.toContentValues(), hasSelected);

		imageView.setImageResource(imageResource);
		imageView.setTag(imageResource);

		Intent intent = new Intent();
		intent.putExtra(MovieVars.IS_FAVORITED_KEY, true);

		// Set Result Code and Intent for onActivityResult()
		getActivity().setResult(MovieVars.FAVORITED_RESULT_CODE, intent);
	}

	private void onMovieTrailersLoaded(final ArrayList<MovieTrailerVO> movieTrailers) {
		if (movieTrailers == null || movieTrailers.size() == 0) {
			UiViewUtil.setText(getView(), R.id.movie_trailers_label, MovieVars.NO_TRAILERS_MSG);

			return;
		}

		mMovieTrailers = movieTrailers;

		if (mTrailersContainer == null) {
			mTrailersContainer = (ViewGroup) mRootView.findViewById(R.id.movie_trailers_container);
		}

		// Set up Share Intent with the first Trailer item
		setShareButtonOnClickListener(mMovieTrailers.get(0));

		final LayoutInflater inflater = LayoutInflater.from(getActivity());

		for (final MovieTrailerVO trailerVO : movieTrailers) {
			Log.d("MAIN", "onMovieTrailersLoaded: " + trailerVO.getName());

			final View trailerView = inflater.inflate(R.layout.item_trailer, mTrailersContainer, false);

			trailerView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse(
									trailerVO.getYoutubeUrl()
							)
					);

					startActivity(intent);
				}
			});

			UiViewUtil.setText(trailerView, R.id.trailer_item_title, trailerVO.getName());

			mTrailersContainer.addView(trailerView);
		}
	}

	private void onMovieReviewsLoaded(final ArrayList<MovieReviewVO> movieReviews) {
		if (movieReviews == null || movieReviews.size() == 0) {
			UiViewUtil.setText(getView(), R.id.movie_reviews_label, MovieVars.NO_REVIEWS_MSG);

			return;
		}

		mMovieReviews = movieReviews;

		if (mReviewsContainer == null) {
			mReviewsContainer = (ViewGroup) mRootView.findViewById(R.id.movie_reviews_container);
		}

		final LayoutInflater inflater = LayoutInflater.from(getActivity());

		for (final MovieReviewVO movieReviewVO : mMovieReviews) {
			final View reviewView = inflater.inflate(R.layout.item_review, mReviewsContainer, false);

			reviewView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse(
									movieReviewVO.getUrl()
							)
					);

					startActivity(intent);
				}
			});

			final TextView circle = UiViewUtil.setText(
					reviewView,
					R.id.review_circle_icon,
					String.valueOf(movieReviewVO.getAuthor().charAt(0)).toUpperCase()
			);

			circle.getBackground().setColorFilter(
					Color.parseColor(ColorUtil.getNextColor()),
					PorterDuff.Mode.SRC
			);

			UiViewUtil.setText(reviewView, R.id.review_item_author, movieReviewVO.getAuthor());

			final String content = movieReviewVO.getContent();
			int previewLength = MovieVars.CONTENT_PREVIEW_LENGTH;

			// Try to display more preview text depending on device orientation
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				previewLength = previewLength * 2;
			}

			UiViewUtil.setText(
					reviewView,
					R.id.review_item_content,
					content.length() > previewLength
							? content.substring(0, previewLength) + MovieVars.DOTTED
							: content
			);

			mReviewsContainer.addView(reviewView);
		}
	}

	private void setShareButtonOnClickListener(final MovieTrailerVO movieTrailerVO) {

		final ImageView shareBtnImageView = (ImageView) mRootView.findViewById(R.id.button_share);

		shareBtnImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType(MovieVars.SHARE_TYPE);
				intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

				intent.putExtra(
						Intent.EXTRA_SUBJECT,
						movieTrailerVO.getName()
				);

				intent.putExtra(
						Intent.EXTRA_TEXT,
						MovieVars.EXTRA_TEXT + movieTrailerVO.getYoutubeUrl()
				);

				try {
					startActivity(Intent.createChooser(intent, MovieVars.SHARE_TEXT));
				} catch (ActivityNotFoundException e) {
					Log.e(NAME, e.getMessage());
				}
			}
		});
	}

	private void onFeedError(String message) {
		UiViewUtil.displayToastMessage(getActivity(), HttpRequest.CONNECTIVY_ERROR);
	}

}
