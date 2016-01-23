package com.andela.standingstill.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.andela.standingstill.R;
import com.andela.standingstill.Utility.ItemDivider;
import com.andela.standingstill.Utility.Settings;
import com.andela.standingstill.adapter.LocationAdapter;
import com.andela.standingstill.dal.DataAccess;
import com.andela.standingstill.dal.SqliteDBHelper;
import com.andela.standingstill.model.Movement;
import com.andela.standingstill.model.Place;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShowActivityFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private TextView textView;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private List<Place> places;
    private List<Movement>movements;
    DateTime date;
    private SqliteDBHelper helper;
    private DataAccess access;
    private TextView titleText;

    public ShowActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                View v = inflater.inflate(R.layout.timeline_layout, container, false);
        getLocations();
        initialize(v);
        return v;
    }

    public void initialize(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.activity_items);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new LocationAdapter(places, getContext());
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        recyclerView.setAdapter(adapter);
        titleText = (TextView) v.findViewById(R.id.title_text);
    }

    public void getLocations() {
        Place place = new Place();
        place.setTimeSpent(300000);
        place.setAddress("2/3, Funsho Street");
        places = new ArrayList<>();
        //places.add(place);
        movements = new ArrayList<>();
       helper = new SqliteDBHelper(getContext());
        access = new DataAccess(helper);
        movements = access.getByDate(DateTime.now(), null);
        places = place.getPlaces(movements);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);

        movements = access.getByDate(date, null);
        if (Settings.isToday(date, DateTime.now())){
            titleText.setText(R.string.today);
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, MMMM d, y");
        titleText.setText(date.toString(formatter));


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_choose_date){
            date = DateTime.now();
            DatePickerFragment fragment = new DatePickerFragment();
            Bundle args = new Bundle();
            args.putString(DatePickerFragment.DEFAULT, date.toString());

            fragment.setArguments(args);

            fragment.setListener(this);

            fragment.show(getActivity().getFragmentManager(), "dialogpicker");

        }
        return true;

    }


}
