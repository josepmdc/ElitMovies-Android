package com.example.josepm.elitmovies.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.josepm.elitmovies.R;
import com.example.josepm.elitmovies.fragments.MoviesFragment;
import com.example.josepm.elitmovies.fragments.TvShowsFragment;
import com.example.josepm.elitmovies.fragments.UserFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MoviesFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_movies:
                            selectedFragment = new MoviesFragment();
                            break;
                        case R.id.nav_tv_shows:
                            selectedFragment = new TvShowsFragment();
                            break;
                        case R.id.nav_user:
                            selectedFragment = new UserFragment();
                            break;
                        default:
                            selectedFragment = new MoviesFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

}
