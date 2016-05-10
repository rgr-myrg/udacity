package com.udacity.gradle.builditbigger.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import net.usrlib.android.backend.myApi.MyApi;
import net.usrlib.pattern.TinyEvent;

import java.io.IOException;

/**
 * Created by rgr-myrg on 5/2/16.
 */
public class EndpointAsyncTask extends AsyncTask<Context, Void, String> {
	public static final String APP_SERVER = "https://jokeapp-1300.appspot.com/_ah/api/";
	public static final String LOCALHOST = "http://10.0.2.2:8080/_ah/api/";
	public static final TinyEvent OnPostExecute = new TinyEvent();

	private static MyApi sMyApiService = null;

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

			.setRootUrl(APP_SERVER)
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

		try {
			return sMyApiService.tellJoke().execute().getData();
		} catch (IOException e) {
			OnPostExecute.notifyError(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		OnPostExecute.notifySuccess(result);
	}
}
