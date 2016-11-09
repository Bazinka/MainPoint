package com.mainpoint.map.exist_points;

import android.content.Context;

import com.mainpoint.models.Point;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class ExistingPointsMapPresenterImpl implements ExistingPointsMapPresenter {

    private Realm realm;
    private ExistingPointsMapView mainView;

    public ExistingPointsMapPresenterImpl(Context contex, ExistingPointsMapView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCreate() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {

        realm.close();
    }

    @Override
    public void getPoints() {
        List<Point> points;
        if (realm != null && !realm.isClosed()) {
            points = new ArrayList<>(realm.where(Point.class).findAll());
        } else {
            points = new ArrayList<>();
        }
        if (mainView != null) {
            mainView.setListPointsToMap(points);
        }
    }
}
