package com.mainpoint.list_points;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface PointListPresenter {

    public void onCreate();

    public void onDestroy();

//    List<Point>getPoints();

    void getPoints();
}
