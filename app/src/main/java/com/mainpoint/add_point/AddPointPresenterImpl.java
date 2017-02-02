package com.mainpoint.add_point;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.mainpoint.R;
import com.mainpoint.models.Point;

import org.joda.time.LocalDateTime;

import java.util.Locale;

import io.realm.Realm;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class AddPointPresenterImpl implements AddPointPresenter {

    private AddPointView mainView;
    private Context context;

    public AddPointPresenterImpl(Context contex, AddPointView mainView) {
        this.context = contex;
        this.mainView = mainView;
    }

    @Override
    public String getDefaultName() {
        LocalDateTime currentDate = new LocalDateTime();
        String defaultName = context.getString(R.string.default_place_name)
                + " " + currentDate.dayOfWeek().getAsText(Locale.getDefault())
                + ", " + currentDate.toString("dd.MM.yyyy HH:mm");
        if (context instanceof AppCompatActivity) {
            String name = ((AppCompatActivity) context).getIntent().getStringExtra(
                    AddPointActivity.PLACE_NAME_KEY);
            if (name != null) {
                defaultName = name;
            }
        }
        return defaultName;
    }

    @Override
    public void savePoint(String name, String comments, double latitude, double longitude) {
        Realm realm = Realm.getDefaultInstance();

        if (realm != null && name != null && !name.isEmpty()) {
            boolean isSuccess = true;
            try {
                realm.beginTransaction();
                // increatement index
                long nextID = 1;
                if (realm.where(Point.class).max("id") != null) {
                    nextID = realm.where(Point.class).max("id").longValue() + 1L;
                }
                Point point = realm.createObject(Point.class, nextID);
                point.setName(name);
                point.setLatityde(latitude);
                point.setLongitude(longitude);
                point.setComments(comments);
                realm.commitTransaction();
            } catch (Exception e) {
                isSuccess = false;
                if (mainView != null) {
                    mainView.setErrorSavePoint("Something went wrong");
                }
            } finally {
                realm.close();
            }

            if (isSuccess && mainView != null) {
                mainView.setSuccessSavePoint();
            }
        }
    }
}
