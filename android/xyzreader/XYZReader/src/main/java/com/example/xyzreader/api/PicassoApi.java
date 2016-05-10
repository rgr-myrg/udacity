package com.example.xyzreader.api;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.usrlib.pattern.TinyEvent;

/**
 * Created by rgr-myrg on 5/6/16.
 */
public class PicassoApi {
	public static final TinyEvent OnImageLoaded = new TinyEvent();

	public static enum ImageType {
		THUMBNAIL,
		FULLSIZE
	}

	public static final void loadImage(final Context context, final ImageView imageView, final String url) {
		Picasso.with(context)
				.load(url)
				.networkPolicy(NetworkPolicy.OFFLINE)
				.noPlaceholder()
				.fit()
				.centerCrop()
				.into(imageView, new Callback() {
					@Override
					public void onSuccess() {
						//onImageLoadSuccess(imageType);
						OnImageLoaded.notifySuccess();
					}

					@Override
					public void onError() {
						OnImageLoaded.notifyError();
					}
				});
	}
}
