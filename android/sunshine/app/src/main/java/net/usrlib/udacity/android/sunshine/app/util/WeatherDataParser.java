package net.usrlib.udacity.android.sunshine.app.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rgr-myrg on 2/18/16.
 */
public class WeatherDataParser {

	/**
	 * Given a string of the form returned by the api call:
	 * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
	 * retrieve the maximum temperature for the day indicated by dayIndex
	 * (Note: 0-indexed, so 0 would refer to the first day).
	 */
	public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
			throws JSONException {
//list[dayIndex].weather[0]
//list[[dayIndex].temp.max
		JSONObject jsonObject = new JSONObject(weatherJsonStr);
		JSONObject data = (JSONObject) jsonObject.getJSONArray("list").get(dayIndex);
		return data.getJSONObject("temp").getDouble("max");

	}

}
