package com.vadim.hasdfa.udacity.favorite_movies.Controllers.MainMoview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.vadim.hasdfa.udacity.favorite_movies.Controllers.DetailMovie.MoviewDetailActivity;
import com.vadim.hasdfa.udacity.favorite_movies.Model.Movie;
import com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.NetworkUtils;
import com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.OnLoadCompleteListener;
import com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.TheMoviewDBAPI;
import com.vadim.hasdfa.udacity.favorite_movies.Model.UserData;
import com.vadim.hasdfa.udacity.favorite_movies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raksha Vadim on 29.07.17, 20:58.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private static int getAdsCount(){
        return NetworkUtils.page * 3;
    }

    private static final String ADMOB_AD_UNIT_ID_BIG = "ca-app-pub-8285809783573127/6795752888";

    public MoviesAdapter(ArrayList<Movie> movies, Context context){
        this.movies = movies;
        this.context = context;
    }

    private Context context;
    private ArrayList<Movie> movies;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.movie_item, parent, false)
            );
        }
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_layout, parent, false)
        );
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= movies.size()-1) {
            Movie m = movies.get(position);
            return m.isAd() ? 5 : 1;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            setupBlock(movies.get(position), holder);
        } else {
            AdLoader.Builder builder = new AdLoader
                    .Builder(holder.itemView.getContext(), ADMOB_AD_UNIT_ID_BIG);

            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd ad) {
                    NativeContentAdView adView = (NativeContentAdView) holder.itemView;
                    populateContentAdView(ad, adView);
                }
            });
            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
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
                        FirebaseAnalytics.getInstance(holder.itemView.getContext())
                                .logEvent("failed_native_ad_"
                                        + error, Bundle.EMPTY);
                    }
                }
            }).build();
            AdRequest.Builder requestBuilder = new AdRequest.Builder();
            adLoader.loadAd(requestBuilder.build());
        }
    }


    private void setupBlock(final Movie movie, final ViewHolder holder){
        String imgUrl = TheMoviewDBAPI.baseImage + movie.getPosterPath();
        Log.d("myLog", "imgURL: " + imgUrl);
        Picasso.with(holder.thumbnail.getContext())
                .load(imgUrl)
                .into(holder.thumbnail);
        holder.title.setText(movie.getTitle());
        Log.d("myLog", "movie.getTitle(): " + movie.getTitle());
        holder.rateStars.setText(""+movie.getVoteAverage());
        Log.d("myLog", "movie.getVoteAverage(): " + movie.getVoteAverage());

        final View transitionComponent1 = holder.thumbnail;
        final View transitionComponent2 = holder.rateStars;
        final View transitionComponent3 = holder.rateImage;
//        final View transitionComponent4 = holder.title;
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.movie = movie;
                Intent i = new Intent(context, MoviewDetailActivity.class);

                Bundle bundle2Send = Bundle.EMPTY;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(
                                    (Activity) context,
                                    Pair.create(transitionComponent1, "posterTransition"),
                                    Pair.create(transitionComponent2, "rateTextTransition"),
                                    Pair.create(transitionComponent3, "rateImageTransition")
                                    //, Pair.create(transitionComponent4, "titleTransition")
                            );
                    bundle2Send = options.toBundle();
                }
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, holder.getAdapterPosition()+"");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, movie.getTitle());
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "detailMovie");
                FirebaseAnalytics.getInstance(context)
                        .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                context.startActivity(i, bundle2Send);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (movies != null) ? movies.size() : 0;
    }

    public void notifyDataSetChanged(ArrayList<Movie> movies){
        if (this.movies != null) {
            this.movies.addAll(movies);
        } else {
            this.movies = movies;
        }
        notifyDataSetChanged();
    }

    void reload(ArrayList<Movie> movies) {
        this.movies = movies;
        this.notifyDataSetChanged();
    }

    public OnLoadCompleteListener mNetworkLoader = null;

    void reload() {
        this.movies = new ArrayList<>();
        NetworkUtils.page = 1;
        new NetworkUtils(mNetworkLoader).loadMore();
        this.notifyDataSetChanged();
    }

    private static final String key = "movies_array_list";
    void onRestoreInstanceState(Bundle savedInstanceState) {
        NetworkUtils.page = savedInstanceState.getInt("page");
        movies = new ArrayList<>();
        movies = savedInstanceState.getParcelableArrayList(key);
        Log.d("myLog", "Adapter:onRestoreInstanceState - array: " + movies);
        notifyDataSetChanged();
    }

    void onSavedInstance(Bundle outState) {
        outState.putInt("page", NetworkUtils.page);
        outState.putParcelableArrayList(key, movies);
        Log.d("myLog", "Adapter:onSavedInstance - array: " + movies);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView thumbnail;
        TextView rateStars;
        ImageView rateImage;
        View view;
        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_image_view);
            rateStars = (TextView) itemView.findViewById(R.id.rate_text_view);
            rateImage = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }


    // Ads
    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }
}