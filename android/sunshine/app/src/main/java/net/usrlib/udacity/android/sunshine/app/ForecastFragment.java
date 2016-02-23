package net.usrlib.udacity.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.usrlib.udacity.android.sunshine.app.util.JsonHttpRequest;
import net.usrlib.udacity.android.sunshine.app.util.OpenWeatherUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

	private ArrayAdapter<String> mForecastAdapter;

	public ForecastFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		getActivity().getMenuInflater().inflate(R.menu.menu_forecast, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_refresh) {
			requestForecastUpdate();
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		requestForecastUpdate();

		String[] forecastArray = {
				"Today0 - Sunny - 88/63",
				"Today1 - Sunny - 88/63",
				"Today2 - Sunny - 88/63",
				"Today3 - Sunny - 88/63",
				"Today4 - Sunny - 88/63",
				"Today5 - Sunny - 88/63"
		};

		List<String> weekForecast = new ArrayList<String>(
				Arrays.asList(forecastArray)
		);

		mForecastAdapter = new ArrayAdapter<String>(
				getActivity(),
				R.layout.list_item_forecast,
				R.id.list_item_forecast_textview,
				weekForecast
		);

		ListView listView = (ListView) rootView.findViewById(R.id.list_view_forecast);
		listView.setAdapter(mForecastAdapter);

		return rootView;
	}

	private void requestForecastUpdate() {
		JsonHttpRequest request = new JsonHttpRequest(
				new JsonHttpRequest.RequestDelegate() {
					@Override
					public void onJsonLoaded(JSONObject json) {
						Log.d("MAIN JSONObject", json.toString());
						onForecastUpdate(json);
					}
				}
		);

		request.fetchWithUrl(OpenWeatherUtil.getUriWithQueryParam("33308,us").toString());
	}

	private void onForecastUpdate(JSONObject json) {

	}

}
