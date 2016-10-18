package com.mainpoint.add_place;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.EditText;

import com.mainpoint.BaseActivity;
import com.mainpoint.R;
import com.mainpoint.models.Point;

import org.joda.time.LocalDateTime;

import java.util.Locale;

import io.realm.Realm;

public class AddPlaceActivity extends BaseActivity {

    public static final String PLACE_LATITUDE_KEY = "PLACE_LATITUDE_KEY";
    public static final String PLACE_LONGITUDE_KEY = "PLACE_LONGITUDE_KEY";

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        setNavigationArrow();

        LocalDateTime currentDate = new LocalDateTime();
        String defaultDate = getString(R.string.default_place_name)
                + " " + currentDate.dayOfWeek().getAsText(Locale.getDefault())
                + ", " + currentDate.toString("dd.MM.yyyy HH:mm");
        EditText nameEditText = (EditText) findViewById(R.id.name_place_edit_view);
        if (nameEditText != null) {
            nameEditText.setText(defaultDate);
        }

        Button saveButton = (Button) findViewById(R.id.save_place_button);
        saveButton.setOnClickListener(v -> {
            String name = null;
            if (nameEditText != null) {
                name = nameEditText.getText().toString();
            }
            double latitude = getIntent().getDoubleExtra(PLACE_LATITUDE_KEY, -1);
            double longitude = getIntent().getDoubleExtra(PLACE_LONGITUDE_KEY, -1);

            if (realm != null && name != null && !name.isEmpty() && latitude > 0 && longitude > 0) {
                EditText commentsEditText = (EditText) findViewById(R.id.comments_edit_text);
                String comments = commentsEditText.getText().toString();

                // increatement index
                long nextID = 0L;
                if (realm.where(Point.class).max("id") != null) {
                    nextID = realm.where(Point.class).max("id").longValue() + 1L;
                }

                Point point = new Point();
                point.setId(nextID);
                point.setName(name);
                point.setLatityde(latitude);
                point.setLongitude(longitude);
                point.setComments(comments);

                realm.executeTransactionAsync(newRealm -> {
                    newRealm.copyToRealmOrUpdate(point);
                }, () -> {
                    finish();
                }, error -> {
                    Snackbar.make(findViewById(R.id.activity_add_place), error.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    finish();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }
}
