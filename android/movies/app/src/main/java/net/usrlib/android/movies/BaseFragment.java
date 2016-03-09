package net.usrlib.android.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import net.usrlib.android.movies.facade.Facade;

public class BaseFragment extends Fragment {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Facade creates shared objects which are re-used throughout the lifetime of the app.
		Facade.onCreate(getActivity().getBaseContext());
	}

}
