package com.example.weatherforecastapp.ui.fragment;

import static com.example.weatherforecastapp.Constant.API_ID;
import static com.example.weatherforecastapp.ui.fragment.TodayFragment.city;

import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.weatherforecastapp.databinding.FragmentDetailBinding;
import com.example.weatherforecastapp.model.ListApi;
import com.example.weatherforecastapp.model.Root;
import com.example.weatherforecastapp.viewModel.WeatherViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DetailFragment extends Fragment {

    private WeatherViewModel viewModel;
    private FragmentDetailBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentDetailBinding.inflate(getLayoutInflater(), container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ListApi currentItem = (ListApi) bundle.getSerializable("current forecast");
            assert currentItem != null;
            getCurrentWeatherData(currentItem);
        }else{
            viewModel = new WeatherViewModel();

            viewModel.getWeather(city,API_ID,"metric");
            viewModel.getForecast(city,API_ID,"metric");
            getWeatherData();
            getForecastData();

        }

        return binding.getRoot();
    }

    private void getCurrentWeatherData(ListApi currentItem) {
        binding.temperature.setText(format("%s°C", currentItem.main.temp));
        binding.temperatureMin.setText(format("Min: %s°C", currentItem.main.temp_min));
        binding.temperatureMax.setText(format("Max: %s°C", currentItem.main.temp_max));
        binding.humidity.setText(format("Humidity: %d%%", currentItem.main.humidity));
        binding.feelslike.setText(format("Feels Like: %s°C", currentItem.main.feels_like));
        binding.city.setText(city);
        binding.windSpeed.setText(format("Wind Speed: %s m/s", currentItem.wind.speed));

        binding.conditionWeather.setText(valueOf(currentItem.weather.get(0).main));
        binding.pressure.setText(format("Pressure: %d hPa", currentItem.main.pressure));

        binding.sealevel.setText("Sea Level: " + currentItem.main.sea_level + " hPa");
        binding.ground.setText(format("Ground Level: %d hPa", currentItem.main.grnd_level));
        binding.visibility.setText(String.format("Visibility:%d km", currentItem.visibility));

        binding.cloudiness.setText("Cloudliness: " + currentItem.clouds.all + "%");
        binding.date.setText(currentItem.dt_txt);
        binding.timell.setVisibility(View.GONE);
        binding.weatherforecast.setText("Weather ForestCast");
    }


    private void getForecastData() {
        viewModel.getForecastData().observe(getViewLifecycleOwner(),new Observer<Root>(){

            @Override
            public void onChanged(Root root) {
                if(root!=null){
                    try{

                        binding.date.setText(viewModel.convertUnixToDate(root.listapi.get(0).dt_txt));
                        binding.time.setText(viewModel.convertUnixToDateTime(root.listapi.get(0).dt_txt));





                    }catch (Exception e){

                    }
                }
            }
        });
    }

    private void getWeatherData() {
        viewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<ListApi>() {

            @Override
            public void onChanged(ListApi listApi) {
                if(listApi !=null) {
                    try {
                        binding.temperature.setText(format("%s°C", listApi.main.temp));
                        binding.temperatureMin.setText(format("Min: %s°C", listApi.main.temp_min));
                        binding.temperatureMax.setText(format("Max: %s°C", listApi.main.temp_max));
                        binding.humidity.setText(format("Humidity: %d%%", listApi.main.humidity));
                        binding.feelslike.setText(format("Feels Like: %s°C", listApi.main.feels_like));
                        binding.city.setText(city);
                        binding.windSpeed.setText(format("Wind Speed: %s m/s", listApi.wind.speed));

                        binding.conditionWeather.setText(valueOf(listApi.weather.get(0).main));
                        binding.pressure.setText(format("Pressure: %d hPa", listApi.main.pressure));

                        binding.sealevel.setText("Sea Level: " + listApi.main.sea_level + " hPa");
                        binding.ground.setText(format("Ground Level: %d hPa", listApi.main.grnd_level));
                        binding.visibility.setText(String.format("Visiblity:%d km", listApi.visibility));

                        binding.cloudiness.setText("Cloudliness: " + listApi.clouds.all + " %");

                    }catch (Exception exception){
                        Log.d("Fetching","error $exception");

                    }
                }
            }
        });
    }




}