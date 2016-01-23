package com.andela.standingstill.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.andela.standingstill.R;
import com.andela.standingstill.Utility.ItemDivider;
import com.andela.standingstill.adapter.ActivityAdapter;
import com.andela.standingstill.dal.DataAccess;
import com.andela.standingstill.dal.SqliteDBHelper;
import com.andela.standingstill.model.Movement;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private final static String TAG = "Record Location";
    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private List<Movement> movements;
    private CollapsingToolbarLayout collapsingToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        collapsingToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolBar.setTitle(getString(R.string.timeline));
        collapsingToolBar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getMovements();
        initializeComponents();
    }

    public void initializeComponents() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.activity_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ActivityAdapter(this, movements);
        recyclerView.addItemDecoration(new ItemDivider(this));
        recyclerView.setAdapter(adapter);

    }

    public void getMovements() {
        movements = new ArrayList<>();
        SqliteDBHelper helper = new SqliteDBHelper(this);
        DataAccess access = new DataAccess(helper);
        movements = access.getByDate(DateTime.now(), null);

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
