package com.sam_chordas.android.stockhawk.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sam_chordas.android.stockhawk.R;

/**
 * Created by rgr-myrg on 4/18/16.
 */
public class UiUtil {
	public static final void showNetworkNotAvailableDialog(Context context) {
		new MaterialDialog.Builder(context)
				.title(R.string.network_not_available_title)
				.content(R.string.network_not_available)
				.show();
	}
}
