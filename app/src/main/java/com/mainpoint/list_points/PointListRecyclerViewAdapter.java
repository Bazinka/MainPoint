package com.mainpoint.list_points;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.mainpoint.R;
import com.mainpoint.firebase_database.FirebaseRecyclerAdapter;
import com.mainpoint.models.Point;

public class PointListRecyclerViewAdapter extends FirebaseRecyclerAdapter<Point, PointListRecyclerViewAdapter.PointListViewHolder> {

    private OnPointsListEventListener listener;

    /**
     * @param ref      The Firebase location to watch for data changes. Can also be a slice of a location,
     *                 using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     * @param listener callback if event of click on item or data changes will happen.
     */
    public PointListRecyclerViewAdapter(Query ref, OnPointsListEventListener listener) {
        super(Point.class, ref);
        this.listener = listener;
    }

    @Override
    protected void populateViewHolder(PointListViewHolder holder, Point point, int position) {
        holder.point = point;
        holder.nameTextView.setText(point.getName());
        holder.contentTextView.setText(point.getComments());

        holder.mainView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.point);
            }
        });
    }

    @Override
    protected void onDataChanged() {
        if (listener != null) {
            listener.onDataChanged();
        }
    }

    @Override
    public PointListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_point, parent, false);
        return new PointListViewHolder(view);
    }

    public static class PointListViewHolder extends RecyclerView.ViewHolder {
        final View mainView;
        final TextView nameTextView;
        final TextView contentTextView;
        Point point;

        PointListViewHolder(View view) {
            super(view);
            mainView = view;
            nameTextView = (TextView) view.findViewById(R.id.name);
            contentTextView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentTextView.getText() + "'";
        }
    }

    public interface OnPointsListEventListener {
        void onItemClick(Point item);

        void onDataChanged();
    }
}
