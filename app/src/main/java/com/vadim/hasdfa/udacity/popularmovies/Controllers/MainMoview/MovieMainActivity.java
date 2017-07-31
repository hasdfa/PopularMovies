package com.vadim.hasdfa.udacity.popularmovies.Controllers.MainMoview;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.NetworkUtils;
import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;
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
        mRecyclerView.setLayoutManager(lManager);

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        mRecyclerView.setAdapter(mAdapter);

        new NetworkUtils(mAdapter)
                .loadMore();

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
                if (!NetworkUtils.isLoading && totalItemCount <= (lastVisibleItem + 5)) {
                    new NetworkUtils(mAdapter)
                            .incrementPage();
                    NetworkUtils.isLoading = true;
                }
            }
        });
    }

    MenuItem topRated;
    MenuItem popular;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        topRated = menu.findItem(R.id.sort_topRated);
        popular = menu.findItem(R.id.sort_popular);
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
            }
        } else if (item.getItemId() == R.id.sort_popular){
            if (UserData.sortType != UserData.SortType.popular) {
                UserData.sortType = UserData.SortType.popular;
                mAdapter.reload();
                item.setChecked(true);
                topRated.setChecked(false);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
