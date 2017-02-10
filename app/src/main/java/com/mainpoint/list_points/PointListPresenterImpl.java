package com.mainpoint.list_points;

import android.content.Context;

import com.mainpoint.models.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class PointListPresenterImpl implements PointListPresenter {

    private PointListView mainView;
    private Context context;

    public PointListPresenterImpl(Context contex, PointListView mainView) {
        this.context = contex;
        this.mainView = mainView;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }
}
