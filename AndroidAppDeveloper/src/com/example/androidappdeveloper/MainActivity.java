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
	public void onPause(){
		super.onPause();
		
		// Manage onPause activity...
	}
	
	@Override
	public void onStop(){
		super.onStop();
		
		// Manage onStop activity...
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendMessage(View view){
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText edit = (EditText)findViewById(R.id.edit_message);
		String message = edit.getText().toString();
		// Send data to DisplayMessageActivity activity...
		intent.putExtra(EXTRA_MESSAGE, message);
		// Start new activity...
		startActivity(intent);
	}
}
