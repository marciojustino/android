package com.example.androidappdeveloper;

import android.os.Bundle;
import android.provider.CalendarContract.Instances;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class DisplayFragmentActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_fragment);

		// 1.Check that activity is using the layout version with the fragment
		// container "FrameLayout"...
		if (findViewById(R.id.headlines_fragment) != null) {

			if (savedInstanceState != null)
				return;

			// 2.Insert headline fragment in FrameLayout...
			DisplayFragment(new HeadlineFragment());

			// 3.Insert article fragment in FrameLayout...
			DisplayFragment(new ArticleFragment());
		}
	}

	private void DisplayFragment(Fragment f) {
		// 1.In case this activity was started with special instructions
		// from an Intent, pass the Intent's extras to the fragment as
		// arguments...
		f.setArguments(getIntent().getExtras());

		// 3.Add the fragment to the 'fragment_container' FrameLayout
		if (f.getClass() == HeadlineFragment.class) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.headlines_fragment, f).commit();
			return;
		}
		
		if (f.getClass() == ArticleFragment.class) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.article_fragment, f).commit();
			return;
		}
	}
}
