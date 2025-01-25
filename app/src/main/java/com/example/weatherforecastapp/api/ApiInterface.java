package com.example.weatherforecastapp.api;

import com.example.weatherforecastapp.model.ListApi;
import com.example.weatherforecastapp.model.Root;
import com.example.weatherforecastapp.model.airpollution.RootResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("forecast")
    Call<Root>getForecast(
            @Query("q") String city,
            @Query("appid") String appid,
            @Query("units") String units

    );

    @GET("weather")
    Call<ListApi>getWeather(
            @Query("q") String city,
            @Query("appid") String appid,
            @Query("units") String units

    );
    @GET("air_pollution")
    Call<RootResponse>getAirPollution(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("appid") String appid

    );
}
