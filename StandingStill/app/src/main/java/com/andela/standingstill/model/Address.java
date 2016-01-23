package com.andela.standingstill.model;

import android.app.Activity;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

/**
 * A class that Represents an Address
 */
public class Address {

    private Activity activity;
    private String country;

    public Address(Activity activity) {
        this.activity = activity;
        country = "";

    }

    public List<android.location.Address> getAddress(double latitude, double logitude) throws IOException {
        Geocoder geocoder = new Geocoder(activity);
        return geocoder.getFromLocation(latitude, logitude, 1);

    }

    public String getCountryname(double latitude, double longitude) {
        List<android.location.Address> addressList= null;
        try {
            addressList = getAddress(latitude, longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addressList != null && addressList.size() > 0 ){

            android.location.Address address = addressList.get(0);

          country =   address.getThoroughfare() + ", "+ address.getSubLocality();
        }

        return country;
    }

}
