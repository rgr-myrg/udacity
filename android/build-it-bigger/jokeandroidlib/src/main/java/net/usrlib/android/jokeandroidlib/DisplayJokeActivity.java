package net.usrlib.android.jokeandroidlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayJokeActivity extends AppCompatActivity {
	public static final String JOKE_KEY = "jokeKey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_joke);

		final TextView textView = (TextView) findViewById(R.id.joke_text);
		textView.setText(getJokeFromIntent());
	}

	private String getJokeFromIntent() {
		String joke = getIntent().getStringExtra(JOKE_KEY);

		if (joke == null) {
			joke = "No Joke";
		}

		return joke;
	}
}
