package com.mainpoint.add_point;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface AddPointPresenter {

    String getDefaultName();

    void savePoint(String name, String comments, double latitude, double longitude);
}
