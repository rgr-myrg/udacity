package net.usrlib.android.util;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class HttpRequest extends AsyncTask<Void, Void, String> {

	public static final String JSON_NULL_ERROR = "Json Object is Null";
	public static final String CONNECTIVY_ERROR = "No Internet Connection";

	private Delegate mRequestDelegate;
	private boolean mHasJsonRequest;
	private URL url;

	public interface Delegate {
		void onPostExecuteComplete(Object object);
		void onError(String message);
	}

	public HttpRequest(final Delegate delegate) {
		mRequestDelegate = delegate;
	}

	public final void fetchJsonObjectWithUrl(final String targetUrl) {
		mHasJsonRequest = true;
		fetchWithUrl(targetUrl);
	}

	public final void fetchWithUrl(final String targetUrl) {
		try {
			this.url = new URL(targetUrl);
		} catch (MalformedURLException e) {
			mRequestDelegate.onError(e.getMessage());
			e.printStackTrace();
		}

		this.execute();
	}

	@Override
	protected String doInBackground(Void... params) {
		StringBuilder body = new StringBuilder();
		HttpURLConnection urlConnection = null;
		BufferedReader bufferedReader = null;

		try {
			// HttpURLConnection uses GET request by default.
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();

			InputStream inputStream = urlConnection.getInputStream();

			if (inputStream == null) {
				return null;
			}

			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";

			while ((line = bufferedReader.readLine()) != null) {
				body.append(line);
			}
		} catch (IOException e) {
			mRequestDelegate.onError(e.getMessage());
			e.printStackTrace();

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					mRequestDelegate.onError(e.getMessage());
					e.printStackTrace();
				}
			}
		}

		return String.valueOf(body);
	}

	@Override
	protected void onPostExecute(final String string) {
		super.onPostExecute(string);

		if (string != null) {
			try {
				if (mHasJsonRequest) {
					JSONObject jsonObject = new JSONObject(string);

					if (jsonObject != null ) {
						mRequestDelegate.onPostExecuteComplete(jsonObject);
					} else {
						mRequestDelegate.onError(JSON_NULL_ERROR);
					}
				} else {
					mRequestDelegate.onPostExecuteComplete(string);
				}

			} catch (JSONException e) {
				mRequestDelegate.onError(e.getMessage());
				e.printStackTrace();

			}
		}
	}

}
