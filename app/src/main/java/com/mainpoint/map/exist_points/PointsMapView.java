package com.mainpoint.map.exist_points;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.mainpoint.R;
import com.mainpoint.map.MapEventListener;
import com.mainpoint.models.Point;
import com.mainpoint.utils.BitmapUtils;
import com.mainpoint.utils.PermissionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PointsMapView extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    private Map<Marker, Point> markersToPointMap;
    private Marker prevSelectedMarker;
    private Point selectedPoint;

    private MapEventListener mapEventListener;

    public PointsMapView() {
    }

    public static PointsMapView newInstance() {
        PointsMapView fragment = new PointsMapView();
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
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.view_points_list_map, container, false);
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

        if (getParentFragment() instanceof ExistingPointsMapFragment) {
            ((ExistingPointsMapFragment) getParentFragment()).loadPointsList();
        }

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
                    View layout = LayoutInflater.from(getActivity()).inflate(R.layout.transparency_map_info_window, null);
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
            if (getParentFragment() instanceof ExistingPointsMapFragment) {
                ((ExistingPointsMapFragment) getParentFragment()).loadPointsList();
            }
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
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
    }

    @Override
    public boolean onMarkerClick(Marker clickedMarker) {
        if (markersToPointMap != null && markersToPointMap.get(clickedMarker) != null) {
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

    public void setMapEventListener(MapEventListener mapEventListener) {
        this.mapEventListener = mapEventListener;
    }
}
