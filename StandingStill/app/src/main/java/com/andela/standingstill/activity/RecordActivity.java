package com.andela.standingstill.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.andela.standingstill.R;
import com.andela.standingstill.Utility.ItemDivider;
import com.andela.standingstill.Utility.Settings;
import com.andela.standingstill.adapter.ActivityAdapter;
import com.andela.standingstill.dal.DataAccess;
import com.andela.standingstill.dal.DataCollection;
import com.andela.standingstill.dal.SqliteDBHelper;
import com.andela.standingstill.fragment.DatePickerFragment;
import com.andela.standingstill.model.Movement;
import com.andela.standingstill.model.Place;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private final static String TAG = "Record Location";
    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private List<Movement> movements;
    private CollapsingToolbarLayout collapsingToolBar;
    private DateTime date;
    private DataAccess access;
    private TextView titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        collapsingToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolBar.setTitle(getString(R.string.timeline));
        collapsingToolBar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getMovements();
        initializeComponents();
    }

    /**
     * Initialize components
     */
    public void initializeComponents() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.activity_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ActivityAdapter(this, movements);
        recyclerView.addItemDecoration(new ItemDivider(this));
        recyclerView.setAdapter(adapter);
        titleText = (TextView) findViewById(R.id.head_text);

    }

    /**
     * Get movements by date
     */
    public void getMovements() {
        movements = new ArrayList<>();
        SqliteDBHelper helper = new SqliteDBHelper(this);
        access = new DataAccess(helper);
        movements = access.getByDate(DateTime.now(), null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, PreferenceSettingsActivity.class);
            startActivity(settingsIntent);
        }
        if (id == R.id.movements_fragment_action_choose_day){
            date = DateTime.now();
            DatePickerFragment fragment = new DatePickerFragment();
            Bundle args = new Bundle();
            args.putString(DatePickerFragment.DEFAULT, date.toString());

            fragment.setArguments(args);

            fragment.setListener(this);

            fragment.show(getFragmentManager(), "dialogpicker");
        }

        if (id == android.R.id.home){
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)){
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            }
            else {
                NavUtils.navigateUpTo(this, upIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        Log.d("TAG", date.toString());

        movements = access.getByDate(date, null);
        if (Settings.isToday(date, DateTime.now())){
            titleText.setText(R.string.today);
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, MMMM d, y");
        titleText.setText(date.toString(formatter));
        movements.clear();
        List<Movement> newMovements = access.getByDate(date, null);
        adapter.notifyDataSetChanged();
        for (Movement movment : newMovements){
            int index = findIndex(movment);
            if (index < 0){
                movements.add(movment);
                adapter.notifyItemInserted(movements.size() -1 );
            }
            else {
                movements.set(index, movment);
                adapter.notifyItemChanged(index);
            }

        }

    }

    /**
     * Return the index of an element in an array
     * @param movement
     * @return the index
     */
    private int findIndex(Movement movement) {
        for (int i = 0; i < movements.size()-1; i++) {
            if (movement.getID() == (movements.get(i).getID())){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(1);
    }
}
