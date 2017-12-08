package com.example.sridh.inclass_13;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static com.example.sridh.inclass_13.MainActivity.MAPS;

public class Create_Trip extends AppCompatActivity implements LocationListener {
    ArrayList<Places> places = new ArrayList<Places>();
    RecyclerView.Adapter mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    FirebaseAuth mAuth;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public static final int PERMISSION_REQUEST_LC_CODE = 99;
    public static final int PERMISSION_REQUEST_NC_CODE = 98;
    ArrayList<String> checked = new ArrayList<String>();
    Boolean Create_new = true;

    //    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__trip);
        Intent intent = this.getIntent();
        final Trips trips = new Trips();
        EditText peoples = (EditText) findViewById(R.id.trip_people);
        final ArrayList<Places> places1 = new ArrayList<>();
        EditText name = (EditText) findViewById(R.id.trip_name);
        TextView cost = (TextView) findViewById(R.id.trip_cost);
        int size = 0;
        if(intent.getExtras().get("Type").toString().equals("new")){
            Create_new = true;
        } else {
            Create_new = false;
            name.setText((String) intent.getExtras().getString("Name"));
            trips.setPlaces_added((ArrayList<Places>) intent.getExtras().getSerializable("Places"));
            peoples.setText((String) intent.getExtras().getString("People"));
            cost.setText((String) intent.getExtras().getString("Cost"));
        }
        places1.addAll(trips.getPlaces_added());
        mAuth = FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            check_location_permission();
        }
        peoples.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText peoples = (EditText) findViewById(R.id.trip_people);
                TextView total_cost = (TextView) findViewById(R.id.trip_cost);
                if(peoples.getText().toString() != null){
                    if(!(peoples.getText().toString().isEmpty())){
                        int cost = 0;
                        for(int i = 0;i<checked.size();i++){
                            cost = cost + Integer.parseInt(places.get(Integer.parseInt(checked.get(i))).getPlace_cost());
                        }
                        total_cost.setText(String.valueOf(cost * Integer.parseInt(peoples.getText().toString())));
                    }
                } else {
                    total_cost.setText("");
                }
            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission","Accepted");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Deals");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//            if(listUsers.size()>0)
//            {/
                Log.d("demo","inside");
                if(dataSnapshot.getChildrenCount() > 0){
                    places.clear();
                    Log.d("demo","inside if");
                    int count = 0;
                    for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        Places pla = new Places(postSnapShot.child("Cost").getValue().toString().trim()
                                                ,postSnapShot.child("Duration").getValue().toString().trim()
                                                ,Double.parseDouble(postSnapShot.child("Location").child("Lat").getValue().toString().trim())
                                                ,Double.parseDouble(postSnapShot.child("Location").child("Lon").getValue().toString().trim())
                                                ,postSnapShot.child("Place").getValue().toString().trim());
                        Log.d("Postsnapshot",pla.toString());
                        //Places trip = postSnapShot.getValue(Places.class);
                        places.add(pla);
                        count = count + 1;
                        if(!Create_new){
                            for(int i = 0;i<trips.getPlaces_added().size();i++){
                                if(places1.get(i).getPlace_name().trim().equals(pla.getPlace_name().trim())){
                                    checked.add(String.valueOf(count - 1));
                                }
                            }
                        }
                        mRecyclerView = (RecyclerView) findViewById(R.id.places_list);
                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(Create_Trip.this);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new Place_list_adaptor(Create_Trip.this, R.layout.trips_list_adaptor, places, 35.227085, -80.843124,checked);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {

                }
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.places_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Place_list_adaptor(Create_Trip.this, R.layout.trips_list_adaptor, places,35.227085, -80.843124,checked);
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.view_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checked == null){
                    Toast.makeText(Create_Trip.this,"Add places to view trip",Toast.LENGTH_SHORT).show();
                } else if(checked.size() == 0){
                    Toast.makeText(Create_Trip.this,"Add places to view trip",Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Places> trip_place = new ArrayList<Places>();
                    for(int x = 0;x<checked.size();x++){
                        trip_place.add(places.get(Integer.parseInt(checked.get(x))));
                    }
                    Intent intent = new Intent(Create_Trip.this,View_Maps.class);
                    intent.putExtra("Places",trip_place);
                    startActivityForResult(intent,MAPS);
                }
            }
        });
        findViewById(R.id.add_to_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.trip_name);
                EditText people = (EditText) findViewById(R.id.trip_people);
                TextView cost = (TextView) findViewById(R.id.trip_cost);
                if(name == null){
                    Toast.makeText(Create_Trip.this,"Trip name cannot be empty",Toast.LENGTH_SHORT).show();
                } else if(people == null){
                    Toast.makeText(Create_Trip.this,"No of people cannot be empty",Toast.LENGTH_SHORT).show();
                } else if(name.getText().toString().isEmpty()){
                    Toast.makeText(Create_Trip.this,"Trip name cannot be empty",Toast.LENGTH_SHORT).show();
                } else if(people.getText().toString().isEmpty()){
                    Toast.makeText(Create_Trip.this,"No of people cannot be empty",Toast.LENGTH_SHORT).show();
                } else if(checked.size() == 0){
                    Toast.makeText(Create_Trip.this,"Select atleast one trip",Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Places> checked_trips = new ArrayList<Places>();
                    for(int i = 0; i<checked.size();i++){
                        checked_trips.add(places.get(Integer.parseInt(checked.get(i))));
                    }
                    Trips trips = new Trips(name.getText().toString(),people.getText().toString(), cost.getText().toString(),checked_trips);
                    Intent intent=new Intent();
                    if(Create_new){
                        intent.putExtra(MainActivity.VALUE_KEY,trips );
                    } else {
                        intent.putExtra(MainActivity.CHANGE_KEY,trips );
                    }
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        findViewById(R.id.cancel_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void Checked_function(int checkedList){
        if(checked.contains(String.valueOf(checkedList))){
            checked.remove(String.valueOf(checkedList));
        } else {
            checked.add(String.valueOf(checkedList));
        }
        EditText peoples = (EditText) findViewById(R.id.trip_people);
        TextView total_cost = (TextView) findViewById(R.id.trip_cost);
        if(peoples.getText().toString() != null){
            if(!(peoples.getText().toString().isEmpty())){
                int cost = 0;
                for(int i = 0;i<checked.size();i++){
                    cost = cost + Integer.parseInt(places.get(Integer.parseInt(checked.get(i))).getPlace_cost());
                }
                total_cost.setText(String.valueOf(cost * Integer.valueOf(peoples.getText().toString())));
            }
        } else {
            total_cost.setText("");
        }
        //mAdapter.notifyDataSetChanged();
    }
    public Boolean check_location_permission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d("Permission","not Accepted1");
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LC_CODE);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LC_CODE);
            }
            return false;
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d("Permission","not Accepted2");
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_NC_CODE);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_NC_CODE);
            }
            return false;
        } else {
            Log.d("Permission","Accepted1");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_LC_CODE:
                if((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //Granted
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        if(client == null){
//                            buildGoogleApiClient();
//                        }
//                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_REQUEST_NC_CODE:
                if((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //Granted
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        if(client == null){
//                            buildGoogleApiClient();
//                        }
//                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("Lattitude",String.valueOf(location.getLatitude()));
        Log.d("Longitude",String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
