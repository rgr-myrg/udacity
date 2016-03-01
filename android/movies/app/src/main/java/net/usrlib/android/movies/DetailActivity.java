package net.usrlib.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.android.movies.data.MoviesDB;
import net.usrlib.android.movies.movieapi.MovieItemVO;

public class DetailActivity extends AppCompatActivity {

	private MoviesDB mMoviesDB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mMoviesDB = new MoviesDB(getApplicationContext());

		retrieveMovieItemVO();
	}

	private void retrieveMovieItemVO() {
		Intent intent = getIntent();

		if (intent != null && intent.hasExtra(MovieItemVO.NAME)) {
			final Bundle data = intent.getExtras();
			final MovieItemVO movieItemVO = (MovieItemVO) data.getParcelable(MovieItemVO.NAME);
			final ImageView posterImageView = (ImageView) findViewById(R.id.movie_poster);
			final ImageView favBtnImageView = (ImageView) findViewById(R.id.button_favorite);

			// Invoking placeholder causes the image to misalign. Meh.
			Glide.with(this)
					.load(movieItemVO.getImageUrl())
				//	.placeholder(R.drawable.image_poster_placeholder)
				//	.error(R.drawable.image_poster_placeholder)
				//	.crossFade()
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
					R.id.movie_title,
					movieItemVO.getOriginalTitle()
			);

			setTextViewWithValue(
					R.id.movie_release_date,
					movieItemVO.getReleaseDate()
			);

			setTextViewWithValue(
					R.id.movie_rating,
					String.valueOf(movieItemVO.getVoteAverage())
			);

			setTextViewWithValue(
					R.id.movie_votes,
					String.valueOf(movieItemVO.getVoteCount())
			);

			setTextViewWithValue(
					R.id.movie_overview,
					movieItemVO.getOverview()
			);
		}
	}

	private void setTextViewWithValue(final int id, final String value) {
		TextView textView = (TextView) findViewById(id);
		textView.setText(value);
	}

	private void setMovieIfLiked(final MovieItemVO movieItemVO, final ImageView imageView) {
		final boolean isMovieLiked = (Integer) imageView.getTag() == R.drawable.heart_selected;
		final int imageResource = isMovieLiked
				? R.drawable.heart_unselected
				: R.drawable.heart_selected;

		mMoviesDB.setMovieAsFavorite(movieItemVO.toContentValues(), !isMovieLiked);

		imageView.setImageResource(imageResource);
		imageView.setTag(imageResource);
	}

}
