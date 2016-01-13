package checkpoint4.andela.com.standingstill;

import android.app.Activity;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by andeladev on 13/01/2016.
 */
public class Address {

    private Activity activity;
    private String country;

    public Address(Activity activity) {
        this.activity = activity;

    }

    public List<android.location.Address> getAddress(double latitude, double logitude) throws IOException {
        Geocoder geocoder = new Geocoder(activity);
        List<android.location.Address> addresses = null;
        return addresses = geocoder.getFromLocation(latitude, logitude, 1);

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
            Log.d("TAG", address.getCountryName());
          country =   address.getSubAdminArea();//+ ", "+ address.getAdminArea();
        }

        return country;
    }

}
