package com.example.weatherforecastapp.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;

import androidx.navigation.ui.NavigationUI;

import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        NavController navController = navHostFragment.getNavController();

        // Set up the BottomNavigationView with the NavController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemWidth = binding.bottomNavigationView.getWidth() / binding.bottomNavigationView.getMenu().size();
                int indicatorPosition = 0;

                if (item.getItemId() == R.id.today) {
                    navController.navigate(R.id.todayFragment);
                    indicatorPosition = 0;
                } else if (item.getItemId() == R.id.hourly) {
                    navController.navigate(R.id.hourlyFragment);
                    indicatorPosition = itemWidth;
                } else if (item.getItemId() == R.id.air) {
                    indicatorPosition = itemWidth * 2;
                    navController.navigate(R.id.airQualityFragment);
                }

                binding.indicator.animate().x(indicatorPosition).setDuration(300).start();
                return true; // Return true to indicate the item was selected
            }
        });

    }


    }

