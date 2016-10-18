package com.mainpoint.places_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mainpoint.R;
import com.mainpoint.models.Point;

import java.util.List;

public class PointListRecyclerViewAdapter extends RecyclerView.Adapter<PointListRecyclerViewAdapter.ViewHolder> {

    private final List<Point> points;
    private final PointListFragment.OnPointsListClickListener listener;

    public PointListRecyclerViewAdapter(List<Point> _points, PointListFragment.OnPointsListClickListener _listener) {
        points = _points;
        listener = _listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_point, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.point = points.get(position);
        holder.nameTextView.setText(points.get(position).getName());
        holder.contentTextView.setText(points.get(position).getComments());

        holder.mainView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(holder.point);
            }
        });
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mainView;
        final TextView nameTextView;
        final TextView contentTextView;
        Point point;

        ViewHolder(View view) {
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
}
