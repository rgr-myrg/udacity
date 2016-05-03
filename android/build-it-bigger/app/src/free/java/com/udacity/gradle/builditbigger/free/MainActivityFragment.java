package com.udacity.gradle.builditbigger.free;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.udacity.gradle.builditbigger.BaseActivityFragment;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.task.EndpointAsyncTask;

public class MainActivityFragment extends BaseActivityFragment {
	// Constructor Needed for Different Flavor Versions
	public MainActivityFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		Log.i("MainActivityFragment", "onCreateView FREE version");
		final View root = inflater.inflate(R.layout.fragment_main_activity, container, false);

		EndpointAsyncTask.OnPostExecute.addListener(mPostExecuteListener);

		setProgressBar(root);
		setJokeButton(root);
		setAdView(root);

		return root;
	}

	private void setAdView(final View rootView) {
		final AdView mAdView = (AdView) rootView.findViewById(R.id.adView);

		if (mAdView == null) {
			return;
		}

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device. e.g.
		// "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
		final AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build();

		mAdView.loadAd(adRequest);
	}
}
