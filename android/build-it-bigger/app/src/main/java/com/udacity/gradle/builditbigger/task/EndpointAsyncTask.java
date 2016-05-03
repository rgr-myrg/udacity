package com.udacity.gradle.builditbigger.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import net.usrlib.android.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Created by rgr-myrg on 5/2/16.
 */
public class EndpointAsyncTask extends AsyncTask<Context, Void, String> {
	private static MyApi sMyApiService = null;
	private Context mContext;

	@Override
	protected String doInBackground(Context... params) {
		if (sMyApiService == null) {
			MyApi.Builder builder = new MyApi.Builder(
				AndroidHttp.newCompatibleTransport(),
				new AndroidJsonFactory(),
				null
			)

			// options for running against local devappserver
			// ­ 10.0.2.2 is localhost's IP address in Android emulator
			// ­ turn off compression when running against local devappserver

			.setRootUrl("http://10.0.2.2:8080/_ah/api/")
			.setGoogleClientRequestInitializer(
				new GoogleClientRequestInitializer() {
					@Override
					public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
							throws IOException {
						abstractGoogleClientRequest.setDisableGZipContent(true);
					}
				}
			);

			sMyApiService = builder.build();
		}

		mContext = params[0];

		try {
			return sMyApiService.tellJoke().execute().getData();
		} catch (IOException e) {
			return e.getMessage();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
	}
}
