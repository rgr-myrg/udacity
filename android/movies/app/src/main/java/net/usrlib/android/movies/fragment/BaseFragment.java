package net.usrlib.android.movies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import net.usrlib.android.movies.facade.Facade;

public class BaseFragment extends Fragment {

	protected View mRootView = null;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Facade creates shared objects which are re-used throughout the lifetime of the app.
		Facade.onCreate(getActivity().getBaseContext());
	}

	public View getRootView() {
		return mRootView;
	}

}
