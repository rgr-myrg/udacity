package com.sam_chordas.android.stockhawk.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sam_chordas.android.stockhawk.R;

/**
 * Created by rgr-myrg on 4/18/16.
 */
public final class UiUtil {
	private static String sStockExistsMsg;

	public static void onCreate(AppCompatActivity app) {
		sStockExistsMsg = app.getString(R.string.stock_exists);
	}

	public static final void showNetworkNotAvailableDialog(Context context) {
		new MaterialDialog.Builder(context)
				.title(R.string.network_not_available_title)
				.content(R.string.network_not_available)
				.show();
	}

	public static final String getStockExistsMsg() {
		return sStockExistsMsg;
	}
}
