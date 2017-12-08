package com.example.sridh.inclass_13;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sridh on 12/6/2017.
 */

public class Trips implements Serializable, Comparable<Trips>{
    String trip_name,no_of_people,cost;
    ArrayList<Places> places_added;

    public Trips() {

    }

    @Override
    public String toString() {
        return "Trips{" +
                "trip_name='" + trip_name + '\'' +
                ", no_of_people='" + no_of_people + '\'' +
                ", cost='" + cost + '\'' +
                ", places_added=" + places_added +
                '}';
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getNo_of_people() {
        return no_of_people;
    }

    public void setNo_of_people(String no_of_people) {
        this.no_of_people = no_of_people;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public ArrayList<Places> getPlaces_added() {
        return places_added;
    }

    public void setPlaces_added(ArrayList<Places> places_added) {
        this.places_added = places_added;
    }


    public Trips(String trip_name, String no_of_people,String cost, ArrayList<Places> places_added) {
        this.trip_name = trip_name;
        this.no_of_people = no_of_people;
        this.cost = cost;
        this.places_added = places_added;
    }

    @Override
    public int compareTo(@NonNull Trips o) {
        if(Integer.valueOf(cost) > Integer.valueOf(o.getCost()))
        {
            return 1;
        }
        else
            return -1;
    }
}
