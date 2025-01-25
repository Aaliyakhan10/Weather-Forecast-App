package com.example.weatherforecastapp.ui.fragment;

import static com.example.weatherforecastapp.Constant.API_ID;
import static com.example.weatherforecastapp.ui.fragment.TodayFragment.city;

import static java.lang.String.format;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.adapter.WeatherAdapter;
import com.example.weatherforecastapp.databinding.FragmentHourlyBinding;
import com.example.weatherforecastapp.model.ListApi;
import com.example.weatherforecastapp.model.Root;
import com.example.weatherforecastapp.viewModel.WeatherViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HourlyFragment extends Fragment {

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private List<ListApi> hourlyWeatherList = new ArrayList<>();
    private FragmentHourlyBinding binding;
    private WeatherViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHourlyBinding.inflate(getLayoutInflater(), container, false);

        viewModel = new WeatherViewModel();
        viewModel.getForecast(city, API_ID, "metric");
        getForecastData();

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        weatherAdapter = new WeatherAdapter(hourlyWeatherList);

        recyclerView.setAdapter(weatherAdapter);
        return binding.getRoot();
    }

    private void getForecastData() {
        viewModel.getForecastData().observe(getViewLifecycleOwner(), new Observer<Root>() {
            @Override
            public void onChanged(Root root) {
                if (root != null) {
                    Log.d("HourlyFragment", "Fetched data: " + root.listapi.size() + " items");
                    hourlyWeatherList.clear();
                    hourlyWeatherList.addAll(root.listapi);  // Correctly referencing listapi
                    weatherAdapter.notifyDataSetChanged();
                } else {
                    Log.d("HourlyFragment", "root is null or listapi is empty");
                }
            }
        });
    }


}
