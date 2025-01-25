package com.example.weatherforecastapp.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherforecastapp.model.ListApi;
import com.example.weatherforecastapp.api.ApiInstance;
import com.example.weatherforecastapp.model.Root;
import com.example.weatherforecastapp.model.airpollution.RootResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel {
    private final MutableLiveData<ListApi> weatherData = new MutableLiveData<ListApi>();
    private final MutableLiveData<RootResponse> airData = new MutableLiveData<RootResponse>();
    private final MutableLiveData<Root> forecastData = new MutableLiveData<Root>();
    private final MutableLiveData<String> errorData = new MutableLiveData<>();
    public void getWeather(String city, String apiId, String units) {
        ApiInstance.getApiInterface().getWeather(city, apiId, units)
                .enqueue(new Callback<ListApi>() {
                    @Override
                    public void onResponse(Call<ListApi> call, Response<ListApi> response) {
                        if (response.isSuccessful()) {

                            Log.d("API", "Success: " + response.body());
                            weatherData.setValue(response.body());


                        } else {

                            Log.d("API", "Error: " + response.code());
                            errorData.setValue("Failed: " + response.code());

                        }
                    }

                    @Override
                    public void onFailure(Call<ListApi> call, Throwable t) {
                        Log.d("API", "Failed: " + t.getMessage());
                        errorData.setValue("Failed: " + t.getMessage());
                    }
                });

    }
    public  void getForecast(String city, String apiId, String units){
        ApiInstance.getApiInterface().getForecast(city, apiId, units)
                .enqueue(new Callback<Root>() {
                    @Override
                    public void onResponse(Call<Root> call, Response<Root> response) {
                        if (response.isSuccessful()) {
                            Log.d("API", "Success: " + response.body());

                            // Log the full response to check for the list field
                            if (response.body() != null) {
                                // Check if 'list' exists and has data
                                if (response.body().listapi != null && !response.body().listapi.isEmpty()) {
                                    Log.d("API", "listapi size: " + response.body().listapi.size());
                                } else {
                                    Log.d("API", "listapi is null or empty");
                                }
                            }
                            forecastData.setValue(response.body());
                        } else {
                            Log.d("API", "Error: " + response.code());
                            errorData.setValue("Failed: " + response.code());
                        }
                    }



                    @Override
                    public void onFailure(Call<Root> call, Throwable t) {
                        Log.d("API", "Failed: " + t.getMessage());
                        errorData.setValue("Failed: " + t.getMessage());



                    }
                });
    }

    public void getAirPollution(Double lat, Double lon, String apiKey) {
        ApiInstance.getApiInterface().getAirPollution(lat, lon, apiKey)
                .enqueue(new Callback<RootResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RootResponse> call, Response<RootResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d("API", "Success air pollution data");
                            airData.setValue(response.body());
                        } else {
                            Log.d("API", "Failed air pollution data: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<RootResponse> call, Throwable t) {
                        Log.d("API", "API call failed: " + t.getMessage());
                    }
                });
    }

    public LiveData<ListApi> getWeatherData() {
        return weatherData;
    }
    public LiveData<RootResponse> getAirData() {
        return airData;
    }
    public LiveData<Root> getForecastData() {
        return forecastData;
    }


    public LiveData<String> getErrorData() {
        return errorData;
    }
    public String convertUnixTotime(int unixTimestamp) {
        Date date = new Date((long) unixTimestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }


    public String convertUnixToDate(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        Date date = new Date();
        return sdf.format(date);
    }
    public String convertUnixToDateTime(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        Date date = new Date();
        return sdf.format(date);
    }

}
