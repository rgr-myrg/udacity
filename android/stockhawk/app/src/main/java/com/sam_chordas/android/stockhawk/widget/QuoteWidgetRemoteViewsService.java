package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by rgr-myrg on 4/27/16.
 */
public class QuoteWidgetRemoteViewsService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		Log.d("WIDGET", "onGetViewFactory");
		return new WidgetDataProvider(this);
	}
}
