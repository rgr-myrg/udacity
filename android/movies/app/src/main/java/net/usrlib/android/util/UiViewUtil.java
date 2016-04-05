package net.usrlib.android.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public final class UiViewUtil {

	private static boolean sIsDisplayingToast;

	public static final TextView setText(final View view, final int id, final String value) {
		if (view == null || value == null) {
			return null;
		}

		final Activity activity = (Activity) view.getContext();
		final TextView textView = (TextView) view.findViewById(id);

		if (activity == null || textView == null) {
			return null;
		}

		activity.runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						textView.setText(value);
					}
				}
		);

		return textView;
	}

	public static final void displayToastMessage(final Activity activity, final String message) {
		if (activity == null || message == null || sIsDisplayingToast) {
			return;
		}

		sIsDisplayingToast = true;

		activity.runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						final Toast toast = Toast.makeText(
								activity.getApplicationContext(),
								message,
								Toast.LENGTH_SHORT
						);

						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.show();

						sIsDisplayingToast = false;
					}
				}
		);
	}

	public static final void setViewAsVisible(final Activity activity, final View view) {
		if (activity == null || view == null) {
			return;
		}

		activity.runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						view.setVisibility(View.VISIBLE);
					}
				}
		);
	}

	public static final void setViewAsInvisible(final Activity activity, final View view) {
		if (activity == null || view == null) {
			return;
		}

		activity.runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						view.setVisibility(View.INVISIBLE);
					}
				}
		);
	}

	public static final TextView applyNextColorOnTextView(final View view, final int id) {
		if (view == null) {
			return null;
		}

		final Activity activity = (Activity) view.getContext();
		final TextView textView = (TextView) view.findViewById(id);

		if (activity == null || textView == null) {
			return null;
		}

		activity.runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						textView.getBackground().setColorFilter(
								Color.parseColor(ColorUtil.getNextColorDarkTheme()),
								PorterDuff.Mode.SRC
						);
					}
				}
		);

		return textView;
	}
}
