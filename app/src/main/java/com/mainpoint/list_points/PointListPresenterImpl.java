package com.mainpoint.list_points;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mainpoint.utils.DatabaseConstants;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class PointListPresenterImpl implements PointListPresenter {

    private PointListView mainView;
    private Context context;

    private DatabaseReference pointListRef;

    public PointListPresenterImpl(Context contex, PointListView mainView) {
        this.context = contex;
        this.mainView = mainView;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        pointListRef = ref.child(DatabaseConstants.DATABASE_POINT_TABLE);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public Query getItems() {
        Query lastFifty = pointListRef.limitToLast(50);
        return lastFifty;
    }
}
