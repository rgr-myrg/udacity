package com.example.xyzreader.application;

import android.app.Application;

/**
 * Created by rgr-myrg on 5/5/16.
 */
public class XYZReaderApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		//buildPicassoWithOkHttp();
	}

	private void buildPicassoWithOkHttp() {
//		Picasso picassoBuild = (new Picasso.Builder(this))
//				.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE))
//				.build();
//
//		picassoBuild.setIndicatorsEnabled(false);
//		picassoBuild.setLoggingEnabled(BuildConfig.DEBUG);
//
//		Picasso.setSingletonInstance(picassoBuild);
	}
}
