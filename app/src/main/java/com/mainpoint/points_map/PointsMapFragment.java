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
import android.widget.TextView;

import com.mainpoint.R;
import com.mainpoint.models.Point;

import java.util.List;

public class PointsMapFragment extends Fragment implements PointMapView, MapEventListener {

    private static final String SELECTED_POINT_KEY = "SELECTED_POINT_KEY";
    private PointMapPresenter presenter;

    private BottomSheetBehavior behavior;
    private ViewGroup mainView;

    private Point selectedPoint;

    public PointsMapFragment() {
    }

    public static PointsMapFragment newInstance() {
        PointsMapFragment fragment = new PointsMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static PointsMapFragment newInstance(Point point) {
        PointsMapFragment fragment = new PointsMapFragment();
        Bundle args = new Bundle();
        if (point != null) {
            args.putSerializable(SELECTED_POINT_KEY, point);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            selectedPoint = (Point) getArguments().getSerializable(SELECTED_POINT_KEY);
        }
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
        MapViewFragment mapViewFragment = (MapViewFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapViewFragment != null) {
            mapViewFragment.setMapEventListener(this);
            if (selectedPoint != null) {
//                onPointClick(selectedPoint);
                mapViewFragment.setSelectedPoint(selectedPoint);
            }
        }
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
        if (point != null) {
            View bottomSheet = mainView.findViewById(R.id.map_bottom_sheet);
            bottomSheet.setVisibility(View.VISIBLE);
//        bottomSheet.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            TextView nameTextView = (TextView) bottomSheet.findViewById(R.id.name_place_text_view);
            nameTextView.setText(point.getName());

            TextView descTextView = (TextView) bottomSheet.findViewById(R.id.desc_place_text_view);
            descTextView.setText(point.getComments());

            selectedPoint = null;
        }
    }
}
