package com.mainpoint.points_map;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mainpoint.R;
import com.mainpoint.models.Point;

import java.util.List;

public class PointsMapFragment extends Fragment implements PointMapView, MapEventListener {

    private PointMapPresenter presenter;

    private BottomSheetBehavior behavior;
    private ViewGroup mainView;

    public PointsMapFragment() {
    }

    public static PointsMapFragment newInstance() {
        PointsMapFragment fragment = new PointsMapFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new PointMapPresenterImpl(getActivity(), this);
        presenter.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_points_map, container, false);

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        MapViewFragment mapViewFragment = (MapViewFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapViewFragment != null) {
            mapViewFragment.setMapEventListener(this);
        }
        View bottomSheet = mainView.findViewById(R.id.map_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });
        return mainView;
    }

    @Override
    public void setListPointsToMap(List<Point> pointsList) {
        MapViewFragment mapViewFragment = (MapViewFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapViewFragment != null) {
            mapViewFragment.showPointsToMap(pointsList);
        }
    }

    public void loadPointsList() {
        if (presenter != null) {
            presenter.getPoints();
        }
    }

    @Override
    public void onPointClick(Point point) {
        Toast.makeText(getActivity(), "onPointClick on point " + point.getName(), Toast.LENGTH_LONG).show();
        View bottomSheet = mainView.findViewById(R.id.map_bottom_sheet);
        bottomSheet.setVisibility(View.VISIBLE);
//        bottomSheet.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
