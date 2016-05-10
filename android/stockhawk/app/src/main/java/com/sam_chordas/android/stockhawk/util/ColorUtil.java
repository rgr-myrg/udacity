package com.sam_chordas.android.stockhawk.util;

/**
 * Created by rgr-myrg on 4/28/16.
 */

public final class ColorUtil {
	// Color list from http://htmlcolorcodes.com/color-chart/material-design-color-chart/
	private static final String[] sColorCodesBright = {
			"#D32F2F",
			"#607D8B",
			"#C2185B",
			"#795548",
			"#E040FB",
			"#FF9E80",
			"#7C4DFF",
			"#303F9F",
			"#64B5F6",
			"#FF9E80",
			"#00ACC1",
			"#FBC02D",
			"#00BFA5",
			"#F57F17",
			"#AEEA00",
			"#43A047",
			"#AFB42B",
			"#7CB342"
	};

	private static final String[] sColorCodesDark = {
			"#C70039",
			"#757575",
			"#1565C0",
			"#FF5733",
			"#5C6BC0",
			"#00838F",
			"#9E9D24",
			"#A1887F",
			"#78909C"
	};

	private static int sIndexBright = 0;
	private static int sIndexDark = 0;

	public static final String getNextColorBrightTheme() {
		String selectedColor;

		if (sIndexBright > sColorCodesBright.length - 1) {
			sIndexBright = 0;
		}

		selectedColor = sColorCodesBright[sIndexBright];

		sIndexBright++;

		return selectedColor;
	}

	public static final String getNextColorDarkTheme() {
		String selectedColor;

		if (sIndexDark > sColorCodesDark.length - 1) {
			sIndexDark = 0;
		}

		selectedColor = sColorCodesDark[sIndexDark];

		sIndexDark++;

		return selectedColor;
	}
}