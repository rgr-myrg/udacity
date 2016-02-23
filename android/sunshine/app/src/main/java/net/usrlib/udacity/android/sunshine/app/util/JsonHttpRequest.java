package net.usrlib.udacity.android.sunshine.app.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rgr-myrg on 2/18/16.
 */
public class JsonHttpRequest extends AsyncTask<Void, Void, String> {

	private RequestDelegate requestDelegate;
	private URL url;

	public interface RequestDelegate {
		void onJsonLoaded(JSONObject json);
	}

	public JsonHttpRequest(RequestDelegate delegate) {
		this.requestDelegate = delegate;
	}

	public void fetchWithUrl(String targetUrl) {
		Log.d("MAIN", targetUrl);
		try {
			this.url = new URL(targetUrl);
		} catch (MalformedURLException e) {
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

			e.printStackTrace();

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return String.valueOf(body);
	}

	@Override
	protected void onPostExecute(String string) {
		super.onPostExecute(string);

		if ( string != null ) {
			try {

				JSONObject jsonObject = new JSONObject(string);
				requestDelegate.onJsonLoaded(jsonObject);

			} catch (JSONException e) {

				e.printStackTrace();

			}
		}
	}
}
