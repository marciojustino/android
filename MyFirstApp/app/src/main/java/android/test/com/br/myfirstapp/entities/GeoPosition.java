package android.test.com.br.myfirstapp.entities;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marcio on 06/03/2015.
 */
public class GeoPosition implements Parcelable {
    private ArrayList<LatLng> mLatLong;
    private ArrayList<Address> mAddress;

    public GeoPosition() {
        mLatLong = new ArrayList<>();
        mAddress = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public void add(LatLng latLng) {
        mLatLong.add(latLng);
    }

    public void remove(LatLng latLng) {
        mLatLong.remove(latLng);
    }

    public int size() {
        return mLatLong.size();
    }

    public LatLng get(int index) {
        return mLatLong.get(index);
    }

    public void addAddress(Address address) {
        mAddress.add(address);
    }

    public Address getAddress(int index) {
        return mAddress.get(index);
    }
}
