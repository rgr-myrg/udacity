package net.usrlib.android.movies;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_detail);
	//	setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		if (savedInstanceState == null) {
			// Begin Fragment Transaction for Tablet
//			getSupportFragmentManager()
//					.beginTransaction()
//					.replace(
//							R.id.detail_container,
//							new DetailActivityFragment(),
//							DetailActivityFragment.NAME
//					)
//					.commit();
//			getSupportFragmentManager()
//					.beginTransaction()
//					.add(R.id.detail_container, new DetailActivityFragment())
//					.commit();
		}
	}

}
