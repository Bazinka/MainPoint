package com.mainpoint.list_points;

import com.google.firebase.database.Query;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface PointListPresenter {

    public void onCreate();

    public void onDestroy();

    public Query getItems();
}
