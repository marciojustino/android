package com.example.androidappdeveloper;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.androidappdeveloper.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first

		// 1. Persist any user's change that needs recovery...

		// 2. Release active resources...
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		// 1. Initialize resources that released in onPause event...
	}

	@Override
	public void onStop() {
		super.onStop();

		// 1. Finalize all active resources...
	}

	@Override
	public void onRestart() {
		super.onRestart(); // Always call the superclass method first

		// 1. Recover saved data in onStop event...
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendMessage(View view) {
		// 1. Create new activity...
		Intent intent = new Intent(this, DisplayMessageActivity.class);

		// 2. Get data to send to new activity...
		EditText edit = (EditText) findViewById(R.id.edit_message);
		String message = edit.getText().toString();

		// 1. Send data to DisplayMessageActivity activity...
		intent.putExtra(EXTRA_MESSAGE, message);

		// 2. Start new activity...
		startActivity(intent);
	}

	public void onFragmentClick(View view) {
		// 1. Create new activity...
		Intent intent = new Intent(this, DisplayFragmentActivity.class);
		
		// 2. Start new activity...
		startActivity(intent);
	}
}
