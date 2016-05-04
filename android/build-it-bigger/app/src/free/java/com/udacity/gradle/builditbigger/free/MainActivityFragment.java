package com.udacity.gradle.builditbigger.free;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.udacity.gradle.builditbigger.BaseActivityFragment;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.task.EndpointAsyncTask;

public class MainActivityFragment extends BaseActivityFragment {
	private PublisherInterstitialAd mInterstitialAd = null;

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
		setInterstitialAd();
		setAdView(root);
		setJokeButton(root);

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

	private void setInterstitialAd() {
		mInterstitialAd = new PublisherInterstitialAd(getContext());
		mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				super.onAdClosed();
				startEndPointTask();
				requestNewInterstitial();
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				super.onAdFailedToLoad(errorCode);
				requestNewInterstitial();
			}

			@Override
			public void onAdLeftApplication() {
				super.onAdLeftApplication();
			}

			@Override
			public void onAdOpened() {
				super.onAdOpened();
			}

			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
			}
		});

		requestNewInterstitial();
	}

	private void requestNewInterstitial() {
		PublisherAdRequest adRequest = new PublisherAdRequest
				.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build();

		mInterstitialAd.loadAd(adRequest);
	}

	@Override
	protected void setJokeButton(View rootView) {
		final Button button = (Button) rootView.findViewById(R.id.joke_btn);

		button.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (mInterstitialAd.isLoaded()) {
							mInterstitialAd.show();
						} else {
							startEndPointTask();
						}
					}
				}
		);
	}
}
