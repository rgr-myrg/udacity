package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.ChartSqlActivity;

public class QuoteWidgetProvider extends AppWidgetProvider {
	public static final String SYNC_CLICKED = "com.sam_chordas.android.stockhawk.SYNC_CLICKED";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int appWidgetId : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		Log.d("QuoteWidgetProvider", "onReceive");

		if (intent.getAction().equals(SYNC_CLICKED)) {
			final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			final ComponentName componentName = new ComponentName(context, QuoteWidgetProvider.class);

			if (appWidgetManager == null || componentName == null) {
				return;
			}

			appWidgetManager.notifyAppWidgetViewDataChanged(
					appWidgetManager.getAppWidgetIds(componentName),
					R.id.widget_list_view
			);
		}
	}

	private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		final RemoteViews remoteViews = new RemoteViews(
				context.getPackageName(),
				R.layout.widget_collection
		);

		final Intent refreshIntent = new Intent(context, QuoteWidgetProvider.class);

		refreshIntent.setAction(SYNC_CLICKED);

		final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
				context,
				0,
				refreshIntent,
				PendingIntent.FLAG_UPDATE_CURRENT
		);

		remoteViews.setOnClickPendingIntent(R.id.update_button, refreshPendingIntent);
		remoteViews.setEmptyView(R.id.widget_list_view, R.id.empty_text_view);

		final Intent openAppIntent = new Intent(context, ChartSqlActivity.class);

		PendingIntent openAppPendingIntent = TaskStackBuilder
				.create(context)
				.addNextIntentWithParentStack(openAppIntent)
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		remoteViews.setPendingIntentTemplate(R.id.widget_list_view, openAppPendingIntent);

		// Set up the collection
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setRemoteAdapter(context, remoteViews);
		} else {
			setRemoteAdapterV11(context, remoteViews);
		}

		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(
				R.id.widget_list_view,
				getNewIntentWithContext(context)
		);
	}

	/**
	 * Sets the remote adapter used to fill in the list items
	 *
	 * @param views RemoteViews to set the RemoteAdapter
	 */
	@SuppressWarnings("deprecation")
	private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(
				0,
				R.id.widget_list_view,
				getNewIntentWithContext(context)
		);
	}

	public static Intent getNewIntentWithContext(Context context) {
		return new Intent(context, QuoteWidgetRemoteViewsService.class);
	}
}
