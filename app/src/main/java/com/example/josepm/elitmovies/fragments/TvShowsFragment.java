package com.example.josepm.elitmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.josepm.elitmovies.R;
import com.example.josepm.elitmovies.activities.SearchActivity;
import com.example.josepm.elitmovies.activities.TvShowDetailActivity;
import com.example.josepm.elitmovies.adapters.TvShowsAdapter;
import com.example.josepm.elitmovies.api.tmdb.TvShowsRepository;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetGenresCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetTvShowsCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnTvShowsClickCallback;
import com.example.josepm.elitmovies.api.tmdb.models.Genre;
import com.example.josepm.elitmovies.api.tmdb.models.TvShow;

import java.util.List;

public class TvShowsFragment extends Fragment {

    //region Variable declaration
    private ProgressBar mProgressBar;

    private RecyclerView tvShowsList;
    private TvShowsAdapter adapter;

    private TvShowsRepository tvShowsRepository;

    private List<Genre> movieGenres;

    private String sortBy = TvShowsRepository.POPULAR;

    private boolean isFetchingMovies;
    private int currentPage = 1;
    //endregion

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tv_shows_fragment, container, false);
        setHasOptionsMenu(true);

        mProgressBar = view.findViewById(R.id.indeterminateBar);

        tvShowsRepository = TvShowsRepository.getInstance();

        tvShowsList = view.findViewById(R.id.tv_shows_list);
        tvShowsList.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setupOnScrollListener();

        getGenres();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            case R.id.search_button:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // region Get data

    // It gets all the genres names
    private void getGenres() {
        tvShowsRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getTvShows(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    // It gets the tv shows and appends them to the RecyclerView
    private void getTvShows(int page) {
        isFetchingMovies = true;
        tvShowsRepository.getTvShows(page, sortBy, new OnGetTvShowsCallback() {
            @Override
            public void onSuccess(int page, List<TvShow> movies) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new TvShowsAdapter(movies, movieGenres, callback);
                    tvShowsList.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearTvShows();
                    }
                    adapter.appendTvShows(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    // endregion

    OnTvShowsClickCallback callback = new OnTvShowsClickCallback() {
        @Override
        public void onClick(TvShow tvShow) {
            Intent intent = new Intent(getActivity(), TvShowDetailActivity.class);
            intent.putExtra(TvShowDetailActivity.TV_SHOW_ID, tvShow.getId());
            startActivity(intent);
        }
    };

    // Displays the menu to sort by popular. top rated...
    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(getActivity(), getActivity().findViewById(R.id.sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // Every time we sort, we need to go back to page 1
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = TvShowsRepository.POPULAR;
                        getTvShows(currentPage);
                        tvShowsList.smoothScrollToPosition(0);
                        return true;
                    case R.id.top_rated:
                        sortBy = TvShowsRepository.TOP_RATED;
                        getTvShows(currentPage);
                        tvShowsList.smoothScrollToPosition(0);
                        return true;
                    case R.id.airing_today:
                        sortBy = TvShowsRepository.AIRING_TODAY;
                        getTvShows(currentPage);
                        tvShowsList.smoothScrollToPosition(0);
                        return true;
                    case R.id.on_the_air:
                        sortBy = TvShowsRepository.ON_THE_AIR;
                        getTvShows(currentPage);
                        tvShowsList.smoothScrollToPosition(0);
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_tv_shows_sort);
        sortMenu.show();
    }

    // When scrolling it makes requests to get more results
    private void setupOnScrollListener() {
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        tvShowsList.setLayoutManager(manager);
        tvShowsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getTvShows(currentPage + 1);
                    }
                }
            }
        });
    }

    // It displays a toast when there's an error
    private void showError() {
        Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}
