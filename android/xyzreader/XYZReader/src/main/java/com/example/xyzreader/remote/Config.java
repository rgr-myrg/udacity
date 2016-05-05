package com.example.xyzreader.remote;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
	public static final String NAME = Config.class.getSimpleName();
	public static final String BASE_URL_STRING = "https://dl.dropboxusercontent.com/u/231329/xyzreader_data/data.json";
	public static final URL BASE_URL;

	static {
		URL url = null;

		try {
			url = new URL(BASE_URL_STRING);
		} catch (MalformedURLException e) {
			Log.e(NAME, "Unable to create URL instance with " + BASE_URL_STRING);
			e.printStackTrace();
		}

		BASE_URL = url;
	}
}
