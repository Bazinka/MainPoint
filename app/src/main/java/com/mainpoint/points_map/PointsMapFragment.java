package com.mainpoint.points_map;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mainpoint.R;
import com.mainpoint.models.Point;

import java.util.List;

public class PointsMapFragment extends Fragment implements PointMapView, MapEventListener {

    private PointMapPresenter presenter;

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
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_points_map, container, false);
        MapViewFragment mapViewFragment = (MapViewFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapViewFragment != null) {
            mapViewFragment.setMapEventListener(this);
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
        Toast.makeText(getActivity(), "onPointClick on point " + point.getName(), Toast.LENGTH_LONG).show();
    }
}
