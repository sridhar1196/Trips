package com.example.sridh.inclass_13;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sridh on 12/6/2017.
 */

public class Trips_list_adaptor extends RecyclerView.Adapter<Trips_list_adaptor.ViewHolder>{
    MainActivity mainActivity;
    int location_list;
    ArrayList<Trips> tripsDetails;
    public Trips_list_adaptor(MainActivity mainActivity, int location_list, ArrayList<Trips> trips) {
        this.mainActivity = mainActivity;
        this.location_list = location_list;
        this.tripsDetails = trips;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trips_list_adaptor, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.trip_list_name.setText(tripsDetails.get(position).getTrip_name());
        holder.trip_cost_list.setText(tripsDetails.get(position).getCost());
        holder.maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.view_trip(position);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.edit_trip(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.delete_trip(position);
            }
        });
        holder.position = position;
        holder.mainActivity = mainActivity;
    }
    @Override
    public int getItemCount() {
        if(tripsDetails != null){
            return  tripsDetails.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trip_list_name,trip_cost_list;
        ImageView maps,edit,delete;
        int position;
        MainActivity mainActivity;
        public ViewHolder(View itemView) {
            super(itemView);
            trip_list_name = (TextView) itemView.findViewById(R.id.trip_list_name);
            trip_cost_list = (TextView) itemView.findViewById(R.id.trip_cost_list);
            maps = (ImageView) itemView.findViewById(R.id.maps);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            delete = (ImageView) itemView.findViewById(R.id.delete);

        }
    }
}