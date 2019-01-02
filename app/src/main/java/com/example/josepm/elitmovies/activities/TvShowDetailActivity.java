package com.example.josepm.elitmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.josepm.elitmovies.R;
import com.example.josepm.elitmovies.api.tmdb.TvShowsRepository;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetGenresCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetTrailersCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetTvShowCallback;
import com.example.josepm.elitmovies.api.tmdb.models.Genre;
import com.example.josepm.elitmovies.api.tmdb.models.Trailer;
import com.example.josepm.elitmovies.api.tmdb.models.TvShow;

import java.util.ArrayList;
import java.util.List;

public class TvShowDetailActivity extends AppCompatActivity {

    public static String TV_SHOW_ID = "tv_show_id";

    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";

    private ImageView tvShowBackdrop;
    private TextView tvShowGenres;
    private TextView tvShowTitle;
    private TextView tvShowOverview;
    private TextView tvShowOverviewLabel;
    private TextView tvShowReleaseDate;
    private RatingBar tvShowRating;
    private LinearLayout tvShowTrailers;
    private LinearLayout tvShowReviews;
    private TextView trailersLabel;

    private TvShowsRepository tvShowsRepository;
    private int tvShowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);

        tvShowId = getIntent().getIntExtra(TV_SHOW_ID, tvShowId);

        tvShowsRepository = TvShowsRepository.getInstance();

        setupToolbar();

        initUI();

        getTvShow();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarTvShowDetails);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        tvShowTitle = findViewById(R.id.tvShowDetailsTitle);
        tvShowBackdrop = findViewById(R.id.tvShowDetailsBackdrop);
        tvShowGenres = findViewById(R.id.tvShowDetailsGenres);
        tvShowOverview = findViewById(R.id.tvShowDetailsOverview);
        tvShowOverviewLabel = findViewById(R.id.tvShowSummaryLabel);
        tvShowReleaseDate = findViewById(R.id.tvShowDetailsReleaseDate);
        tvShowRating = findViewById(R.id.tvShowDetailsRating);
        tvShowTrailers = findViewById(R.id.tvShowTrailers);
        tvShowReviews = findViewById(R.id.tvShowReviews);
        trailersLabel = findViewById(R.id.tvShowTrailersLabel);
    }

    private void getTvShow() {
        tvShowsRepository.getTvShow(tvShowId, new OnGetTvShowCallback() {
            @Override
            public void onSuccess(TvShow tvShow) {
                tvShowTitle.setText(tvShow.getName());
                tvShowOverviewLabel.setVisibility(View.VISIBLE);
                tvShowOverview.setText(tvShow.getOverview());
                tvShowRating.setVisibility(View.VISIBLE);
                tvShowRating.setRating(tvShow.getRating() / 2);
                getGenres(tvShow);
                tvShowReleaseDate.setText(tvShow.getFirstAirDate());
                if (!isFinishing()) {
                    Glide.with(getApplicationContext())
                            .load(IMAGE_BASE_URL + tvShow.getBackdropPath())
                            .apply(new RequestOptions()
                                    .placeholder(R.color.colorPrimary))
                            .into(tvShowBackdrop);
                }
                getTrailers(tvShow);
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    private void getGenres(final TvShow tvShow) {
        tvShowsRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (tvShow.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : tvShow.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    tvShowGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getTrailers(TvShow tvShow) {
        tvShowsRepository.getTrailers(tvShow.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                trailersLabel.setVisibility(View.VISIBLE);
                tvShowTrailers.removeAllViews();
                for (final Trailer trailer : trailers) {
                    View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, tvShowTrailers, false);
                    ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                    thumbnail.requestLayout();
                    thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey()));
                        }
                    });
                    Glide.with(TvShowDetailActivity.this)
                            .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                            .into(thumbnail);
                    tvShowTrailers.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
                trailersLabel.setVisibility(View.GONE);
            }
        });
    }

    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showError() {
        Toast.makeText(TvShowDetailActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}
