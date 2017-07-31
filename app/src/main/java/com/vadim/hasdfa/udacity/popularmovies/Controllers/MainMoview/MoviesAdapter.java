package com.vadim.hasdfa.udacity.popularmovies.Controllers.MainMoview;

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

import com.squareup.picasso.Picasso;
import com.vadim.hasdfa.udacity.popularmovies.Controllers.DetailMovie.MoviewDetailActivity;
import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;
import com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.NetworkUtils;
import com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.TheMoviewDBAPI;
import com.vadim.hasdfa.udacity.popularmovies.Model.UserData;
import com.vadim.hasdfa.udacity.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by Raksha Vadim on 29.07.17, 20:58.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    public MoviesAdapter(ArrayList<Movie> movies, Context context){
        this.movies = movies;
        this.context = context;
    }

    private Context context;
    private ArrayList<Movie> movies;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = movies.get(position);

        String imgUrl = TheMoviewDBAPI.baseImage + movie.getPosterPath();
        Log.d("myLog", "imgURL: " + imgUrl);
        Picasso.with(holder.thumbnail.getContext())
            .load(imgUrl)
                .into(holder.thumbnail);
        holder.title.setText(movie.getTitle());
        Log.d("myLog", "movie.getTitle(): " + movie.getTitle());
        holder.rateStars.setText(movie.getVoteAverage() + "");
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
                context.startActivity(i, bundle2Send);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (movies != null) ? movies.size(): 0;
    }

    public void notifyDataSetChanged(ArrayList<Movie> movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void reload(ArrayList<Movie> movies) {
        this.movies = movies;
        this.notifyDataSetChanged();
    }

    public void reload() {
        this.movies = new ArrayList<>();
        NetworkUtils.page = 1;
        new NetworkUtils(this)
                .loadMore();
        this.notifyDataSetChanged();
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
}