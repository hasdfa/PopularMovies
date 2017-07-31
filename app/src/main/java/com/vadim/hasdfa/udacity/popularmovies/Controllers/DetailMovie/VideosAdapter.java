package com.vadim.hasdfa.udacity.popularmovies.Controllers.DetailMovie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vadim.hasdfa.udacity.popularmovies.R;

/**
 * Created by Raksha Vadim on 31.07.17, 16:33.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.activity_detail_movie, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
