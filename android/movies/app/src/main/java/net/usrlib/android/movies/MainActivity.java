package net.usrlib.android.movies;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import net.usrlib.android.movies.lifecycle.ActivityLifecycle;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityLifecycle.onActivityCreated(this, savedInstanceState);

		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityLifecycle.onActivityDestroyed(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		ActivityLifecycle.onActivityPaused(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityLifecycle.onActivityResumed(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
		ActivityLifecycle.onActivitySaveInstanceState(this, outState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		ActivityLifecycle.onActivityStarted(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		ActivityLifecycle.onActivityStopped(this);
	}

}
