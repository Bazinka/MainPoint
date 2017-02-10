package com.mainpoint.map.exist_points;

import android.content.Context;

import com.mainpoint.models.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class ExistingPointsMapPresenterImpl implements ExistingPointsMapPresenter {

    private ExistingPointsMapView mainView;

    public ExistingPointsMapPresenterImpl(Context contex, ExistingPointsMapView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void getPoints() {
        List<Point> points;
        points = new ArrayList<>();
        if (mainView != null) {
            mainView.setListPointsToMap(points);
        }
    }
}
