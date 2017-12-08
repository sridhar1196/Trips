package com.example.sridh.inclass_13;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> sort_list = new ArrayList<String>(Arrays.asList("Select","Cost:Low to High","Cost: High to Low"));
    String sort_Selected = "Select";
    final static int ADD_LOCATION = 100;
    public final static String MAPS_DIRECTION = "AIzaSyBhc7DLBHWEGsp-NwmIefjQF8n7I88_IUU";
    final static int EDIT_LOCATION = 1;
    final static int MAPS = 10;
    ArrayList<Trips> trips = new ArrayList<Trips>();
    RecyclerView.Adapter mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    FirebaseAuth mAuth;
    public static final String VALUE_KEY="value";
    public static final String CHANGE_KEY="change_key";
    public static int change_trip = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Trips");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//            if(listUsers.size()>0)
//            {/
                Log.d("demo","inside");
                if(dataSnapshot.getChildrenCount() > 0){
                    trips.clear();
                    Log.d("demo","inside if");
                    for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        Log.d("trips",postSnapShot.toString());
                        Trips trip = new Trips();
                        trip.setCost(postSnapShot.child("cost").getValue().toString().trim());
                        trip.setNo_of_people(postSnapShot.child("no_of_people").getValue().toString());
                        GenericTypeIndicator<ArrayList<Places>> genericTypeIndicator =new GenericTypeIndicator<ArrayList<Places>>(){};

                        trip.setPlaces_added(postSnapShot.child("places_added").getValue(genericTypeIndicator));
                        trip.setTrip_name(postSnapShot.child("trip_name").getValue().toString().trim());
                        trips.add(trip);
                        mRecyclerView = (RecyclerView) findViewById(R.id.tripList);
                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(MainActivity.this);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new Trips_list_adaptor(MainActivity.this, R.layout.trips_list_adaptor, trips);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {

                }
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.tripList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Trips_list_adaptor(MainActivity.this, R.layout.trips_list_adaptor, trips);
        mRecyclerView.setAdapter(mAdapter);
        final Spinner sort_Spinner = (Spinner) findViewById(R.id.sortby);
        final ArrayAdapter<String> sort_Adaptor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sort_list);
        sort_Adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_Spinner.setAdapter(sort_Adaptor);
        sort_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sort_Selected = adapterView.getItemAtPosition(i).toString().trim();
                if(sort_Selected.equals("Cost:Low to High")){
                    Collections.sort(trips);
                    mAdapter.notifyDataSetChanged();
                } else if(sort_Selected.equals("Cost: High to Low")){
                    Collections.sort(trips, new PriceComparator());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        findViewById(R.id.add_trips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Create_Trip.class);
                intent.putExtra("Type","new");
                startActivityForResult(intent,ADD_LOCATION);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ADD_LOCATION:
                if(resultCode == RESULT_OK){

                    Trips loca = new Trips();
                    loca = (Trips) data.getExtras().getSerializable(VALUE_KEY);
                    trips.add(loca);
                    DatabaseReference update = FirebaseDatabase.getInstance().getReference().child("Trips");
                    update.setValue(trips);
                    Toast.makeText(MainActivity.this,"New trip is added",Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case EDIT_LOCATION:
                if(resultCode == RESULT_OK){

                    Trips loca = new Trips();
                    loca = (Trips) data.getExtras().getSerializable(CHANGE_KEY);
                    trips.remove(change_trip);
                    trips.add(loca);
                    DatabaseReference update = FirebaseDatabase.getInstance().getReference().child("Trips");
                    update.setValue(trips);
                    Toast.makeText(MainActivity.this,"New trip is added",Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
    public void edit_trip(int position){
        Intent intent = new Intent(MainActivity.this,Create_Trip.class);
        intent.putExtra("Type","edit");
        intent.putExtra("Name",trips.get(position).getTrip_name());
        intent.putExtra("Size",trips.get(position).getPlaces_added().size());
        intent.putExtra("Places",trips.get(position).getPlaces_added());
        intent.putExtra("People",trips.get(position).getNo_of_people());
        intent.putExtra("Cost",trips.get(position).getCost());
        change_trip = position;
        startActivityForResult(intent,EDIT_LOCATION);
    }
    public void view_trip(int position){
        Intent intent = new Intent(MainActivity.this,View_Maps.class);
        intent.putExtra("Places",trips.get(position).getPlaces_added());
        startActivityForResult(intent,MAPS);
    }
    public void delete_trip(int position){
        trips.remove(position);
        DatabaseReference update = FirebaseDatabase.getInstance().getReference().child("Trips");
        update.setValue(trips);
        Toast.makeText(MainActivity.this,"Trip is deleted",Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
    }
}
