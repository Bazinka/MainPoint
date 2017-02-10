package com.mainpoint.map.exist_points;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mainpoint.R;

import java.util.List;

/**
 * Created by DariaEfimova on 10.02.17.
 */

public class PhotoListRecyclerAdapter extends RecyclerView.Adapter<PhotoListRecyclerAdapter.ViewHolder> {

    private List<String> photoUrls;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView photoImageView;

        public ViewHolder(View v) {
            super(v);
            photoImageView = (ImageView) v.findViewById(R.id.point_photo_image_view);
        }
    }

    public PhotoListRecyclerAdapter(Context _context, List<String> _urls) {
        photoUrls = _urls;
        context = _context;
    }

    @Override
    public PhotoListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String url = photoUrls.get(position);
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.logo)
                .centerCrop()
                .crossFade()
                .into(holder.photoImageView);
        holder.photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                remove(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoUrls.size();
    }

}