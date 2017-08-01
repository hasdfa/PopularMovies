package com.vadim.hasdfa.udacity.popularmovies.Controllers.MainMoview;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.vadim.hasdfa.udacity.popularmovies.Model.DataBase.MovieDBController;
import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;
import com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.NetworkUtils;
import com.vadim.hasdfa.udacity.popularmovies.Model.UserData;
import com.vadim.hasdfa.udacity.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieMainActivity extends AppCompatActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        ButterKnife.bind(this);

        final LinearLayoutManager lManager = new GridLayoutManager(this, 2);
        if (savedInstanceState != null) {
            GridLayoutManager llm = (GridLayoutManager) mRecyclerView.getLayoutManager();
            llm.onRestoreInstanceState(savedInstanceState.getParcelable("llm"));
        }
        mRecyclerView.setLayoutManager(lManager);

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        mRecyclerView.setAdapter(mAdapter);

        new NetworkUtils(mAdapter)
                .loadMore();

        ArrayList<Movie> movies = new ArrayList<>();
        try {
            MovieDBController.shared()
                    .beginDataBaseQuery(this)
                    .getAllItems(movies)
                    .endDataBaseQuery();
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
                if (!NetworkUtils.isLoading && totalItemCount <= (lastVisibleItem + 5) && UserData.sortType != UserData.SortType.favorite) {
                    new NetworkUtils(mAdapter)
                            .incrementPage();
                    NetworkUtils.isLoading = true;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserData.sortType == UserData.SortType.favorite) {
            reloadAdapterForFavorite();
        }
    }

    MenuItem topRated;
    MenuItem popular;
    MenuItem favorite;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        topRated = menu.findItem(R.id.sort_topRated);
        popular = menu.findItem(R.id.sort_popular);
        favorite = menu.findItem(R.id.list_favorite);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_topRated){
            if (UserData.sortType != UserData.SortType.topRated) {
                UserData.sortType = UserData.SortType.topRated;
                mAdapter.reload();
                item.setChecked(true);
                popular.setChecked(false);
                favorite.setChecked(false);
            }
        } else if (item.getItemId() == R.id.sort_popular){
            if (UserData.sortType != UserData.SortType.popular) {
                UserData.sortType = UserData.SortType.popular;
                mAdapter.reload();
                item.setChecked(true);
                topRated.setChecked(false);
                favorite.setChecked(false);
            }
        } else if (item.getItemId() == R.id.list_favorite){
            if (UserData.sortType != UserData.SortType.favorite) {
                UserData.sortType = UserData.SortType.favorite;
                reloadAdapterForFavorite();
                item.setChecked(true);
                popular.setChecked(false);
                topRated.setChecked(false);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadAdapterForFavorite(){
        ArrayList<Movie> moviesFromDb = new ArrayList<>();
        try {
            MovieDBController.shared()
                    .beginDataBaseQuery(this)
                    .getAllFavoriteItems(moviesFromDb)
                    .endDataBaseQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter.reload(moviesFromDb);
        Log.d("myLog", "Selected items: " + moviesFromDb);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("udName", UserData.sortType.name());

        GridLayoutManager glm = (GridLayoutManager) mRecyclerView.getLayoutManager();
        outState.putParcelable("llm", glm.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String sortType = savedInstanceState.getString("udName");

        GridLayoutManager glm = (GridLayoutManager) mRecyclerView.getLayoutManager();
        glm.onRestoreInstanceState(savedInstanceState.getParcelable("llm"));

        NetworkUtils.page = 1;
        if (sortType != null) {
            if (sortType.equals(UserData.SortType.favorite.name())) {
                checkOnRestore(UserData.SortType.favorite);
                favorite.setChecked(true);
                topRated.setChecked(false);
                popular.setChecked(false);
            } else if (sortType.equals(UserData.SortType.topRated.name())) {
                checkOnRestore(UserData.SortType.topRated);
                favorite.setChecked(false);
                topRated.setChecked(false);
                popular.setChecked(true);
            } else if (sortType.equals(UserData.SortType.popular.name())) {
                checkOnRestore(UserData.SortType.popular);
                favorite.setChecked(false);
                topRated.setChecked(false);
                popular.setChecked(true);
            }
        }
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
