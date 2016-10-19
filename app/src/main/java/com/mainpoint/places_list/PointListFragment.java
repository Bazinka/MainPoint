package com.mainpoint.places_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mainpoint.R;
import com.mainpoint.models.Point;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class PointListFragment extends Fragment {

    private OnPointsListClickListener mListener;


    private Realm realm;

    public PointListFragment() {
    }

    public static PointListFragment newInstance() {
        PointListFragment fragment = new PointListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.points_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<Point> points;
        if (realm != null && !realm.isClosed()) {
            points = new ArrayList<>(realm.where(Point.class).findAll());
        } else {
            points = new ArrayList<>();
        }
        recyclerView.setAdapter(new PointListRecyclerViewAdapter(points, mListener));

        return view;
    }


    public interface OnPointsListClickListener {
        void onClick(Point item);
    }
}
