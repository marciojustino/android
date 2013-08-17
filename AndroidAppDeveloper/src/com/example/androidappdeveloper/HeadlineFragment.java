package com.example.androidappdeveloper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HeadlineFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_headline_view, container,
				false);
	}

	// Lifecicle events...
	@Override
	public void onPause() {

	}

	@Override
	public void onStop() {

	}

	@Override
	public void onResume() {

	}

}
