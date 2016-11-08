package com.mainpoint.models;

import android.support.annotation.NonNull;

import org.joda.time.LocalDateTime;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by DariaEfimova on 17.10.16.
 */

public class Point extends RealmObject implements Serializable {

    @PrimaryKey
    private long id;
    private String name;
    private String comments;

    @Ignore
    private LocalDateTime jodaDateCreated;
    private long dateCreatedMillisek;

    private double latityde;
    private double longitude;

    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getComments() {
        return comments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getLatityde() {
        return latityde;
    }

    public void setLatityde(double latityde) {
        this.latityde = latityde;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getJodaDateCreated() {
        LocalDateTime jodaDateCreated = new LocalDateTime(dateCreatedMillisek);
        return jodaDateCreated;
    }

    public void setJodaDateCreated(LocalDateTime _jodaDateCreated) {
        if (_jodaDateCreated != null) {
            jodaDateCreated = _jodaDateCreated;
            this.dateCreatedMillisek = _jodaDateCreated.getMillisOfSecond();
        }
    }
}
