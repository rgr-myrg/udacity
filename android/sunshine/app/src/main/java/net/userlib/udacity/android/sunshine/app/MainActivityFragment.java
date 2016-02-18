package net.userlib.udacity.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	private ArrayAdapter<String> mForecastAdapter;

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

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
}
