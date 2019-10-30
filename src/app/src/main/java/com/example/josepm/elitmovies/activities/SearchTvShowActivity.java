package com.example.josepm.elitmovies.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.josepm.elitmovies.R;
import com.example.josepm.elitmovies.adapters.TvShowSearchResultAdapter;
import com.example.josepm.elitmovies.api.tmdb.SearchRepository;
import com.example.josepm.elitmovies.api.tmdb.TvShowsRepository;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetGenresCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetTvShowSearchResultCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnTvShowsClickCallback;
import com.example.josepm.elitmovies.api.tmdb.models.Genre;
import com.example.josepm.elitmovies.api.tmdb.models.TvShow;
import com.example.josepm.elitmovies.api.tmdb.models.TvShowSearchResponse;

import java.util.List;

import retrofit2.Call;

public class SearchTvShowActivity extends AppCompatActivity {

    private RecyclerView searchResultsRecyclerView;
    private ProgressBar progressBar;
    private TextView searchResultsPlaceholder;
    private LinearLayoutManager layoutManager;

    private SearchRepository searchRepository;
    private TvShowSearchResultAdapter adapter;
    private TvShowsRepository tvShowRepository;
    private List<Genre> genresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv_show);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchResultsRecyclerView = findViewById(R.id.tv_shows_search_results);
        layoutManager = new LinearLayoutManager(this);
        searchResultsRecyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.loading_tv_shows_search);
        searchResultsPlaceholder = findViewById(R.id.tv_shows_search_results_placeholder);

        searchRepository = SearchRepository.getInstance();
        tvShowRepository = TvShowsRepository.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResultsPlaceholder.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                fetchResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResultsPlaceholder.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                fetchResults(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    OnTvShowsClickCallback callback = new OnTvShowsClickCallback() {
        @Override
        public void onClick(TvShow tvShowSearchResult) {
            Intent intent = new Intent(getApplicationContext(), TvShowDetailActivity.class);
            intent.putExtra(TvShowDetailActivity.TV_SHOW_ID, tvShowSearchResult.getId());
            startActivity(intent);
        }
    };

    private void fetchResults(String query) {
        getGenres();
        searchRepository.getTvShowSearchResults(query, new OnGetTvShowSearchResultCallback() {
            @Override
            public void onSuccess(List<TvShow> tvShowsSearchResults) {
                adapter = new TvShowSearchResultAdapter(tvShowsSearchResults, genresList, callback);
                searchResultsRecyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Call<TvShowSearchResponse> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        t.toString(),
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void getGenres() {
        tvShowRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                genresList = genres;
            }

            @Override
            public void onError() {

            }
        });
    }


}
