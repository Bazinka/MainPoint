package com.mainpoint.points_map;

import android.content.Context;

import com.mainpoint.models.Point;
import com.mainpoint.points_list.PointListPresenter;
import com.mainpoint.points_list.PointListView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class PointMapPresenterImpl implements PointMapPresenter {

    private Realm realm;
    private PointMapView mainView;
    private Context context;

    public PointMapPresenterImpl(Context contex, PointMapView mainView) {
        this.context = contex;
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
