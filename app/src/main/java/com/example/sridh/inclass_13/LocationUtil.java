package com.example.sridh.inclass_13;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sridh on 11/23/2017.
 */

public class LocationUtil {
    static public class LocationJSONParser{
        static ArrayList<String[]> parsePolyLine(String in) throws JSONException {
            JSONObject root = new JSONObject(in);
            if(root.has("status")){
                Log.d("status:","getting");
                if(root.getString("status").equals("OK")){
                    Log.d("status ok:","getting");
                    JSONArray routesJSONArray = root.getJSONArray("routes");
                    JSONObject routesJSONObject=routesJSONArray.getJSONObject(0);
                    JSONArray legsJSONArray = routesJSONObject.getJSONArray("legs");
                    ArrayList<String[]> PolylinesString = new ArrayList<String[]>();
                    for(int j =0;j<legsJSONArray.length();j++){
                        Log.d("JSONArray",legsJSONArray.toString());
                        JSONObject legsJSONObject = legsJSONArray.getJSONObject(j);
                        JSONArray stepsJSONArray = legsJSONObject.getJSONArray("steps");
                        String[] stringsArray = new String[stepsJSONArray.length()];
                        for(int i=0;i<stepsJSONArray.length();i++ ){
                            JSONObject stepsJSONObject = stepsJSONArray.getJSONObject(i);
                            JSONObject polylineJSONObject = stepsJSONObject.getJSONObject("polyline");
                            String string = new String();
                            string = polylineJSONObject.getString("points");
                            stringsArray[i] = string;
                        }
                        PolylinesString.add(stringsArray);
                    }
                    return PolylinesString;
                }
            }
            return null;
        }
    }
}
