package com.example.weatherforecastapp.ui.fragment;

import static com.example.weatherforecastapp.Constant.API_ID;
import static com.example.weatherforecastapp.ui.fragment.TodayFragment.lat;
import static com.example.weatherforecastapp.ui.fragment.TodayFragment.lon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.databinding.FragmentAirQualityBinding;
import com.example.weatherforecastapp.model.airpollution.RootResponse;
import com.example.weatherforecastapp.viewModel.WeatherViewModel;


public class AirQualityFragment extends Fragment {
private FragmentAirQualityBinding binding;
private WeatherViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAirQualityBinding.inflate(getLayoutInflater(), container, false);
        viewModel= new WeatherViewModel();
        viewModel.getAirPollution(lat,lon,API_ID);
        getAirData();
        toPutImage();

        return binding.getRoot();
    }

    private void toPutImage() {
        binding.co2img.setImageResource(R.drawable.carbon);
        binding.noimg.setImageResource(R.drawable.no);
        binding.no2img.setImageResource(R.drawable.no0);
        binding.o3img.setImageResource(R.drawable.oxygen);
        binding.pm25img.setImageResource(R.drawable.pm);
        binding.pm10img.setImageResource(R.drawable.pmo);
        binding.nh3img.setImageResource(R.drawable.ammonia);
        binding.so2img.setImageResource(R.drawable.so);

    }

    private void getAirData() {
        viewModel.getAirData().observe(getViewLifecycleOwner(), new Observer<RootResponse>() {
            @Override
            public void onChanged(RootResponse rootResponse) {
                if(!(rootResponse==null)){
                    binding.co2.setText(rootResponse.list.get(0).components.co+"");
                    binding.no.setText(rootResponse.list.get(0).components.no+"");
                    binding.no2.setText(rootResponse.list.get(0).components.no2+"");
                    binding.nh3.setText(rootResponse.list.get(0).components.nh3+"");
                    binding.so2.setText(rootResponse.list.get(0).components.so2+"");
                    binding.o3.setText(rootResponse.list.get(0).components.o3+"");
                    binding.pm10.setText(rootResponse.list.get(0).components.pm10+"");
                    binding.pm25.setText(rootResponse.list.get(0).components.pm2_5+"");
                    int aqi=rootResponse.list.get(0).main.aqi;
                    String temp="";
                   switch (aqi){
                       case 1:
                           temp="Good";
                           break;
                       case 2:
                           temp="Fair";
                           break;
                       case 3:
                           temp="Moderate";
                           break;
                       case 4:
                           temp="Poor";
                           break;
                       case 5:
                           temp="Very Poor";
                           break;
                   }
                   binding.aqiValue.setText(temp);

                }
            }
        });
    }
}