package com.example.sridh.inclass_13;

import java.util.Comparator;

/**
 * Created by sridh on 10/24/2017.
 */

public class PriceComparator implements Comparator<Trips> {
    @Override
    public int compare(Trips filteredApp, Trips t1) {
        if(Integer.valueOf(t1.getCost())< Integer.valueOf(filteredApp.getCost()))
        {
            return -1;
        }
        else
            return 1;
    }
}
