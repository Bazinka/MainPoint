package com.mainpoint;

import android.app.Application;

import com.bettervectordrawable.VectorDrawableCompat;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;

/**
 * Created by DariaEfimova on 17.10.16.
 */

public class RApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        JodaTimeAndroid.init(this);
        findAllVectorResourceIdsSlow();
    }

    private void findAllVectorResourceIdsSlow() {
        int[] ids = VectorDrawableCompat.findAllVectorResourceIdsSlow(getResources(), R.drawable.class);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);
    }

}
