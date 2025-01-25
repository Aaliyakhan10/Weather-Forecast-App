package com.example.weatherforecastapp.model.airpollution;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RootResponse {
    public Coord coord;
    @SerializedName("list")
    public ArrayList<ListAirApi> list;
}
