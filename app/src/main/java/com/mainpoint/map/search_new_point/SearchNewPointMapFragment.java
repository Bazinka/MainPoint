package com.mainpoint.map.search_new_point;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mainpoint.R;
import com.mainpoint.map.MapEventListener;
import com.mainpoint.models.Point;

public class SearchNewPointMapFragment extends Fragment implements SearchNewPointMapView, MapEventListener {

    private SearchNewPointMapPresenter presenter;

    private BottomSheetBehavior behavior;
    private ViewGroup mainView;

    public SearchNewPointMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new SearchNewPointMapPresenterImpl(getActivity(), this);
        presenter.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_search_new_point_on_map, container, false);

        View bottomSheet = mainView.findViewById(R.id.new_point_map_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });

        NewPointMapView newPointMapView = (NewPointMapView) getChildFragmentManager()
                .findFragmentById(R.id.new_point_map);
        if (newPointMapView != null) {
            newPointMapView.setMapEventListener(this);
        }
        return mainView;
    }

    @Override
    public void onPointClick(Point point) {
        if (point != null) {
            View bottomSheet = mainView.findViewById(R.id.new_point_map_bottom_sheet);
            bottomSheet.setVisibility(View.VISIBLE);
//        bottomSheet.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            TextView nameTextView = (TextView) bottomSheet.findViewById(R.id.new_point_name_place_text_view);
            nameTextView.setText(point.getName());

            TextView descTextView = (TextView) bottomSheet.findViewById(R.id.new_point_desc_place_text_view);
            descTextView.setText(point.getComments());
        }
    }
}
