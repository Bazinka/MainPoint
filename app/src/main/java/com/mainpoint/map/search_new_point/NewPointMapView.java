package com.mainpoint.map.search_new_point;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mainpoint.BaseActivity;
import com.mainpoint.R;
import com.mainpoint.add_point.AddPointActivity;
import com.mainpoint.map.MapEventListener;
import com.mainpoint.models.Point;
import com.mainpoint.utils.BitmapUtils;
import com.mainpoint.utils.PermissionUtils;


public class NewPointMapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    private Marker newPointMarker;

    private Point selectedPoint;

    private MapEventListener mapEventListener;
    private ViewGroup mainView;

    public NewPointMapView() {
    }

    public static NewPointMapView newInstance() {
        NewPointMapView fragment = new NewPointMapView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup mainView =  (ViewGroup) inflater.inflate(R.layout.fragment_new_points_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            // Get the button view
            View locationButton = ((View) mapFragment.getView().findViewById(1).getParent()).findViewById(2);

            // and next place it, for exemple, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            params.setMargins(0, 0, 20, 20);
            locationButton.setLayoutParams(params);
        }


        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                showNewPoint(place.getLatLng());
            }

            @Override
            public void onError(Status status) {

            }
        });

        return mainView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermissionFromFragment(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.TransparentBackground);
                    LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    return inflater.inflate(R.layout.add_point_map_info_window, null);
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null && selectedPoint == null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
        if (mMap != null) {
            if (newPointMarker != null) {
                newPointMarker.remove();
                newPointMarker = null;
            }
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showNewPoint(latLng);
    }

    private void showNewPoint(LatLng latLng) {
        if (newPointMarker != null) {
            newPointMarker.remove();
        }

        MarkerOptions markerOption = new MarkerOptions()
                .position(latLng)
                .title("Сохранить место?")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_selected_pin)));

        newPointMarker = mMap.addMarker(markerOption);
        newPointMarker.showInfoWindow();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        startAddPointActivity(marker);
    }

    @Override
    public boolean onMarkerClick(Marker clickedMarker) {
        startAddPointActivity(newPointMarker);
        return false;
    }

    void startAddPointActivity(Marker marker) {
        Intent intent = new Intent(getActivity(), AddPointActivity.class);
        intent.putExtra(AddPointActivity.PLACE_LATITUDE_KEY, marker.getPosition().latitude);
        intent.putExtra(AddPointActivity.PLACE_LONGITUDE_KEY, marker.getPosition().longitude);
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivityWithUpAnimation(intent);
        }
    }

    public void setMapEventListener(MapEventListener mapEventListener) {
        this.mapEventListener = mapEventListener;
    }
}
