package net.usrlib.android.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.usrlib.android.event.Listener;
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieReviewVO;
import net.usrlib.android.movies.movieapi.MovieTrailerVO;
import net.usrlib.android.movies.movieapi.MovieVars;
import net.usrlib.android.util.TextViewUtil;

import java.util.ArrayList;

public class DetailActivityFragment extends BaseFragment {

	private ViewGroup mTrailersContainer = null;
	private ViewGroup mReviewsContainer = null;

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
		final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

		addEventListeners();

		initDetailView(rootView);

		if (instanceState == null) {
			Log.d("DETAIL", "instanceState is null");
		} else {

		}

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void addEventListeners() {
		MovieEvent.MovieTrailersLoaded.addListenerOnce(mMovieTrailersListener);
		MovieEvent.MovieReviewsLoaded.addListenerOnce(mMovieReviewsListener);
	}

	private void initDetailView(final View rootView) {
		Intent intent = getActivity().getIntent();

		if (intent == null || !intent.hasExtra(MovieItemVO.NAME)) {
			return;
		}

		final Bundle data = intent.getExtras();
		final MovieItemVO movieItemVO = (MovieItemVO) data.getParcelable(MovieItemVO.NAME);

		// Fetch Movie Trailers and Reviews as early as possible
		fetchMovieTrailersWithId(movieItemVO.getId());
		fetchMovieReviewsWithId(movieItemVO.getId());

		final ImageView posterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
		final ImageView favBtnImageView = (ImageView) rootView.findViewById(R.id.button_favorite);

		// Invoking placeholder causes the image to misalign. Meh. >:(
		Glide.with(getActivity())
				.load(movieItemVO.getImageUrl())
						//.placeholder(R.drawable.image_poster_placeholder)
						//.error(R.drawable.image_poster_placeholder)
						//.crossFade()
				.into(posterImageView);

		int resourceId = R.drawable.heart_unselected;

		if (Facade.getMoviesDBHelper().isMovieSetAsFavorite(movieItemVO.getId())) {
			resourceId = R.drawable.heart_selected;
		}

		favBtnImageView.setImageResource(resourceId);
		favBtnImageView.setTag(resourceId);

		favBtnImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMovieIfLiked(movieItemVO, (ImageView) v);
			}
		});

		TextViewUtil.setText(
				rootView,
				R.id.movie_title,
				movieItemVO.getOriginalTitle()
		);

		TextViewUtil.setText(
				rootView,
				R.id.movie_release_date,
				movieItemVO.getReleaseDate()
		);

		TextViewUtil.setText(
				rootView,
				R.id.movie_rating,
				String.valueOf(movieItemVO.getVoteAverage())
		);

		TextViewUtil.setText(
				rootView,
				R.id.movie_overview,
				movieItemVO.getOverview()
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
			return;
		}

		if (mTrailersContainer == null) {
			mTrailersContainer = (ViewGroup) getActivity().findViewById(R.id.movie_trailers_container);
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
			return;
		}

		if (mReviewsContainer == null) {
			mReviewsContainer = (ViewGroup) getActivity().findViewById(R.id.movie_reviews_container);
		}

		final LayoutInflater inflater = LayoutInflater.from(getActivity());

		for (final MovieReviewVO movieReviewVO : movieReviews) {
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

			TextViewUtil.setText(reviewView, R.id.review_item_title, movieReviewVO.getAuthor());

			mReviewsContainer.addView(reviewView);
		}
	}

	private void onMovieReviewsError() {
		//MovieEvent.MovieReviewsLoaded.deleteListener();
	}
}
