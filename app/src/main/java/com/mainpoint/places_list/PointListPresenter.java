package com.mainpoint.places_list;

import com.mainpoint.models.Point;

import java.util.List;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface PointListPresenter {

    public void onCreate();

    public void onDestroy();

//    List<Point>getPoints();

    void getPoints();
}
