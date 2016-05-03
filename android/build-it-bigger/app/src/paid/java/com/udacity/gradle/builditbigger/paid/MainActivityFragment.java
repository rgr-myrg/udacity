package com.udacity.gradle.builditbigger.paid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.BaseActivityFragment;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.task.EndpointAsyncTask;

/**
 * Created by rgr-myrg on 5/3/16.
 */
public class MainActivityFragment extends BaseActivityFragment {
	// Constructor Needed for Different Flavor Versions
	public MainActivityFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		Log.i("MainActivityFragment", "onCreateView PAID version");

		final View root = inflater.inflate(R.layout.fragment_main_activity, container, false);

		EndpointAsyncTask.OnPostExecute.addListener(mPostExecuteListener);

		setProgressBar(root);
		setJokeButton(root);

		return root;
	}
}
