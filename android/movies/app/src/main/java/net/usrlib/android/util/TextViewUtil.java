package net.usrlib.android.util;

import android.view.View;
import android.widget.TextView;

public class TextViewUtil {

	public static final TextView setText(final View view, final int id, final String value) {
		final TextView textView = (TextView) view.findViewById(id);
		textView.setText(value);

		return textView;
	}

}
