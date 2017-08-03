package com.vadim.hasdfa.udacity.favorite_movies.Controllers.MainMoview;

import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vadim.hasdfa.udacity.favorite_movies.Model.DataBase.MovieDBController;
import com.vadim.hasdfa.udacity.favorite_movies.Model.Movie;
import com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.NetworkUtils;
import com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.OnLoadCompleteListener;
import com.vadim.hasdfa.udacity.favorite_movies.Model.UserData;
import com.vadim.hasdfa.udacity.favorite_movies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieMainActivity extends AppCompatActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    //Popular
    @BindView(R.id.popular_text_view) TextView popular;
    @BindView(R.id.card_popular) CardView popularCardView;

    //Top rated
    @BindView(R.id.top_rated_text_view) TextView topRated;
    @BindView(R.id.card_top_rated) CardView topRatedCardView;

    //Favorites
    @BindView(R.id.favorites_text_view) TextView favorites;
    @BindView(R.id.card_favorites) CardView favoritesCardView;

    @BindView(R.id.loading) View loadingView;

    MoviesAdapter mAdapter;

    private static final String ADMOB_APP_ID = "ca-app-pub-8285809783573127~6778429257";

    private OnLoadCompleteListener mNetworkLoader = new OnLoadCompleteListener() {
        @Override
        public void onLoad(ArrayList<Movie> movies) {
            mAdapter.notifyDataSetChanged(movies);
            loadingView.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        ButterKnife.bind(this);

        int columnCount = getColumns();

        final LinearLayoutManager lManager = new GridLayoutManager(this, columnCount);
        mRecyclerView.setLayoutManager(lManager);

        MobileAds.initialize(this, ADMOB_APP_ID);

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        mAdapter.mNetworkLoader = mNetworkLoader;
        mRecyclerView.setAdapter(mAdapter);

        loadingView.setVisibility(View.VISIBLE);
        new NetworkUtils(mNetworkLoader).loadMore();

        ArrayList<Movie> movies = new ArrayList<>();
        try {
            Uri queryUri = Uri.parse("content://com.vadim.hasdfa.udacity.popularmovies/movies");
            MovieDBController.shared()
                    .getFromDB(movies, getContentResolver().query(queryUri, null, null, null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("myLog", "Movies in db: " + movies);


        // Add
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = lManager.getItemCount();
                int lastVisibleItem = lManager.findLastVisibleItemPosition();
                if (!NetworkUtils.isLoading && totalItemCount <= (lastVisibleItem + 2) && UserData.sortType != UserData.SortType.favorite) {
                    NetworkUtils.isLoading = true;
                    new NetworkUtils(mNetworkLoader).incrementPage();
                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (getActionBar() != null) {
            getActionBar().hide();
        }

        popularCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserData.sortType != UserData.SortType.popular) {
                    UserData.sortType = UserData.SortType.popular;
                    setSelected();
                    loadingView.setVisibility(View.VISIBLE);
                    mAdapter.reload();
                }
            }
        });
        topRatedCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserData.sortType != UserData.SortType.topRated) {
                    UserData.sortType = UserData.SortType.topRated;
                    setSelected();
                    loadingView.setVisibility(View.VISIBLE);
                    mAdapter.reload();
                }
            }
        });
        favoritesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserData.sortType != UserData.SortType.favorite) {
                    UserData.sortType = UserData.SortType.favorite;
                    setSelected();
                    reloadAdapterForFavorite();
                }
            }
        });
    }

    private void setSelected(){
        FirebaseAnalytics.getInstance(this).logEvent("select_category", Bundle.EMPTY);

        selectView(popular, popularCardView, UserData.sortType == UserData.SortType.popular);
        selectView(topRated, topRatedCardView, UserData.sortType == UserData.SortType.topRated);
        selectView(favorites, favoritesCardView, UserData.sortType == UserData.SortType.favorite);
    }

    private void selectView(TextView textView, CardView cardView, boolean isSelected){
        int colorPrimary = getResources().getColor(R.color.colorPrimary);
        int colorWhite = Color.WHITE;
        if (isSelected) {
            cardView.setCardBackgroundColor(colorPrimary);
            textView.setTextColor(colorWhite);
        } else {
            cardView.setCardBackgroundColor(colorWhite);
            textView.setTextColor(colorPrimary);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserData.sortType == UserData.SortType.favorite) {
            reloadAdapterForFavorite();
        }
    }


    private void reloadAdapterForFavorite(){
        ArrayList<Movie> moviesFromDb = new ArrayList<>();
        try {
            Uri queryUri = Uri.parse("content://com.vadim.hasdfa.udacity.popularmovies/movies");
            MovieDBController.shared()
                    .getFromDB(moviesFromDb, getContentResolver().query(queryUri, null, "isFavorite=1", null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter.reload(moviesFromDb);
        Log.d("myLog", "Selected items: " + moviesFromDb);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSavedInstance(outState);

        outState.putString("udName", UserData.sortType.name());

        GridLayoutManager glm = (GridLayoutManager) mRecyclerView.getLayoutManager();
        outState.putParcelable("llm", glm.onSaveInstanceState());

        mAdapter.onSavedInstance(outState);

        outState.putInt("scrollPosition", glm.findFirstVisibleItemPosition());
        Log.d("myLog", "onSaveInstanceState");
    }

    private int getColumns(){
        int columnCount = 2;
        Configuration config = getResources().getConfiguration();

        boolean isSmallTablet = config.smallestScreenWidthDp >= 600;
        boolean isMiddleTablet = config.smallestScreenWidthDp >= 720;
        boolean isBigTablet = config.smallestScreenWidthDp >= 800;

        boolean isLandscape = config.screenHeightDp < config.screenWidthDp;
        if (isSmallTablet) {
            columnCount = isLandscape ? 4 : 3;
        } else if (isMiddleTablet) {
            columnCount = isLandscape ? 5 : 4;
        } else if (isBigTablet) {
            columnCount = isLandscape ? 6 : 5;
        } else if (isLandscape)
            columnCount = 3;

        return columnCount;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String sortType = savedInstanceState.getString("udName");

        mAdapter.onRestoreInstanceState(savedInstanceState);

        GridLayoutManager glm = (GridLayoutManager) mRecyclerView.getLayoutManager();
        glm.onRestoreInstanceState(savedInstanceState.getParcelable("llm"));

        int position = savedInstanceState.getInt("scrollPosition");
        glm.scrollToPosition(position);

        int columnCount = getColumns();
        glm.setSpanCount(columnCount);


        if (sortType != null) {
            if (sortType.equals(UserData.SortType.favorite.name())) {
                checkOnRestore(UserData.SortType.favorite);
            } else if (sortType.equals(UserData.SortType.topRated.name())) {
                checkOnRestore(UserData.SortType.topRated);
            } else if (sortType.equals(UserData.SortType.popular.name())) {
                checkOnRestore(UserData.SortType.popular);
            }
        }
        setSelected();
    }

    private void checkOnRestore(UserData.SortType type){
        if (type != UserData.sortType) {
            UserData.sortType = type;
            if (type != UserData.SortType.favorite) {
                reloadAdapterForFavorite();
            } else {
                mAdapter.reload();
            }
        }
    }
}
