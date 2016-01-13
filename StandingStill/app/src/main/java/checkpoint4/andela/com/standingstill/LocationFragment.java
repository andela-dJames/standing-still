package checkpoint4.andela.com.standingstill;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationFragment extends AppCompatActivity {
    private final static String TAG = "Record Location";

    private TextView view;
    private TextView latitiudeText;
    private GoogleApiClient googleApiClient;
    private double longitude;
    private double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            LocationFragmentFragment fragment = new LocationFragmentFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();

        }

        view = (TextView) findViewById(R.id.long_text);
        latitiudeText = (TextView) findViewById(R.id.lat_text);
        receiveIntent(savedInstanceState);

    }


    public void receiveIntent(Bundle bundle) {
        Intent intent = getIntent();
        String message = intent.getStringExtra("TIMESPENT");
        String user = intent.getStringExtra("LATITUDE");
        String activity = intent.getStringExtra("DO");
        TextView txt = new TextView(this);
        txt.setTextSize(40);
        txt.setText(message);
        view.setText(message);
        latitiudeText.setText(user+ " "+ activity);



    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }



}
