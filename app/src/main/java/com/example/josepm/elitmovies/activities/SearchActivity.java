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
import com.example.josepm.elitmovies.adapters.SearchResultsAdapter;
import com.example.josepm.elitmovies.api.tmdb.MoviesRepository;
import com.example.josepm.elitmovies.api.tmdb.SearchRepository;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetGenresCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetSearchResultsCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnSearchResultClickCallback;
import com.example.josepm.elitmovies.api.tmdb.models.Genre;
import com.example.josepm.elitmovies.api.tmdb.models.SearchResponse;
import com.example.josepm.elitmovies.api.tmdb.models.SearchResult;

import java.util.List;

import retrofit2.Call;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchResultsRecyclerView;
    private ProgressBar progressBar;
    private TextView searchResultsPlaceholder;
    private LinearLayoutManager layoutManager;

    private SearchRepository searchRepository;
    private SearchResultsAdapter adapter;
    private MoviesRepository moviesRepository;
    private List<Genre> genresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchResultsRecyclerView = findViewById(R.id.search_results);
        layoutManager = new LinearLayoutManager(this);
        searchResultsRecyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.loading_search);
        searchResultsPlaceholder = findViewById(R.id.search_results_placeholder);

        searchRepository = SearchRepository.getInstance();
        moviesRepository = MoviesRepository.getInstance();

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

    OnSearchResultClickCallback callback = new OnSearchResultClickCallback() {
        @Override
        public void onClick(SearchResult searchResult) {
            Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MOVIE_ID, searchResult.getId());
            startActivity(intent);
        }
    };

    private void fetchResults(String query) {
        getGenres();
        searchRepository.getSearchResults(query, new OnGetSearchResultsCallback() {
            @Override
            public void onSuccess(List<SearchResult> searchResults) {
                adapter = new SearchResultsAdapter(searchResults, genresList, callback);
                searchResultsRecyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        t.toString(),
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
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
