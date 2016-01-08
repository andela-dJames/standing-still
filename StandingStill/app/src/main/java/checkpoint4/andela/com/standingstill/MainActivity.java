package checkpoint4.andela.com.standingstill;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "Main Activity";

    private TextView hourDisplay;
    private TextView minuteDisplay;
    private TextView secondsDisplay;
    private boolean isRecording;
    private FloatingActionButton fab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    private void initializeComponents() {
        hourDisplay = (TextView) findViewById(R.id.hourText);
        minuteDisplay = (TextView) findViewById(R.id.minuteText);
        secondsDisplay = (TextView) findViewById(R.id.secondText);

        isRecording = false;
    }

    public void startRecording(View v) {
        changeIcon();

    }
    

    private void changeIcon() {
        if (!isRecording){
            fab.setImageResource(R.drawable.ic_stop_button);
            isRecording = true;
        }
        else {
            fab.setImageResource(R.drawable.ic_stat_name);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
