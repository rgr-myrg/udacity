package net.usrlib.android.jokeandroidlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DisplayJokeActivity extends AppCompatActivity {
	public static final String JOKE_KEY = "jokeKey";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_joke);
	}
}
