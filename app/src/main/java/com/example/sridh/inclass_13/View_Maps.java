package com.example.sridh.inclass_13;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import com.google.maps.android.PolyUtil;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;

public class View_Maps extends FragmentActivity implements OnMapReadyCallback
    , GoogleApiClient.ConnectionCallbacks
    , GoogleApiClient.OnConnectionFailedListener
    , LocationListener
    , GoogleMap.OnMarkerClickListener
    , GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private Marker currentlocationmarker;
    int[] colors = {Color.RED,Color.BLUE,Color.GREEN};
    private Location last_location;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private AppCompatDelegate delegate;
    public static final int PERMISSION_REQUEST_LC_CODE = 99;
    String url="https://maps.googleapis.com/maps/api/directions/json?";

    ArrayList<Places> places = new ArrayList<Places>();
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_LC_CODE:
                if((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //Granted
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__maps);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            check_location_permission();
        }
        Intent intent = this.getIntent();
        places = (ArrayList<Places>) intent.getExtras().get("Places");
        String wayOutput = "";
        for(int i =0;i<places.size();i++){
            if(i == 0){
                wayOutput = "&waypoints=";
                wayOutput = wayOutput + places.get(i).getLat() + "," + places.get(i).getLog();
            } else if((i>0) && (i<=places.size() - 1))  {
                wayOutput = wayOutput + "|" + places.get(i).getLat() + "," + places.get(i).getLog();
            }
        }
        String url1 = url + "origin=" + "35.227085" + "," + "-80.843124" + "&destination=" + "35.227085" + "," + "-80.843124" + wayOutput + "&key=" + MainActivity.MAPS_DIRECTION;
        Log.d("Output URL:",url1);
        new Get_data_polyline(View_Maps.this, new Get_data_polyline.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<String[]> strings) {
                for(int i =0;i<strings.size();i++){
                    String[] string = new String[strings.get(i).length];
                    string = strings.get(i);
                    for(int j =0;j<string.length;j++){
                        PolylineOptions options = new PolylineOptions();
                        options.color(colors[i%3]);
                        options.width(10);
                        options.addAll(PolyUtil.decode(string[j]));
                        mMap.addPolyline(options);
                    }
                }

            }
        }).execute(url1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        last_location = location;
        if(currentlocationmarker != null){
            currentlocationmarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currentlocationmarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        if(client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);

        }

    }

    public Boolean check_location_permission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LC_CODE);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LC_CODE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(getBaseContext(),"Latitude["+ marker.getPosition().latitude + "], Longitude[" + marker.getPosition().longitude + "]",Toast.LENGTH_SHORT).show();
    }
}
