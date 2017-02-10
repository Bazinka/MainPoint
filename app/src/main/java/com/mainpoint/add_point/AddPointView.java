package com.mainpoint.add_point;

import android.app.Activity;

import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface AddPointView {

    void setErrorToast(String error);

    void setSuccessSavePoint();

    Activity getActivity();

    void setSuccessAddPhoto(List<StorageReference> listImageRef);

    void setProgressBarVisability(int visability);
}
