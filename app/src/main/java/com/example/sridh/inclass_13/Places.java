package com.example.sridh.inclass_13;

import java.io.Serializable;

/**
 * Created by sridh on 12/6/2017.
 */

public class Places implements Serializable{
    String place_name,days,place_cost,miles;
    double lat;
    double log;

    public Places(String place_cost, String days, double lat, double log, String place_name) {
        this.place_name = place_name;
        this.days = days;
        this.place_cost = place_cost;
        this.lat = lat;
        this.log = log;
    }

    public Places() {

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPlace_cost() {
        return place_cost;
    }

    public void setPlace_cost(String place_cost) {
        this.place_cost = place_cost;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    @Override
    public String toString() {
        return "Places{" +
                "place_name='" + place_name + '\'' +
                ", days='" + days + '\'' +
                ", place_cost='" + place_cost + '\'' +
                ", miles='" + miles + '\'' +
                ", lat=" + lat +
                ", log=" + log +
                '}';
    }
}
