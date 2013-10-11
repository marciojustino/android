package com.example.adroitest.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView textView;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(android.content.Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				String path = bundle.getString(DownloadService.FILEPATH);
				int resultCode = bundle.getInt(DownloadService.RESULT);

				if (resultCode == RESULT_OK) {
					Toast.makeText(MainActivity.this,
							"Download complete. Download URI: " + path,
							Toast.LENGTH_LONG).show();
					textView.setText("Download is done!");
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(MainActivity.this, path,
                            Toast.LENGTH_LONG).show();
                    textView.setText(path);
                } else {
                    Toast.makeText(MainActivity.this, "Download failed",
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");
                }
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Get status object of layout
		textView = (TextView) findViewById(R.id.status);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));
	}
	
	@Override
	protected void onPause() {		
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
	public void onClick(View view){
		// Start download server...
		Intent intent = new Intent(this, DownloadService.class);
		intent.putExtra(DownloadService.FILEPATH, "index.html");
		intent.putExtra(DownloadService.URL, "http://www.vogella.com/index.html");
		startService(intent);
		textView.setText("Service started");
	}
}
