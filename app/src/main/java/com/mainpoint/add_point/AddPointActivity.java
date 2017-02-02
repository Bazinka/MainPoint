package com.mainpoint.add_point;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mainpoint.BaseActivity;
import com.mainpoint.R;

public class AddPointActivity extends BaseActivity implements AddPointView {

    public static final String PLACE_LATITUDE_KEY = "PLACE_LATITUDE_KEY";
    public static final String PLACE_LONGITUDE_KEY = "PLACE_LONGITUDE_KEY";
    public static final String PLACE_NAME_KEY = "PLACE_NAME_KEY";

    AddPointPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_point);
        setNavigationArrow();

        presenter = new AddPointPresenterImpl(this, this);

        String defaultName = presenter.getDefaultName();
        EditText nameEditText = (EditText) findViewById(R.id.name_place_edit_view);
        if (nameEditText != null) {
            nameEditText.setText(defaultName);
        }

        Button saveButton = (Button) findViewById(R.id.save_place_button);
        saveButton.setOnClickListener(v -> {
            String name = null;
            if (nameEditText != null) {
                name = nameEditText.getText().toString();
            }
            double latitude = getIntent().getDoubleExtra(PLACE_LATITUDE_KEY, -1);
            double longitude = getIntent().getDoubleExtra(PLACE_LONGITUDE_KEY, -1);
            EditText commentsEditText = (EditText) findViewById(R.id.comments_edit_text);
            String comments = commentsEditText.getText().toString();

            if (presenter != null) {
                presenter.savePoint(name, comments, latitude, longitude);
            }
        });
    }

    @Override
    public void setErrorSavePoint(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setSuccessSavePoint() {
        finish();
    }
}
