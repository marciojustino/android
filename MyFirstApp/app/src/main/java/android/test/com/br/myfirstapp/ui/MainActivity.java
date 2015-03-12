package android.test.com.br.myfirstapp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.test.com.br.myfirstapp.R;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_map_settings:
                Toast.makeText(this, "Settings called", Toast.LENGTH_LONG).show();
                openMapSettingsActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openMapSettingsActivity() {
        // todo: change this activity to map's prefer settings - low priority
        try {
            Intent intent = new Intent(this, MapSettingsActivity.class);
            startActivity(intent);
        }
        catch (Exception e) {
            Log.e("activity_action", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void searchLocation(View view) {
        try {
            EditText text = (EditText) findViewById(R.id.local_name);
            String textLocation = text.getText().toString();

            Intent intent = new Intent(this, MapViewActivity.class);
            intent.putExtra(MapViewActivity.LOCATION_NAME, textLocation);
            intent.putExtra(MapViewActivity.FLAG_REQUEST_SEARCH, true);
            startActivity(intent);
        }
        catch (Exception e) {
            Log.d(this.getLocalClassName(), e.getMessage());
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
