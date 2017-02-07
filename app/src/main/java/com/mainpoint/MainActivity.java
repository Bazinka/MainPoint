package com.mainpoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mainpoint.auth.FirebaseAuthActivity;
import com.mainpoint.list_points.PointListFragment;
import com.mainpoint.map.exist_points.ExistingPointsMapFragment;
import com.mainpoint.map.search_new_point.SearchNewPointActivity;
import com.mainpoint.models.Point;
import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int mSelectedNavigationId;
    private Menu mMenu;
    private View mRootView;

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

        mRootView = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        View headerView = navigationView.getHeaderView(0);
        if (auth != null && auth.getCurrentUser() != null) {
            FirebaseUser user = auth.getCurrentUser();
            if (user.getPhotoUrl() != null) {
                ImageView photoImageView = (ImageView) headerView.findViewById(R.id.user_photo_imageView);
                Picasso.with(this).load(user.getPhotoUrl().getPath()).placeholder(R.drawable.logo).into(photoImageView);
            }
            if (user.getDisplayName() != null) {
                TextView nameTextView = (TextView) headerView.findViewById(R.id.user_name_textView);
                nameTextView.setText(user.getDisplayName());
            }
        }

        navigationView.setNavigationItemSelectedListener(this);

        mSelectedNavigationId = R.id.nav_map;
        navigationView.setCheckedItem(mSelectedNavigationId);
        setPointsMapFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_point_fab);
        fab.setOnClickListener(view -> {
            startNewPointActivity();
        });
    }

    void startNewPointActivity() {
        Intent intent = new Intent(this, SearchNewPointActivity.class);
        startActivityWithLeftAnimation(intent);
    }

    void setPointsMapFragment(Point selectedPoint) {
        setTitle(getString(R.string.points_on_map_title));
        ExistingPointsMapFragment existingPointsMapFragment = ExistingPointsMapFragment.newInstance(selectedPoint);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, existingPointsMapFragment);
        transaction.commit();
    }

    void setPointsMapFragment() {
        setTitle(getString(R.string.points_on_map_title));
        ExistingPointsMapFragment existingPointsMapFragment = ExistingPointsMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, existingPointsMapFragment);
        transaction.commit();
    }

    void setPointsListFragment() {
        setTitle(getString(R.string.list_points_title));
        PointListFragment pointListFragment = PointListFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, pointListFragment);
        transaction.commit();
        pointListFragment.setOnPointClickListener(item -> {
            if (mMenu != null) {
                MenuItem menuItem = mMenu.findItem(R.id.action_points_list);
                if (menuItem != null) {
                    menuItem.setIcon(R.drawable.ic_list_points);
                }
            }
            mSelectedNavigationId = R.id.nav_map;
            setPointsMapFragment(item);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(mSelectedNavigationId);
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
        mMenu = _menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_points_list) {
            if (mSelectedNavigationId == R.id.nav_map) {
                setPointsListFragment();
                item.setIcon(R.drawable.ic_map_points);
                mSelectedNavigationId = R.id.nav_place_list;
            } else if (mSelectedNavigationId == R.id.nav_place_list) {
                setPointsMapFragment();
                item.setIcon(R.drawable.ic_list_points);
                mSelectedNavigationId = R.id.nav_map;
            }
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(mSelectedNavigationId);

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
        mSelectedNavigationId = item.getItemId();

        MenuItem upMenuItem = mMenu.findItem(R.id.action_points_list);
        if (mSelectedNavigationId == R.id.nav_map) {
            setPointsMapFragment();
            if (upMenuItem != null) {
                upMenuItem.setIcon(R.drawable.ic_list_points);
            }
        } else if (mSelectedNavigationId == R.id.nav_place_list) {
            setPointsListFragment();
            if (upMenuItem != null) {
                upMenuItem.setIcon(R.drawable.ic_map_points);
            }
        } else if (mSelectedNavigationId == R.id.nav_slideshow) {

        } else if (mSelectedNavigationId == R.id.nav_manage) {

        } else if (mSelectedNavigationId == R.id.nav_sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent in = new Intent();
                                in.setClass(MainActivity.this, FirebaseAuthActivity.class);
                                startActivityWithLeftAnimation(in);
                                finish();
                            } else {
                                Snackbar.make(mRootView, R.string.sign_out_failed, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
