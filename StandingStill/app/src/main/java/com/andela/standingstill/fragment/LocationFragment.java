package com.andela.standingstill.fragment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andela.standingstill.R;
import com.andela.standingstill.adapter.ActivityAdapter;
import com.andela.standingstill.model.Movement;
import com.google.android.gms.common.api.GoogleApiClient;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


public class LocationFragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private final static String TAG = "Record Location";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private List<Movement> movements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_activity_drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_location);
//        //navigationView.setNavigationItemSelectedListener(this);
//
//        if (findViewById(R.id.fragment_container) != null) {
//
//            if (savedInstanceState != null) {
//                return;
//            }
//            ShowActivityFragment fragment = new ShowActivityFragment();
//            fragment.setArguments(getIntent().getExtras());
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, fragment)
//                    .commit();

        //}
//        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(getString(R.string.app_name));
//
////        view = (TextView) findViewById(R.id.long_text);
////        latitiudeText = (TextView) findViewById(R.id.lat_text);
//        recyclerView = (RecyclerView) findViewById(R.id.activity_items);
//        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                Paint paint = new Paint();
//                paint.setColor(getResources().getColor(R.color.divider_color));
//                paint.setStrokeWidth(1.0f);
//
//                float startX = parent.getLeft();
//
//                for (int i = 0, count = parent.getChildCount(); i < count; ++i) {
//                    View child = parent.getChildAt(i);
//
//                    float startY = child.getBottom();
//                    float stopX = child.getRight();
//
//                    c.drawLine(startX, startY, stopX, startY, paint);
//                }
//            }
//        });
//        registerForContextMenu(recyclerView);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        movements = new ArrayList<>();
//
//        adapter = new ActivityAdapter(this, movements);
//        recyclerView.setAdapter(adapter);
//        getMovements();
//
//        //receiveIntent(savedInstanceState);

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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
