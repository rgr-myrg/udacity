package com.sam_chordas.android.stockhawk.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sam_chordas.android.stockhawk.R;

/**
 * Created by rgr-myrg on 4/18/16.
 */
public final class UiUtil {
	private static String sStockExistsMsg;
	private static String sRefreshingMsg;
	private static ProgressBar sProgressBar;

	public static final void onCreate(final AppCompatActivity app) {
		sStockExistsMsg = app.getString(R.string.stock_exists);
		sRefreshingMsg = app.getString(R.string.refreshing_message);
	}

	public static final void showNetworkNotAvailableDialog(final Context context) {
		new MaterialDialog.Builder(context)
				.title(R.string.network_not_available_title)
				.content(R.string.network_not_available)
				.show();
	}

	public static final void displayToast(final AppCompatActivity app, final String message) {
		Toast toast = Toast.makeText(app, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
		toast.show();
	}

	public static final void displayStockExistsMsg(final AppCompatActivity app) {
		displayToast(app, sStockExistsMsg);
	}

	public static final void displayRefreshingMsg(final AppCompatActivity app) {
		displayToast(app, sRefreshingMsg);
	}

	public static final void displayProgressBar(final AppCompatActivity app) {
		setProgressBarVisibility(app, View.VISIBLE);
	}

	public static final void hideProgressBar(final AppCompatActivity app) {
		setProgressBarVisibility(app, View.INVISIBLE);
	}

	private static final void setProgressBarVisibility(final AppCompatActivity app, final int value) {
		if (sProgressBar == null) {
			sProgressBar = (ProgressBar) app.findViewById(R.id.progress_bar);
		}

		app.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				sProgressBar.setVisibility(value);
			}
		});
	}
}
