package com.vadim.hasdfa.udacity.favorite_movies.Controllers.DetailMovie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vadim.hasdfa.udacity.favorite_movies.Model.SecondItem;
import com.vadim.hasdfa.udacity.favorite_movies.R;

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
        } else if (viewType == 5) {
            Log.d("myLog", "LOAD_EXPRESS_AD");
            return new ViewHolder(
                    LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.simple_ad, parent, false)
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
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

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, holder.getAdapterPosition()+"");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, secondItem.getName());
                    bundle.putString(FirebaseAnalytics.Param.SOURCE, url);
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "video_from_"+secondItem.getSite());
                    FirebaseAnalytics.getInstance(mActivity)
                            .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    mActivity.startActivity(i);
                }
            });
        } else if (secondItem.itemType == 5) {
            NativeExpressAdView mAdView = (NativeExpressAdView) holder.itemView.findViewById(R.id.adView);
            // Set its video options.
            mAdView.setVideoOptions(new VideoOptions.Builder()
                    .setStartMuted(true)
                    .build());

            // The VideoController can be used to get lifecycle events and info about an ad's video
            // asset. One will always be returned by getVideoController, even if the ad has no video
            // asset.
            final VideoController mVideoController = mAdView.getVideoController();
            mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    Log.d("myLog", "Video playback is finished.");
                    super.onVideoEnd();
                }
            });

            // Set an AdListener for the AdView, so the Activity can take action when an ad has finished
            // loading.
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (mVideoController.hasVideoContent()) {
                        Log.d("myLog", "Received an ad that contains a video asset.");
                    } else {
                        Log.d("myLog", "Received an ad that does not contain a video asset.");
                    }
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    String error = "";
                    switch (errorCode) {
                        case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                            error = "ERROR_CODE_INTERNAL_ERROR";
                            break;
                        case AdRequest.ERROR_CODE_INVALID_REQUEST:
                            error = "ERROR_CODE_INVALID_REQUEST";
                            break;
                        case AdRequest.ERROR_CODE_NETWORK_ERROR:
                            error = "ERROR_CODE_NETWORK_ERROR";
                            break;
                        case AdRequest.ERROR_CODE_NO_FILL:
                            error = "ERROR_CODE_NO_FILL";
                            break;
                    }
                    if (!error.isEmpty()) {
                        if (!error.isEmpty()) {
                            FirebaseAnalytics.getInstance(holder.itemView.getContext())
                                    .logEvent("failed_native_ad_"
                                            + error, Bundle.EMPTY);
                        }
                    }
                }
            });


            mAdView.loadAd(new AdRequest.Builder().build());
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
