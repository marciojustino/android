package android.test.com.br.myfirstapp.intentservices;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.test.com.br.myfirstapp.constant.GoogleMapsServiceConstants;
import android.test.com.br.myfirstapp.entities.GeoPosition;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Marcio on 06/03/2015.
 */
public class FetchAddressIntentService extends IntentService {
    private final static int RETURN_ADDRESSES_LIMIT = 5;
    protected ResultReceiver mResultReceiver;
    protected GeoPosition mGeoPositions;

    public FetchAddressIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(getBaseContext());

        // Get the location passed to this service through an extra
        String locationName = intent.getStringExtra(GoogleMapsServiceConstants.LOCATION_NAME);
        mResultReceiver = intent.getParcelableExtra(GoogleMapsServiceConstants.RECEIVER);

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(locationName, FetchAddressIntentService.RETURN_ADDRESSES_LIMIT);

            //Get all positions found
            mGeoPositions = new GeoPosition();
            for (int i = 0; i < addresses.size(); i++) {
                Address address = addresses.get(i);
                mGeoPositions.add(new LatLng(address.getLatitude(), address.getLongitude()));
                mGeoPositions.addAddress(address);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            deliverLocationToReceiver(e.getMessage());
        }

        deliverLocationToReceiver(mGeoPositions);
    }

    private void deliverLocationToReceiver(GeoPosition geoPositions) {
        Bundle bundle = new Bundle();
        if (geoPositions != null) {
            bundle.putParcelable(GoogleMapsServiceConstants.ARRAY_LATITUDE_LONGITUDE_POSITION, geoPositions);
            mResultReceiver.send(GoogleMapsServiceConstants.SUCCESS, bundle);
        } else {
            bundle.putString(GoogleMapsServiceConstants.ERROR_MESSAGE, new String("Not found any location on map."));
            mResultReceiver.send(GoogleMapsServiceConstants.FAIL, bundle);
        }

    }

    private void deliverLocationToReceiver(String errorMessage) {
        Bundle bundle = new Bundle();
        bundle.putString(GoogleMapsServiceConstants.ERROR_MESSAGE, errorMessage);
        mResultReceiver.send(GoogleMapsServiceConstants.FAIL, bundle);
    }
}
