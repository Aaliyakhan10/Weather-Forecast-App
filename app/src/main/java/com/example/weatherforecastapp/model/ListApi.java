package com.example.weatherforecastapp.model;
import java.io.Serializable;
import java.util.ArrayList;

public class ListApi implements Serializable {
    public int dt;
    public Main main;
    public ArrayList<Weather> weather;
    public Clouds clouds;
    public Wind wind;
    public int visibility;
    public int pop;
    public Sys sys;
    public String dt_txt;


}
