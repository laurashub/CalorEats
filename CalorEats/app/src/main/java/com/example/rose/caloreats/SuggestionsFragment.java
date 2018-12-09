package com.example.rose.caloreats;

//lots of this code modified from https://stackoverflow.com/questions/42413308/java-lang-illegalargumentexception-invalid-provider-null-when-openning-mapsact

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;

import android.content.pm.PackageManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.location.Geocoder;
import android.location.Criteria;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import android.Manifest;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.net.URL;
import java.util.SortedMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.database.Cursor;

public class SuggestionsFragment extends Fragment {

    SupportMapFragment mapFragment;
    MapHolder mapHolder;
    LocationManager lm;
    LocationListener ll;
    SQLiteDatabase db;

    // newInstance constructor for creating fragment with arguments
    public static SuggestionsFragment newInstance(String title) {
        SuggestionsFragment suggestionsFragment = new SuggestionsFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        suggestionsFragment.setArguments(args);
        return suggestionsFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.suggestion_layout, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            Log.e("DB", "Fail to create database");
        }
        db = dbHelper.getReadableDatabase();

        mapFragment = SupportMapFragment.newInstance();
        mapHolder = new MapHolder(getContext());
        mapFragment.getMapAsync(mapHolder);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment, mapFragment)
                .hide(mapFragment)
                .commit();

        Button suggest = (Button) view.findViewById(R.id.suggest);
        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getMyLocation();
            }
        });

        return view;
    }

    public void getMyLocation() {
        System.out.println("Finding closest restaurant");
        // Acquire a reference to the system Location Manager
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        ll = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                calculateDistances(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        } else {
            System.out.println("Location manager requesting location updates");
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
        }
        // Register the listener with the Location Manager to receive location updates


    }

    public void calculateDistances(Location location){
        lm.removeUpdates(ll); //job is done for now, don't waste energy listening
        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());


        //get location information
        String table = "locations";
        String columns = "locations._id as _id, address, phone";

        ArrayList<String> where = new ArrayList<String>();
        ArrayList<String> args = new ArrayList<String>();
        String queryString = "";


        Cursor c = db.query(table, columns.split(","), "",
                args.toArray(new String[0]), "", "", "");

        ArrayList<String> addresses = new ArrayList<>();


        if (c == null) {
            System.out.println("Something's wrong - location :(");
        } else {
            while(c.moveToNext()) {
                String address = c.getString(c.getColumnIndexOrThrow("address"));
                addresses.add(address);

            }
        }

        //convert to LatLng
        HashMap<String, LatLng> mappings = new HashMap<>();
        mapHolder.getLatLngs(myPos, addresses, mappings, this);


    }

    public void checkReady(LatLng myPos, HashMap<String, LatLng> mappings){
        if (mappings.entrySet().size() == 17){
            getDistanceInformation(myPos, mappings);
        }
    }


    public void getDistanceInformation(LatLng myPos, HashMap<String, LatLng> mappings) {
       //calculate distance to each based on difference between latitude and longitude
        HashMap<Double, String> distanceMap = new HashMap<>();
        ArrayList<Double> distanceList = new ArrayList<>();
        double minDist = Integer.MAX_VALUE;
        for (String address : mappings.keySet()){
            double currentDistance = findDistance(myPos, mappings.get(address));
            distanceMap.put(currentDistance, address);
            distanceList.add(currentDistance);
        }

        Collections.sort(distanceList);

        ArrayList<String> sortedList = new ArrayList<>();
        for (Double dist : distanceList){
            sortedList.add(distanceMap.get(dist));
        }

        for (String address : sortedList){
            System.out.println("Address: " + address);
        }
    }

    public Double findDistance(LatLng myPos, LatLng otherPos){
        double x1 = myPos.latitude;
        double y1 =  myPos.longitude;
        double x2 = otherPos.latitude;
        double y2 =  otherPos.longitude;

        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }



    //should never get into this because the permisison is in the manifest but Android studio complains
    //if I don't error check it
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted
                        System.out.println("permission granted!");

                        //try again
                        getMyLocation();
                    } else {

                        Toast.makeText(getContext(), "Unable to provide suggestions", Toast.LENGTH_LONG);
                    }
                }
            }
        }
    /*
        https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=YOUR_API_KEY
        origin*/
}
