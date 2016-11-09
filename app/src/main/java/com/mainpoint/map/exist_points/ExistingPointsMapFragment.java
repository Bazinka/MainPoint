package com.mainpoint.map.exist_points;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mainpoint.R;
import com.mainpoint.map.MapEventListener;
import com.mainpoint.models.Point;

import java.util.List;

public class ExistingPointsMapFragment extends Fragment implements ExistingPointsMapView, MapEventListener {

    private static final String SELECTED_POINT_KEY = "SELECTED_POINT_KEY";
    private ExistingPointsMapPresenter presenter;

    private BottomSheetBehavior behavior;
    private ViewGroup mainView;

    private Point selectedPoint;

    public ExistingPointsMapFragment() {
    }

    public static ExistingPointsMapFragment newInstance() {
        ExistingPointsMapFragment fragment = new ExistingPointsMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ExistingPointsMapFragment newInstance(Point point) {
        ExistingPointsMapFragment fragment = new ExistingPointsMapFragment();
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
        presenter = new ExistingPointsMapPresenterImpl(getActivity(), this);
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
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_existing_points_map, container, false);

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
        PointsMapView pointsMapView = (PointsMapView) getChildFragmentManager()
                .findFragmentById(R.id.points_map_view);
        if (pointsMapView != null) {
            pointsMapView.setMapEventListener(this);
            if (selectedPoint != null) {
//                onPointClick(selectedPoint);
                pointsMapView.setSelectedPoint(selectedPoint);
            }
        }
        return mainView;
    }

    @Override
    public void setListPointsToMap(List<Point> pointsList) {
        PointsMapView pointsMapView = (PointsMapView) getChildFragmentManager()
                .findFragmentById(R.id.points_map_view);
        if (pointsMapView != null) {
            pointsMapView.showPointsToMap(pointsList);
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
