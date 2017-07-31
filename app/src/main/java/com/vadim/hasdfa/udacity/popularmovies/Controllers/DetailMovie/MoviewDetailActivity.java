package com.vadim.hasdfa.udacity.popularmovies.Controllers.DetailMovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vadim.hasdfa.udacity.popularmovies.Model.DataBase.MovieDBController;
import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;
import com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.TheMoviewDBAPI;
import com.vadim.hasdfa.udacity.popularmovies.Model.UserData;
import com.vadim.hasdfa.udacity.popularmovies.R;
import com.vadim.hasdfa.udacity.popularmovies.utils.Blur;

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

    Movie currentMoview = UserData.movie;
    MovieDBController dbController;

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
        loadView();
        dbController = new MovieDBController();
    }

    private void loadView(){
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
            loadView();
        }
    }
}
