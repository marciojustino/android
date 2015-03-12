package android.test.com.br.myfirstapp.ui;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.test.com.br.myfirstapp.R;
import android.test.com.br.myfirstapp.constant.GoogleMapsServiceConstants;
import android.test.com.br.myfirstapp.entities.GeoPosition;
import android.test.com.br.myfirstapp.errorhandle.ErrorConstants;
import android.test.com.br.myfirstapp.intentservices.FetchAddressIntentService;
import android.test.com.br.myfirstapp.loghandle.LogConstants;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public final static String LOCATION_NAME = "LOCATION_NAME";
    public final static String FLAG_REQUEST_SEARCH = "FLAG_REQUEST_SEARCH";
    private final static int MAP_ZOOM = 10;
    private final static int CAMERA_ANIMATION_SPEED = 5000; // 5 seconds, in milliseconds

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private String uiSearchLocation;
    private GeoPosition mGeoPositions;
    private LocationResultReceiver mReceiver;
    private boolean mRequestedSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        setUpMapIfNeeded();

        // Enable back button on icon view
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(15 * 1000)        // 15 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mReceiver = new LocationResultReceiver(new Handler());

        mRequestedSearch = getIntent().getBooleanExtra(MapViewActivity.FLAG_REQUEST_SEARCH, false);
        uiSearchLocation = getIntent().getStringExtra(MapViewActivity.LOCATION_NAME);
        Toast.makeText(this, "Searching for " + uiSearchLocation, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            setUpMapIfNeeded();
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getLocalClassName(), e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getLocalClassName(), e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            // Exit if came of onResume
            if (!mRequestedSearch)
                return;

            Log.i(this.getLocalClassName(), "Connected");

            if (!uiSearchLocation.isEmpty()) {
                searchLocationByName();
            } else {
                Toast.makeText(this, "Invalid local entry. Searching for local position...", Toast.LENGTH_LONG);
                mLastLocation = getLastLocation();
                if (mLastLocation != null) {
                    // Check if geocoder is present
                    if (!Geocoder.isPresent()) {
                        Toast.makeText(getBaseContext(), "Geocoder unavaliable.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    handleNewLocation(mLastLocation, "Now");
                } else {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        try {
//            Toast.makeText(this, "Connection has been suspended", Toast.LENGTH_SHORT).show();
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getLocalClassName(), e.getMessage());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, ErrorConstants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(this.getLocalClassName(), "Location services connection failed with code " + connectionResult.getErrorCode());
        }
        Toast.makeText(this, "Connection fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        // log new location founded in background
        handleNewLocation(location, "I am here!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Show the menu options using menu_main.xml layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mapview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i("map_menu", "Selected item: " + id);

        switch (item.getItemId()) {
            case R.id.action_menu_map_activity_buttonSearch:
                // todo: Open new field to search
                break;

            case android.R.id.home:
                Log.i("map_menu", "Home id pressed!");
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapViewActivity}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        try {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap();
                }
            }
        }
        catch (Exception e) {
            Log.e(this.getLocalClassName(), e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
    }


    private void searchLocationByName() {
        // Try locate UI entry
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            startLocationService(uiSearchLocation);
    }


    private void handleNewLocation(Location location, String markTitle) {
        if (location == null) {
            Toast.makeText(this, "No gps signal", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(LogConstants.TAG_INFO, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(markTitle);
        mMap.addMarker(options);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapViewActivity.MAP_ZOOM),
                MapViewActivity.CAMERA_ANIMATION_SPEED, null);

        // Search finished
        mRequestedSearch = false;
    }


    private void startLocationService(String uiSearchLocation) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(GoogleMapsServiceConstants.LOCATION_NAME, uiSearchLocation);
        intent.putExtra(GoogleMapsServiceConstants.RECEIVER, mReceiver);
        intent.putExtra(GoogleMapsServiceConstants.ARRAY_LATITUDE_LONGITUDE_POSITION, mGeoPositions);
        startService(intent);
    }


    private Location getLastLocation() {
        mMap.clear();
        if (mGoogleApiClient.isConnected()) {
            Log.d(this.getLocalClassName(), "Get last location");
            if (mLastLocation == null)
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        return mLastLocation;
    }


    class LocationResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public LocationResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == GoogleMapsServiceConstants.SUCCESS) {
                mGeoPositions = resultData.getParcelable(GoogleMapsServiceConstants.ARRAY_LATITUDE_LONGITUDE_POSITION);
                if (mGeoPositions != null) {
                    for (int i = 0; i < mGeoPositions.size(); i++) {
                        LatLng latLng = mGeoPositions.get(i);
                        Address address = mGeoPositions.getAddress(i);

                        Log.d(this.toString(), address.toString());

                        String addressText = String.format("%s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                                address.getCountryName());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(addressText);
                        mMap.addMarker(markerOptions);

                        //Position on first location
                        if (i == 0) {
                            Location loc = new Location("");
                            loc.setLatitude(latLng.latitude);
                            loc.setLongitude(latLng.longitude);
                            handleNewLocation(loc, addressText);
                        }
                    }
                }
            } else {
                String errorMessage = resultData.getString(GoogleMapsServiceConstants.ERROR_MESSAGE);
                Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}
