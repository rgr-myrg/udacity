package net.usrlib.android.util;

public class ColorUtil {

	// Color list from http://htmlcolorcodes.com/color-chart/material-design-color-chart/
	private static final String[] sColorCodes = {
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

	private static int index = 0;

	public static final String getNextColor() {
		if (index > sColorCodes.length) {
			index = 0;
		}

		index++;

		return sColorCodes[index];
	}

}
