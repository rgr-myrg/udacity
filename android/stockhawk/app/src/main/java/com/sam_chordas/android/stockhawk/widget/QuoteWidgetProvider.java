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
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.ChartActivity;

public class QuoteWidgetProvider extends AppWidgetProvider {
	public static final String SYNC_CLICKED= "com.sam_chordas.android.stockhawk.SYNC_CLICKED";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int appWidgetId : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals(SYNC_CLICKED)) {
			final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			final ComponentName componentName = new ComponentName(context, QuoteWidgetProvider.class);

			if (appWidgetManager == null || componentName == null) {
				return;
			}

			appWidgetManager.notifyAppWidgetViewDataChanged(
					appWidgetManager.getAppWidgetIds(componentName),
					R.id.widget_list
			);
		}
	}

	private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		final RemoteViews views = new RemoteViews(
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

		views.setOnClickPendingIntent(R.id.update, refreshPendingIntent);
		views.setEmptyView(R.id.widget_list, R.id.empty_view);

		final Intent openAppIntent = new Intent(context, ChartActivity.class);

		PendingIntent openAppPendingIntent = TaskStackBuilder
				.create(context)
				.addNextIntentWithParentStack(openAppIntent)
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		views.setPendingIntentTemplate(R.id.widget_list, openAppPendingIntent);

		// Set up the collection
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setRemoteAdapter(context, views);
		} else {
			setRemoteAdapterV11(context, views);
		}

		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(
				R.id.widget_list,
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
				R.id.widget_list,
				getNewIntentWithContext(context)
		);
	}

	public static Intent getNewIntentWithContext(Context context) {
		return new Intent(context, QuoteWidgetRemoteViewsService.class);
	}
}
