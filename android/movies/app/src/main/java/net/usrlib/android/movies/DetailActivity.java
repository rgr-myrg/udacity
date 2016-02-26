package net.usrlib.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		retrieveMovieItemVO();
	}

	private void retrieveMovieItemVO() {
		Intent intent = getIntent();

		if (intent != null && intent.hasExtra(MovieItemVO.NAME)) {
			Bundle data = intent.getExtras();
			MovieItemVO movieItemVO = (MovieItemVO) data.getParcelable(MovieItemVO.NAME);
			Log.d("MAIN", movieItemVO.getOriginalTitle());

			ImageView imageView = (ImageView) findViewById(R.id.movie_poster);
			TextView title = (TextView) findViewById(R.id.movie_title);
			TextView date = (TextView) findViewById(R.id.movie_release_date);
			TextView rating = (TextView) findViewById(R.id.movie_rating);
			TextView votes = (TextView) findViewById(R.id.movie_votes);
			TextView overview = (TextView) findViewById(R.id.movie_overview);

			Glide.with(this)
					.load(movieItemVO.getImageUrl())
					.into(imageView);

			title.setText(movieItemVO.getOriginalTitle());

			date.setText(movieItemVO.getReleaseDate());

			rating.setText(String.valueOf(movieItemVO.getVoteAverage()));

			votes.setText(String.valueOf(movieItemVO.getVoteCount()));

			overview.setText(String.valueOf(movieItemVO.getOverview()));
		}
	}

}
