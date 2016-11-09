package com.mainpoint.map.search_new_point;

import android.content.Context;

import io.realm.Realm;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class SearchNewPointMapPresenterImpl implements SearchNewPointMapPresenter {

    private Realm realm;
    private SearchNewPointMapView mainView;
    private Context context;

    public SearchNewPointMapPresenterImpl(Context contex, SearchNewPointMapView mainView) {
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
}
