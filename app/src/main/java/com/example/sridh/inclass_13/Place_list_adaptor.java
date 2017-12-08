package com.example.sridh.inclass_13;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sridh on 12/6/2017.
 */

public class Place_list_adaptor extends RecyclerView.Adapter<Place_list_adaptor.ViewHolder>{
    Create_Trip create_trip;
    int location_list;
    ArrayList<Places> placeDetails;
    ArrayList<String> checked = new ArrayList<String>();
    double v,v1;

    public Place_list_adaptor(Create_Trip create_trip, int location_list, ArrayList<Places> places, double v, double v1, ArrayList<String> checked) {
        this.create_trip = create_trip;
        this.location_list = location_list;
        this.placeDetails = places;
        this.v = v;
        this.v1 = v1;
        if(checked != null){
            this.checked.addAll(checked);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_adaptor, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("check",checked.toString());
        if(checked.contains(String.valueOf(position))){
            holder.checkBox.setChecked(true);
        } else  {
            holder.checkBox.setChecked(false);
        }
        holder.place_name.setText(placeDetails.get(position).getPlace_name().trim());
        holder.place_days.setText(placeDetails.get(position).getDays().trim());
        holder.place_cost.setText(placeDetails.get(position).getPlace_cost().trim());
        //holder.place_miles.setText(placeDetails.get(position).getMiles());
        holder.position = position;
        holder.create_trip = create_trip;
        Location loc1 = new Location("");
        loc1.setLongitude(v1);
        loc1.setLatitude(v);
        Location loc2 = new Location("");
        loc2.setLatitude(placeDetails.get(position).getLat());
        loc2.setLongitude(placeDetails.get(position).getLog());
        int distance = (int) (loc1.distanceTo(loc2) * 0.00062137);
        holder.place_miles.setText(String.valueOf(distance).trim());
    }
    @Override
    public int getItemCount() {
        if(placeDetails != null){
            return  placeDetails.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView place_name,place_days,place_cost,place_miles;
        int position;
        Create_Trip create_trip;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
            place_cost = (TextView) itemView.findViewById(R.id.place_cost);
            place_name = (TextView) itemView.findViewById(R.id.place_name);
            place_days = (TextView) itemView.findViewById(R.id.place_days);
            place_miles = (TextView) itemView.findViewById(R.id.place_miles);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checked.contains(String.valueOf(position))){
                        checked.remove(String.valueOf(position));
                        create_trip.Checked_function(position);
                    } else {
                        checked.add(String.valueOf(position));
                        create_trip.Checked_function(position);
                    }
                }
            });
        }
    }
}
