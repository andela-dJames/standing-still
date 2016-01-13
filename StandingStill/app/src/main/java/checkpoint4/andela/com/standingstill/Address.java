package checkpoint4.andela.com.standingstill;

import android.content.Context;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by andeladev on 13/01/2016.
 */
public class Address {

    private double logitude;
    private double latitude;
    private Context context;

    public Address() {
    }

    public Address(double logitude, double latitude, Context context) {
        this.logitude = logitude;
        this.latitude = latitude;
        this.context = context;
    }

    private List<android.location.Address> getAddress() throws IOException {
        Geocoder geocoder = new Geocoder(context);
        List<android.location.Address> addresses = null;
        return addresses = geocoder.getFromLocation(latitude, logitude, 1);

    }
}
