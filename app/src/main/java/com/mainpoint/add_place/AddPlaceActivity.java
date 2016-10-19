package com.mainpoint.add_place;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mainpoint.BaseActivity;
import com.mainpoint.R;
import com.mainpoint.models.Point;

import org.joda.time.LocalDateTime;

import java.util.Locale;

import io.realm.Realm;

public class AddPlaceActivity extends BaseActivity {

    public static final String PLACE_LATITUDE_KEY = "PLACE_LATITUDE_KEY";
    public static final String PLACE_LONGITUDE_KEY = "PLACE_LONGITUDE_KEY";

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

            Realm realm = Realm.getDefaultInstance();

            if (realm != null && name != null && !name.isEmpty() && latitude > 0 && longitude > 0) {
                EditText commentsEditText = (EditText) findViewById(R.id.comments_edit_text);
                String comments = commentsEditText.getText().toString();

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
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                } finally {
                    realm.close();
                    finish();
                }
            }
        });
    }

}
