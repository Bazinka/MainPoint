package com.mainpoint;

import android.app.Application;

import com.bettervectordrawable.VectorDrawableCompat;

/**
 * Created by DariaEfimova on 17.10.16.
 */

public class RApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        findAllVectorResourceIdsSlow();
    }

    private void findAllVectorResourceIdsSlow() {
        int[] ids = VectorDrawableCompat.findAllVectorResourceIdsSlow(getResources(), R.drawable.class);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);
    }

}
