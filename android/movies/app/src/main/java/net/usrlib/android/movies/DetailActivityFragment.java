package net.usrlib.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.movieapi.MovieVars;

public class DetailActivityFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

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

		Facade.getMoviesDBHelper().setMovieAsFavorite(movieItemVO.toContentValues(), hasSelected);

		imageView.setImageResource(imageResource);
		imageView.setTag(imageResource);

		Intent intent = new Intent();
		intent.putExtra(MovieVars.IS_FAVORITED_KEY, true);

		// Set Result Code and Intent for onActivityResult
		getActivity().setResult(MovieVars.FAVORITED_RESULT_CODE, intent);
	}

}
