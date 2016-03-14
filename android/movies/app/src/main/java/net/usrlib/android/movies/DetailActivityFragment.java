package net.usrlib.android.movies;

import android.content.Intent;
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
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieReviewVO;
import net.usrlib.android.movies.movieapi.MovieTrailerVO;
import net.usrlib.android.movies.movieapi.MovieVars;
import net.usrlib.android.util.ColorUtil;
import net.usrlib.android.util.TextViewUtil;

import java.util.ArrayList;

public class DetailActivityFragment extends BaseFragment {

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
			onMovieTrailersError();
		}
	};

	private Listener mMovieReviewsListener = new Listener() {
		@Override
		public void onComplete(Object eventData) {
			onMovieReviewsLoaded((ArrayList<MovieReviewVO>) eventData);
		}

		@Override
		public void onError(Object eventData) {
			onMovieReviewsError();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle instanceState) {
		mRootView = inflater.inflate(R.layout.fragment_detail, container, false);

		if (instanceState == null) {
			Log.d("MAIN", "instanceState is null");
			addEventListeners();
			parseIntentAndFetchData();

		} else {
			restoreValuesFromBundle(instanceState);
		}

		return mRootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d("MAIN", "onSaveInstanceState");
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

	public void addEventListeners() {
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
		fetchMovieTrailersWithId(mMovieItemVO.getId());
		fetchMovieReviewsWithId(mMovieItemVO.getId());
	}

	private void restoreValuesFromBundle(final Bundle bundle) {
		Log.d("MAIN", "restoreValuesFromBundle");

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
		final ImageView posterImageView = (ImageView) mRootView.findViewById(R.id.movie_poster);
		final ImageView favBtnImageView = (ImageView) mRootView.findViewById(R.id.button_favorite);

		// Invoking placeholder causes the image to misalign. Meh. >:(
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
		favBtnImageView.setTag(resourceId);

		favBtnImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMovieIfLiked(mMovieItemVO, (ImageView) v);
			}
		});

		TextViewUtil.setText(
				mRootView,
				R.id.movie_title,
				mMovieItemVO.getOriginalTitle()
		);

		TextViewUtil.setText(
				mRootView,
				R.id.movie_release_date,
				mMovieItemVO.getReleaseDate()
		);

		TextViewUtil.setText(
				mRootView,
				R.id.movie_rating,
				String.valueOf(mMovieItemVO.getVoteAverage())
		);

		TextViewUtil.setText(
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

		// Set Result Code and Intent for onActivityResult
		getActivity().setResult(MovieVars.FAVORITED_RESULT_CODE, intent);
	}

	private void fetchMovieTrailersWithId(final int id) {
		Facade.getMovieApi().fetchMovieTrailersWithId(id);
	}

	private void fetchMovieReviewsWithId(final int id) {
		Facade.getMovieApi().fetchMovieReviewsWithId(id);
	}

	private void onMovieTrailersLoaded(final ArrayList<MovieTrailerVO> movieTrailers) {
		if (movieTrailers.size() == 0) {
			TextViewUtil.setText(getView(), R.id.movie_trailers_label, MovieVars.NO_TRAILERS);

			return;
		}

		mMovieTrailers = movieTrailers;

		Log.d("MAIN", "onMovieTrailersLoaded Activity: " + getActivity().toString());
		if (mTrailersContainer == null) {
			mTrailersContainer = (ViewGroup) mRootView.findViewById(R.id.movie_trailers_container);
			Log.d("MAIN", "onMovieTrailersLoaded: " + mTrailersContainer.toString());
		}

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

			TextViewUtil.setText(trailerView, R.id.trailer_item_title, trailerVO.getName());

			mTrailersContainer.addView(trailerView);
		}
	}

	private void onMovieTrailersError() {
		//MovieEvent.MovieTrailersLoaded.deleteListener(mMovieTrailersListener);
		//TODO: Handle gracefully
	}

	private void onMovieReviewsLoaded(final ArrayList<MovieReviewVO> movieReviews) {
		if (movieReviews.size() == 0) {
			TextViewUtil.setText(getView(), R.id.movie_reviews_label, MovieVars.NO_REVIEWS);

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

			final TextView circle = TextViewUtil.setText(
					reviewView,
					R.id.review_circle_icon,
					String.valueOf(movieReviewVO.getAuthor().charAt(0)).toUpperCase()
			);

			circle.getBackground().setColorFilter(
					Color.parseColor(ColorUtil.getNextColor()),
					PorterDuff.Mode.SRC
			);

			TextViewUtil.setText(reviewView, R.id.review_item_author, movieReviewVO.getAuthor());

			final String content = movieReviewVO.getContent();

			TextViewUtil.setText(
					reviewView,
					R.id.review_item_content,
					content.length() > MovieVars.CONTENT_PREVIEW_LENGTH
							? content.substring(0, MovieVars.CONTENT_PREVIEW_LENGTH) + MovieVars.DOTTED
							: content
			);

			mReviewsContainer.addView(reviewView);
		}
	}

	private void onMovieReviewsError() {
		//MovieEvent.MovieReviewsLoaded.deleteListener();
	}

}
