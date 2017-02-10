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

public class PointListFragment extends Fragment implements PointListView {

    private PointListPresenter presenter;

    private OnPointsListClickListener listener;

    private RecyclerView pointListRecyclerView;
    private LinearLayoutManager manager;
    private PointListRecyclerViewAdapter adapter;
    private View emptyListView;

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

        emptyListView = view.findViewById(R.id.emptyTextView);

        Context context = view.getContext();
        pointListRecyclerView = (RecyclerView) view.findViewById(R.id.points_list_recyclerview);
        pointListRecyclerView.setHasFixedSize(false);

        manager = new LinearLayoutManager(context);
        pointListRecyclerView.setLayoutManager(manager);

        attachRecyclerViewAdapter();
        return view;
    }

    private void attachRecyclerViewAdapter() {

        adapter = new PointListRecyclerViewAdapter(presenter != null ? presenter.getItems() : null,
                new PointListRecyclerViewAdapter.OnPointsListEventListener() {
                    @Override
                    public void onItemClick(Point item) {
                        if (listener != null) {
                            listener.onClick(item);
                        }
                    }

                    @Override
                    public void onDataChanged() {
                        emptyListView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
                    }
                });

        // Scroll to bottom on new messages
//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                manager.smoothScrollToPosition(pointListRecyclerView, null, adapter.getItemCount());
//            }
//        });

        pointListRecyclerView.setAdapter(adapter);
    }


    public void setOnPointClickListener(OnPointsListClickListener _listener) {
        listener = _listener;
    }

    public interface OnPointsListClickListener {
        void onClick(Point item);
    }
}
