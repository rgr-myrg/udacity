package net.usrlib.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.android.movies.data.MoviesDB;
import net.usrlib.android.movies.movieapi.MovieApi;
import net.usrlib.android.movies.movieapi.MovieItemVO;

public class DetailActivityFragment extends Fragment {

	private MovieApi mMovieApi = null;
	private MoviesDB mMoviesDB = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

		if (mMoviesDB == null) {
			mMoviesDB = new MoviesDB(getActivity().getApplicationContext());
		}

		// Create only one mMovieApi instance
		if (mMovieApi == null) {
			mMovieApi = new MovieApi();
		}

		initDetailView(rootView);

		return rootView;
	}

	private void initDetailView(final View rootView) {
		Intent intent = getActivity().getIntent();

		if (intent != null && intent.hasExtra(MovieItemVO.NAME)) {
			final Bundle data = intent.getExtras();
			final MovieItemVO movieItemVO = (MovieItemVO) data.getParcelable(MovieItemVO.NAME);

			final ImageView posterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
			final ImageView favBtnImageView = (ImageView) rootView.findViewById(R.id.button_favorite);

			// Invoking placeholder causes the image to misalign. Meh.
			Glide.with(getActivity())
					.load(movieItemVO.getImageUrl())
					//.placeholder(R.drawable.image_poster_placeholder)
					//.error(R.drawable.image_poster_placeholder)
					//.crossFade()
					.into(posterImageView);

			int resourceId = R.drawable.heart_unselected;

			if (mMoviesDB.isMovieSetAsFavorite(movieItemVO.getId())) {
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

			setTextViewWithValue(
					rootView,
					R.id.movie_title,
					movieItemVO.getOriginalTitle()
			);

			setTextViewWithValue(
					rootView,
					R.id.movie_release_date,
					movieItemVO.getReleaseDate()
			);

			setTextViewWithValue(
					rootView,
					R.id.movie_rating,
					String.valueOf(movieItemVO.getVoteAverage())
			);

			setTextViewWithValue(
					rootView,
					R.id.movie_overview,
					movieItemVO.getOverview()
			);
		}
	}

	private void setTextViewWithValue(final View view, final int id, final String value) {
		TextView textView = (TextView) view.findViewById(id);
		textView.setText(value);
	}

	private void setMovieIfLiked(final MovieItemVO movieItemVO, final ImageView imageView) {
		final boolean hasSelected = (Integer) imageView.getTag() != R.drawable.heart_selected;

		final int imageResource = hasSelected
		 		? R.drawable.heart_selected
				: R.drawable.heart_unselected;

		mMoviesDB.setMovieAsFavorite(movieItemVO.toContentValues(), hasSelected);

		imageView.setImageResource(imageResource);
		imageView.setTag(imageResource);
	}

}
