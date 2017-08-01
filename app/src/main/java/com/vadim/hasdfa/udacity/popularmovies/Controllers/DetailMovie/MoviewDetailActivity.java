package com.vadim.hasdfa.udacity.popularmovies.Controllers.DetailMovie;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vadim.hasdfa.udacity.popularmovies.Model.DataBase.MovieDBController;
import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;
import com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.LoadDetailNetwork;
import com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.TheMoviewDBAPI;
import com.vadim.hasdfa.udacity.popularmovies.Model.UserData;
import com.vadim.hasdfa.udacity.popularmovies.R;
import com.vadim.hasdfa.udacity.popularmovies.utils.Blur;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Raksha Vadim on 30.07.17, 15:07.
 */

public class MoviewDetailActivity extends AppCompatActivity {

    @BindView(R.id.title_text_view) TextView title;
    @BindView(R.id.date_text_view) TextView date;
    @BindView(R.id.overview_text_view) TextView overview;
    @BindView(R.id.rate_text_view) TextView rate;
    @BindView(R.id.backArrow) ImageButton backButton;
    @BindView(R.id.blured_image_view) ImageView blured;
    @BindView(R.id.poster_image_view) ImageView poster;
    @BindView(R.id.favorite_button) View favoriteButton;
    @BindView(R.id.favorite_image_view) ImageView favoriteImage;
    @BindView(R.id.favorite_text_view) TextView favoriteTextView;

    @BindView(R.id.mRecyclerView) RecyclerView mRecyclerView;
    SecondItemAdapter mSecondItemAdapter;

    Movie currentMoview = UserData.movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMoview.setFavorite(!currentMoview.isFavorite());
                reloadFavorite();
            }
        });
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            Uri queryUri = Uri.parse("content://com.vadim.hasdfa.udacity.popularmovies/movies");
            Cursor c = getContentResolver().query(queryUri, null, null, null, null);
            ArrayList<Movie> moviesWhatIHate = new ArrayList<>();
            MovieDBController.shared().getFromDB(moviesWhatIHate, c);
            Log.d("myLog", "ContentProviderItems: " + moviesWhatIHate);
            if (c != null && !c.isClosed()) c.close();


            MovieDBController dbController = MovieDBController.shared()
                    .beginDataBaseQuery(this)
                    .getItemById(currentMoview.getId(), movies);
            if (movies.size() > 0) {
                currentMoview.setFavorite(movies.get(0).isFavorite());
            } else {
//                ContentValues cv = new ContentValues();
//                put data in ContentProvider
//                getContentResolver().insert(queryUri, cv);

                dbController
                        .putItem(currentMoview);
            }
            dbController
                    .endDataBaseQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        reloadView();
        reloadFavoriteLocal();

        mSecondItemAdapter = new SecondItemAdapter(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSecondItemAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        LoadDetailNetwork.shared()
                .load(currentMoview, mSecondItemAdapter);
    }


    private void reloadFavoriteLocal() {
        if (currentMoview.isFavorite()) {
            favoriteImage.setImageResource(R.drawable.ic_favorite);
            favoriteTextView.setText("Remove from favorite");
        } else {
            favoriteImage.setImageResource(R.drawable.ic_unfavorite);
            favoriteTextView.setText("Add to favorite");
        }
    }

    private void reloadFavorite() {
        if (currentMoview.isFavorite()) {
            favoriteImage.setImageResource(R.drawable.ic_favorite);
            favoriteTextView.setText("Remove from favorite");
        } else {
            favoriteImage.setImageResource(R.drawable.ic_unfavorite);
            favoriteTextView.setText("Add to favorite");
        }
        try {
            MovieDBController.shared()
                    .beginDataBaseQuery(this)
                    .updateItem(currentMoview.getId(), currentMoview.isFavorite())
                    .endDataBaseQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadView(){
        Picasso.with(MoviewDetailActivity.this)
                .load(TheMoviewDBAPI.baseImage + currentMoview.getPosterPath())
                .into(poster);
        Picasso.with(MoviewDetailActivity.this)
                .load(TheMoviewDBAPI.baseBack + currentMoview.getBackdropPath())
                .transform(new Blur(this, 18))
                .into(blured);
        title.setText(currentMoview.getTitle());
        date.setText(currentMoview.getReleaseDate());
        rate.setText(currentMoview.getVoteAverage()+"");
        overview.setText(currentMoview.getOverview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!currentMoview.equals(UserData.movie)) {
            currentMoview = UserData.movie;
            reloadView();
        }
    }
}
