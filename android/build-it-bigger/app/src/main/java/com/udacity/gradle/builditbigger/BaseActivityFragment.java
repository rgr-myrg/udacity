package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.gradle.builditbigger.task.EndpointAsyncTask;

import net.usrlib.android.jokeandroidlib.DisplayJokeActivity;
import net.usrlib.material.MaterialTheme;
import net.usrlib.material.Theme;
import net.usrlib.pattern.TinyEvent;

/**
 * Created by rgr-myrg on 5/3/16.
 */
public class BaseActivityFragment extends Fragment {
	protected MaterialTheme mMaterialTheme = MaterialTheme.get(Theme.FREESIA);
	protected TextView mJokeButton = null;
	protected ProgressBar mProgressBar = null;
	protected TinyEvent.Listener mPostExecuteListener = new TinyEvent.Listener() {
		@Override
		public void onSuccess(Object data) {
			onEndPointPostExecute((String) data);
		}
		@Override
		public void onError(Object data) {
			onEndPointPostError();
		}
	};

	// Empty Constructor Needed for Different Flavor Versions
	public BaseActivityFragment() {
	}

	protected void setProgressBar(final View rootView) {
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
		mProgressBar.setVisibility(View.GONE);
	}

	protected void setJokeButtonColor(final View rootView) {
		if (mJokeButton == null) {
			mJokeButton = (TextView) rootView.findViewById(R.id.joke_btn);
		}

		mJokeButton.getBackground().setColorFilter(
				Color.parseColor(mMaterialTheme.getNextColor().hex),
				PorterDuff.Mode.SRC
		);
	}

	protected void setJokeButtonClickListener(final View rootView) {
		if (mJokeButton == null) {
			mJokeButton = (TextView) rootView.findViewById(R.id.joke_btn);
		}

		mJokeButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						startEndPointTask();
					}
				}
		);
	}

	protected void startEndPointTask() {
		mProgressBar.setVisibility(View.VISIBLE);
		new EndpointAsyncTask().execute();
	}

	protected void onEndPointPostExecute(final String result) {
		Log.d("MAIN", "onEndPointPostExecute result: " + result);

		final Context context = getActivity();

		if (context == null) {
			return;
		}

		final Intent intent = new Intent(context, DisplayJokeActivity.class);

		if (intent == null) {
			return;
		}

		intent.putExtra(DisplayJokeActivity.JOKE_KEY, result);
		context.startActivity(intent);

		mProgressBar.setVisibility(View.GONE);
	}

	protected void onEndPointPostError() {
		final Toast toast = Toast.makeText(
				getContext(),
				getString(R.string.joke_not_loaded_msg),
				Toast.LENGTH_SHORT
		);

		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
}
