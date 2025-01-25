package com.example.weatherforecastapp.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.model.ListApi;
import com.example.weatherforecastapp.viewModel.WeatherViewModel;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<ListApi> hourlyWeatherList;


    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        public TextView dateText;
        public TextView temperatureText;
        public TextView weatherDescription;
        public TextView weather;
        public TextView min_max;
        public ImageView weatherIcon;

        public WeatherViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.dateText);
            temperatureText = view.findViewById(R.id.temperatureText);
            weatherDescription = view.findViewById(R.id.weatherDescription);
            weather = view.findViewById(R.id.weather);
            min_max = view.findViewById(R.id.Max_Min_temperatureText);
            weatherIcon = view.findViewById(R.id.weatherIcon);



        }
    }

    public WeatherAdapter(List<ListApi> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        ListApi currentItem = hourlyWeatherList.get(position);
        Log.d("WeatherAdapter", "Binding data for position " + position);
        Log.d("WeatherAdapter", "Date: " + currentItem.dt_txt);
        Log.d("WeatherAdapter", "Temp: " + currentItem.main.temp);
       anim(holder.itemView);
        // Ensure there is data
        if (currentItem != null) {
            holder.dateText.setText(currentItem.dt_txt);
            holder.temperatureText.setText("Temp: " + currentItem.main.temp + "°C");
            holder.weatherDescription.setText(currentItem.weather.get(0).description);
            holder.weather.setText(currentItem.weather.get(0).main);
            holder.min_max.setText("Min/Max Temp: "+currentItem.main.temp_min+"/"+currentItem.main.temp_max+"°C");

            Glide.with(holder.itemView.getContext())
                    .load("https://openweathermap.org/img/wn/" + currentItem.weather.get(0).icon + "@2x.png")
                    .into(holder.weatherIcon);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("current forecast", currentItem);
                NavController navController = Navigation.findNavController(view);

                navController.navigate(R.id.detailFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
        // In onChanged method of getForecastData


    }
    private void  anim(View view){
        Animation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(1500);
        view.startAnimation(animation);

    }
}
