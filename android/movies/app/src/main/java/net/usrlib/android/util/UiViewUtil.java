package net.usrlib.android.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UiViewUtil {

	public static final TextView setText(final View view, final int id, final String value) {
		if (view == null || value == null) {
			return null;
		}

		final TextView textView = (TextView) view.findViewById(id);

		if (textView == null) {
			return null;
		}

		textView.setText(value);

		return textView;
	}

	public static final void displayToastMessage(final Activity activity, final String message) {
		if (activity == null || message == null) {
			return;
		}

		activity.runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						Toast toast = Toast.makeText(
								activity.getApplicationContext(),
								message,
								Toast.LENGTH_SHORT
						);

						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.show();
					}
				}
		);
	}

}
