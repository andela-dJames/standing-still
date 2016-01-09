package checkpoint4.andela.com.standingstill;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;

public class LocationFragment extends AppCompatActivity {
    private final static String TAG = "Record Location";

    private GoogleLocationService googleLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        googleLocationService = new GoogleLocationService(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleLocationService.connect();
    }

    @Override
    protected void onStop() {

        if (googleLocationService.isConnected()){
            googleLocationService.disconnect();
        }
        super.onStop();
    }
}
