package com.example.xyzreader.remote;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteEndpointUtil {
	public static final String NAME = RemoteEndpointUtil.class.getSimpleName();
	public static final int BYTE_MAX_VALUE = 1024;
	public static final String CHART_SET_ENCODING = "UTF-8";

	private RemoteEndpointUtil() {
	}

	public static JSONArray fetchJsonArray() {
		String itemsJson = null;

		try {
			itemsJson = fetchPlainText(Config.BASE_URL);
		} catch (IOException e) {
			Log.e(NAME, "Error fetching items JSON", e);
			return null;
		}

		// Parse JSON
		try {
			JSONTokener tokener = new JSONTokener(itemsJson);
			Object val = tokener.nextValue();

			if (!(val instanceof JSONArray)) {
				throw new JSONException("Expected JSONArray");
			}

			return (JSONArray) val;
		} catch (JSONException e) {
			Log.e(NAME, "Error parsing items JSON", e);
		}

		return null;
	}

//	static String fetchPlainText(URL url) throws IOException {
//		OkHttpClient client = new OkHttpClient();
//
//		Request request = new Request.Builder()
//				.url(url)
//				.build();
//
//		Response response = client.newCall(request).execute();
//		return response.body().string();
//	}

	public static final String fetchPlainText(final URL url) throws IOException {
		return new String(
				fetchByteArrayOutputStream(url),
				CHART_SET_ENCODING
		);
	}

	public static final byte[] fetchByteArrayOutputStream(final URL url) throws IOException {
		InputStream inputStream = null;

		try {
			final HttpURLConnection httpURLConnection = new OkUrlFactory(new OkHttpClient()).open(url);
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			inputStream = httpURLConnection.getInputStream();

			final byte[] buffer = new byte[BYTE_MAX_VALUE];
			int bytesRead;

			while ((bytesRead = inputStream.read(buffer)) > 0) {
				byteArrayOutputStream.write(buffer, 0, bytesRead);
			}

			return byteArrayOutputStream.toByteArray();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
}
