package com.mainpoint.points_map;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface PointMapPresenter {

    public void onCreate();

    public void onDestroy();

//    List<Point>getPoints();

    void getPoints();
}
