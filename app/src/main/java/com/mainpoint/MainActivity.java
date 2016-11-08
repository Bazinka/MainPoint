package com.mainpoint;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mainpoint.models.Point;
import com.mainpoint.points_list.PointListFragment;
import com.mainpoint.points_map.PointsMapFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int selectedNavigationId;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        selectedNavigationId = R.id.nav_map;
        navigationView.setCheckedItem(selectedNavigationId);
        setPointsMapFragment();
    }

    void setPointsMapFragment(Point selectedPoint) {
        setTitle(getString(R.string.points_on_map_title));
        PointsMapFragment pointsMapFragment = PointsMapFragment.newInstance(selectedPoint);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, pointsMapFragment);
        transaction.commit();
    }

    void setPointsMapFragment() {
        setTitle(getString(R.string.points_on_map_title));
        PointsMapFragment pointsMapFragment = PointsMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, pointsMapFragment);
        transaction.commit();
    }

    void setPointsListFragment() {
        setTitle(getString(R.string.list_points_title));
        PointListFragment pointListFragment = PointListFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, pointListFragment);
        transaction.commit();
        pointListFragment.setOnPointClickListener(item -> {
            if (menu != null) {
                MenuItem menuItem = menu.findItem(R.id.action_points_list);
                if (menuItem != null) {
                    menuItem.setIcon(R.drawable.ic_list_points);
                }
            }
            selectedNavigationId = R.id.nav_map;
            setPointsMapFragment(item);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(selectedNavigationId);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.main, _menu);
        menu = _menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_points_list) {
            if (selectedNavigationId == R.id.nav_map) {
                setPointsListFragment();
                item.setIcon(R.drawable.ic_map_points);
                selectedNavigationId = R.id.nav_place_list;
            } else if (selectedNavigationId == R.id.nav_place_list) {
                setPointsMapFragment();
                item.setIcon(R.drawable.ic_list_points);
                selectedNavigationId = R.id.nav_map;
            }
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(selectedNavigationId);

            return true;
        }
//        else if (id == R.id.app_bar_search) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        selectedNavigationId = item.getItemId();

        MenuItem upMenuItem = menu.findItem(R.id.action_points_list);
        if (selectedNavigationId == R.id.nav_map) {
            setPointsMapFragment();
            if (upMenuItem != null) {
                upMenuItem.setIcon(R.drawable.ic_list_points);
            }
        } else if (selectedNavigationId == R.id.nav_place_list) {
            setPointsListFragment();
            if (upMenuItem != null) {
                upMenuItem.setIcon(R.drawable.ic_map_points);
            }
        } else if (selectedNavigationId == R.id.nav_slideshow) {

        } else if (selectedNavigationId == R.id.nav_manage) {

        } else if (selectedNavigationId == R.id.nav_share) {

        } else if (selectedNavigationId == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
