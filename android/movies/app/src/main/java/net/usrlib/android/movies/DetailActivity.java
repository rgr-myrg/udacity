package net.usrlib.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.android.movies.movieapi.MovieItemVO;

public class DetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		retrieveMovieItemVO();
	}

	private void retrieveMovieItemVO() {
		Intent intent = getIntent();

		if (intent != null && intent.hasExtra(MovieItemVO.NAME)) {
			Bundle data = intent.getExtras();
			MovieItemVO movieItemVO = (MovieItemVO) data.getParcelable(MovieItemVO.NAME);

			ImageView imageView = (ImageView) findViewById(R.id.movie_poster);

			Glide.with(this)
					.load(movieItemVO.getImageUrl())
					.placeholder(R.drawable.image_poster_placeholder)
					.error(R.drawable.image_poster_placeholder)
					.crossFade()
					.into(imageView);

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

	private void setTextViewWithValue(int id, String value) {
		TextView textView = (TextView) findViewById(id);
		textView.setText(value);
	}

}
