package com.vadim.hasdfa.udacity.popularmovies.Controllers.DetailMovie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vadim.hasdfa.udacity.popularmovies.Model.SecondItem;
import com.vadim.hasdfa.udacity.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by Raksha Vadim on 31.07.17, 16:33.
 */

public class SecondItemAdapter extends RecyclerView.Adapter<SecondItemAdapter.ViewHolder> {

    private ArrayList<SecondItem> secondItems;
    private AppCompatActivity mActivity;

    SecondItemAdapter(AppCompatActivity appCompatActivity){
        this.mActivity = appCompatActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(
                    LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_video, parent, false)
            );
        } else {
            return new ViewHolder(
                    LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_review, parent, false)
            );
        }
    }

    @Override
    public int getItemViewType(int position) {
        return secondItems.get(position).itemType;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SecondItem secondItem = secondItems.get(position);
        if (secondItem.itemType == 0) {
            holder.name.setText(secondItem.getName());
            holder.clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "http://www.youtube.com/watch?v="+secondItem.getKey();
                    Log.d("myLog", "URL: " + url);
                    i.setData(Uri.parse(url));
                    mActivity.startActivity(i);
                }
            });
        } else {
            holder.author.setText(secondItem.getAuthor());
            holder.content.setText(secondItem.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return (secondItems == null) ? 0 : secondItems.size();
    }

    public void notifyDataSetChanged(ArrayList<SecondItem> secondItems){
        this.secondItems = secondItems;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout clickable;

        TextView author;
        TextView content;
        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.videoName);
            clickable = (LinearLayout) itemView.findViewById(R.id.clickableVideoFrame);

            author = (TextView) itemView.findViewById(R.id.author_text_view);
            content = (TextView) itemView.findViewById(R.id.content_text_view);
        }
    }
}
