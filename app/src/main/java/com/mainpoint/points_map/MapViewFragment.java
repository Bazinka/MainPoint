package com.mainpoint.points_map;

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
import com.mainpoint.add_place.AddPointActivity;
import com.mainpoint.models.Point;
import com.mainpoint.utils.BitmapUtils;
import com.mainpoint.utils.PermissionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapViewFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    private Map<Marker, Point> markersToPointMap;
    private Marker newPointMarker;
    private Marker prevSelectedMarker;
    private Point selectedPoint;

    private MapEventListener mapEventListener;

    public MapViewFragment() {
    }

    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
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
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return mainView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (getParentFragment() instanceof PointsMapFragment) {
            ((PointsMapFragment) getParentFragment()).loadPointsList();
        }

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
                    View layout;
                    Point point = markersToPointMap.get(marker);
                    if (point == null) {
                        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.TransparentBackground);
                        LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                        layout = inflater.inflate(R.layout.add_point_map_info_window, null);
                    } else {
                        layout = LayoutInflater.from(getActivity()).inflate(R.layout.transparency_map_info_window, null);
                    }
                    return layout;
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
            if (getParentFragment() instanceof PointsMapFragment) {
                ((PointsMapFragment) getParentFragment()).loadPointsList();
            }
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (newPointMarker != null) {
            newPointMarker.remove();
        }

        if (prevSelectedMarker != null) {
            prevSelectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_pin)));
            prevSelectedMarker = null;
        }
        MarkerOptions markerOption = new MarkerOptions()
                .position(latLng)
                .title("Сохранить место?")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_selected_pin)));

        newPointMarker = mMap.addMarker(markerOption);
        newPointMarker.showInfoWindow();
    }

    public void showPointsToMap(List<Point> points) {
        if (mMap != null) {
            if (markersToPointMap == null) {
                markersToPointMap = new HashMap<>();
            } else {
                markersToPointMap.clear();
            }

            mMap.clear();

            for (Point point : points) {
                LatLng latLng = new LatLng(point.getLatityde(), point.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(point.getName())
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_pin)));
                Marker marker = mMap.addMarker(markerOptions);
                markersToPointMap.put(marker, point);
            }
            showSelectedPoint();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        startAddPointActivity(marker);
    }

    @Override
    public boolean onMarkerClick(Marker clickedMarker) {
        if (markersToPointMap != null && markersToPointMap.get(clickedMarker) != null) {
            if (newPointMarker != null) {
                newPointMarker.remove();
                newPointMarker = null;
            }
            if (prevSelectedMarker != null) {
                prevSelectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_pin)));
            }
            prevSelectedMarker = clickedMarker;
            prevSelectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_selected_pin)));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(prevSelectedMarker.getPosition(), 15);
            mMap.animateCamera(cameraUpdate);

            Point point = markersToPointMap.get(clickedMarker);
            if (mapEventListener != null) {
                mapEventListener.onPointClick(point);
            }
        } else {
            startAddPointActivity(newPointMarker);
        }
        return false;
    }

    public void setSelectedPoint(Point point) {
        selectedPoint = point;
    }

    public void showSelectedPoint() {
        if (selectedPoint != null && markersToPointMap != null) {
            for (Map.Entry<Marker, Point> entry : markersToPointMap.entrySet()) {
                if (entry.getValue().getId() == selectedPoint.getId()) {
                    onMarkerClick(entry.getKey());
                }
            }
        }
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
