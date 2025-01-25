package com.example.weatherforecastapp.ui.fragment;

import static androidx.navigation.Navigation.findNavController;
import static com.example.weatherforecastapp.Constant.API_ID;
import static com.example.weatherforecastapp.api.ApiInstance.retrofit;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import android.Manifest;

import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.api.ApiInstance;
import com.example.weatherforecastapp.databinding.FragmentTodayBinding;
import com.example.weatherforecastapp.model.ListApi;
import com.example.weatherforecastapp.model.Root;
import com.example.weatherforecastapp.model.Weather;
import com.example.weatherforecastapp.viewModel.WeatherViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TodayFragment extends Fragment {
    private FragmentTodayBinding binding;
    public static String city;
    public static Double lat;
    public static Double lon;
    private WeatherViewModel viewModel;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;


    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentTodayBinding.inflate(getLayoutInflater(), container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (city == null) {
            binding.progressBar2.setVisibility(View.VISIBLE);
            binding.scrollView2.setVisibility(View.INVISIBLE);


            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {

                getCurrentLocation();
            }

        }
        binding.currentLoacationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ButtonClick", "Time text clicked");
                try {
                    getCurrentLocation();
                } catch (Exception e) {
                    Log.e("Error", "Error getting city ");
                    Toast.makeText(requireContext(), "Error getting location", Toast.LENGTH_SHORT).show();
                }
            }
        });


        viewModel = new WeatherViewModel();
        getCity();
        viewModel.getWeather(city, API_ID, "metric");
        viewModel.getForecast(city, API_ID, "metric");
        getUserData();
        getForecastData();

        binding.seeLeveltxt.setVisibility(View.VISIBLE);
        binding.seebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded()) {

                    NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

                    navController.navigate(R.id.action_todayFragment_to_detailFragment);
                } else {
                    Log.e("Fragment Error", "Fragment is not attached to the activity");
                }
            }
        });


        return binding.getRoot();
    }

    private void getCity() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if (!(s.isEmpty())) {
                    city = s;

                    viewModel.getWeather(city, API_ID, "metric");
                    viewModel.getForecast(city, API_ID, "metric");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }



    private void getForecastData() {
        viewModel.getForecastData().observe(getViewLifecycleOwner(), new Observer<Root>() {

            @Override
            public void onChanged(Root root) {
                if (root != null) {
                    try {
                        binding.sunsetTxt.setText("Sunset:\n" + viewModel.convertUnixTotime(root.city.sunset));
                        binding.sunriseTxt.setText("Sunrise:\n" + viewModel.convertUnixTotime(root.city.sunrise));

                        Glide.with(requireContext())
                                .load("https://openweathermap.org/img/wn/" + root.listapi.get(0).weather.get(0).icon + "@2x.png")
                                .into(binding.iconimg);
                        binding.dateTxt.setText(viewModel.convertUnixToDate(root.listapi.get(0).dt_txt));
                        binding.timeTxt.setText(viewModel.convertUnixToDateTime(root.listapi.get(0).dt_txt));
                        lat = root.city.coord.lat;
                        lon = root.city.coord.lon;
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    private void getUserData() {
        viewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<ListApi>() {
            @Override
            public void onChanged(ListApi listApi) {
                if (listApi != null) {
                    try {
                        binding.tempMainTxt.setText(listApi.main.temp + "°C");
                        binding.MinTxt.setText("Min: " + listApi.main.temp_min + "°C");
                        binding.maxTempTxt.setText("Max: " + listApi.main.temp_max + "°C");
                        binding.HumidityTxt.setText("Humidity:\n" + listApi.main.humidity + "%");
                        binding.realfeelTxt.setText("Feels Like:\n" + listApi.main.feels_like + "°C");
                        binding.cityTxt.setText(city);
                        binding.windTxt.setText("Wind Speed:\n" + listApi.wind.speed + " m/s");
                        binding.seeLeveltxt.setText("Sea Level:\n" + listApi.main.sea_level + " hPa");
                        binding.conditionTxt.setText(String.valueOf(listApi.weather.get(0).main));
                        binding.pressureTxt.setText("Pressure:\n" + listApi.main.pressure + " hPa");
                        String weather = listApi.weather.get(0).main;
                        switch (weather) {
                            case "Clouds":
                                binding.lottieAnimationView.setAnimation(R.raw.clouds);

                                break;

                            case "Rain":
                                binding.lottieAnimationView.setAnimation(R.raw.rain);

                                break;

                            case "Clear":
                                binding.lottieAnimationView.setAnimation(R.raw.sun);

                                break;

                            case "Snow":
                                binding.lottieAnimationView.setAnimation(R.raw.snow);

                                break;
                            case "Smoke":
                                binding.lottieAnimationView.setAnimation(R.raw.smoke);

                                break;
                            case "Mist":
                                binding.lottieAnimationView.setAnimation(R.raw.mist);

                                break;
                            case "Haze":
                                binding.lottieAnimationView.setAnimation(R.raw.haze);

                                break;


                            default:
                                binding.lottieAnimationView.setAnimation(R.raw.sun);

                                break;
                        }


                    } catch (Exception exception) {
                        Log.d("Fetching", "error $exception");

                    }
                }
            }
        });
    }


    private void getCurrentLocation() {
        try {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            getCityFromLocation(latitude, longitude);
                        } else {
                            Toast.makeText(getContext(), "Location not available, trying again.", Toast.LENGTH_SHORT).show();
                            requestLocationUpdates();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to get location", Toast.LENGTH_LONG).show();
                    });
        } catch (SecurityException e) {
            Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_LONG).show();
        }
    }

    // In case the last location is null, request location updates
    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // 10 seconds
                .setFastestInterval(5000); // 5 seconds

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null && locationResult.getLocations().size() > 0) {
                Location location = locationResult.getLastLocation();
                getCityFromLocation(location.getLatitude(), location.getLongitude());
            }
        }
    };


    private void getCityFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String cityAddress = address.getLocality();
                if (cityAddress != null) {
                    city = cityAddress;
                    binding.progressBar2.setVisibility(View.INVISIBLE);
                    binding.scrollView2.setVisibility(View.VISIBLE);
                    viewModel.getWeather(city, API_ID, "metric");
                    viewModel.getForecast(city, API_ID, "metric");
                    Toast.makeText(getContext(), "Current City: " + cityAddress, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "City not found", Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Geocoder not available", Toast.LENGTH_LONG).show();
            Log.e("Geocoder Error", "Error: " + e.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, get the current location
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }






}