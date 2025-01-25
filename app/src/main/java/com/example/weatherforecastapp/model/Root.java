package com.example.weatherforecastapp.model;;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
public class Root{
    public City city;
    public int cnt;
    public String cod;
    @SerializedName("list")
    public List<ListApi> listapi;
    public int message;
}
