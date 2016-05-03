package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.udacity.gradle.builditbigger.task.EndpointAsyncTask;

import net.usrlib.android.jokeandroidlib.DisplayJokeActivity;
import net.usrlib.pattern.TinyEvent;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
	private ProgressBar mProgressBar = null;
	private TinyEvent.Listener mPostExecuteListener = new TinyEvent.Listener() {
		@Override
		public void onSuccess(Object data) {
			onEndPointPostExecute((String) data);
		}
		@Override
		public void onError(Object data) {
			onEndPointPostError();
		}
	};

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.fragment_main, container, false);

		EndpointAsyncTask.OnPostExecute.addListener(mPostExecuteListener);

		setProgressBar(root);
		setJokeButton(root);
		setAdView(root);

		return root;
	}

	private void setProgressBar(final View rootView) {
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
		mProgressBar.setVisibility(View.GONE);
	}

	private void setJokeButton(final View rootView) {
		final Button button = (Button) rootView.findViewById(R.id.joke_btn);

		button.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mProgressBar.setVisibility(View.VISIBLE);
						startEndPointTask();
					}
				}
		);
	}

	private void setAdView(final View rootView) {
		final AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device. e.g.
		// "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
		final AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build();
		mAdView.loadAd(adRequest);
	}

	private void startEndPointTask() {
		new EndpointAsyncTask().execute();
	}

	private void onEndPointPostExecute(final String result) {
		Log.d("MAIN", "onEndPointPostExecute result: " + result);

		final Context context = getActivity();
		final Intent intent = new Intent(context, DisplayJokeActivity.class);

		intent.putExtra(DisplayJokeActivity.JOKE_KEY, result);
		context.startActivity(intent);

		mProgressBar.setVisibility(View.GONE);
	}

	private void onEndPointPostError() {
		final Toast toast = Toast.makeText(
				getContext(),
				getString(R.string.joke_not_loaded_msg),
				Toast.LENGTH_SHORT
		);

		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
}
