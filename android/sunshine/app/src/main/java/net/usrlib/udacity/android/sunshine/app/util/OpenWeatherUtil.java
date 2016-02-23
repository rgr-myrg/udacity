package net.usrlib.udacity.android.sunshine.app.util;

import android.net.Uri;

import net.usrlib.udacity.android.sunshine.app.BuildConfig;
import net.usrlib.udacity.android.sunshine.app.constants.OpenWeatherVars;

/**
 * Created by rgr-myrg on 2/18/16.
 */
public class OpenWeatherUtil {

	public static Uri getUriWithQueryParam(String query) {

		return Uri.parse(OpenWeatherVars.FORECAST_BASE_URL).buildUpon()
				.appendQueryParameter(OpenWeatherVars.FORMAT_PARAM, OpenWeatherVars.JSON_FORMAT)
				.appendQueryParameter(OpenWeatherVars.UNITS_PARAM, OpenWeatherVars.METRIC_UNIT)
				.appendQueryParameter(OpenWeatherVars.DAYS_PARAM, OpenWeatherVars.DAYS_STRING)
				.appendQueryParameter(OpenWeatherVars.APPID_PARAM, BuildConfig.OPEN_WEATHER_KEY)
				.appendQueryParameter(OpenWeatherVars.QUERY_PARAM, query)
				.build();

	}

}
