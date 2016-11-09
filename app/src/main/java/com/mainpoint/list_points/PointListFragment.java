package com.mainpoint.list_points;

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

import java.util.List;

public class PointListFragment extends Fragment implements PointListView {

    private PointListPresenter presenter;
    private PointListRecyclerViewAdapter adapter;
    private OnPointsListClickListener listener;

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
        setRetainInstance(true);
        presenter = new PointListPresenterImpl(getActivity(), this);
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
        View view = inflater.inflate(R.layout.fragment_point_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.points_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new PointListRecyclerViewAdapter();
        if (listener != null) {
            adapter.setClickListener(listener);
        }
        recyclerView.setAdapter(adapter);

        if (presenter != null) {
            presenter.getPoints();
        }
        return view;
    }

    @Override
    public void setListPoints(List<Point> pointsList) {
        if (adapter != null) {
            adapter.updatePointsList(pointsList);
        }
    }

    public void setOnPointClickListener(OnPointsListClickListener _listener) {
        listener = _listener;
        if (adapter != null) {
            adapter.setClickListener(_listener);
        }
    }


    public interface OnPointsListClickListener {
        void onClick(Point item);
    }
}
