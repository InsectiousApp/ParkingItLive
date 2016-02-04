package com.example.codev.parkitlive;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Activity a=this;


    Button bstartserver;
    AsyncTask<Void, Void, String> task;
    int globalCount=0;
    LatLng MSIT = new LatLng(28.621106, 77.091703);
    LatLng MSIT2 = new LatLng(28.621500, 77.090945);


    Button bregister, bmaps, brefresh;
    private static String url = "https://api.thingspeak.com/channels/82010/feeds/last";

    JSONArray contacts = null;
    Location location;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        callAsynchronousTask();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }






    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }

        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings=googleMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 0, new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
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

        );


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
    //    String provider;
      //  provider = locationManager.getBestProvider(criteria, true);
    //    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, (LocationListener) this);
    //    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);



      //  Location location = locationManager.get
        Log.i("locciiiii", location.toString());


      //  mMap.addMarker(new MarkerOptions().position(MSIT).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621626,77.091436)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621628,77.091418)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621625,77.091397)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621621,77.091380)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621620,77.091361)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621615,77.091326)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621614,77.091310)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621611,77.091294)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.621604,77.091280)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(MSIT2).title("hey"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(19.0f));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                Navigator navigator = new Navigator(mMap, new LatLng(location.getLatitude(), location.getLongitude()), latLng);
                navigator.findDirections(false);
                return false;
            }
        });

    }






    String data=null;
    int lastValue=0;

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);
//            Toast.makeText(getApplicationContext(), "abc", Toast.LENGTH_SHORT).show();
            if (jsonStr != null) {
                try {
                    JSONObject c = new JSONObject(jsonStr);

                    String id = c.getString("entry_id");
                    String created = c.getString("created_at");
                    String field1 = c.getString("field1");
                    String field2 = c.getString("field2");

                    Log.i("valuessss", id + " " + created + " " + field1 + " " + field2);

                    data=field1;



                    HashMap<String, String> contact = new HashMap<String, String>();

                    contact.put("entry_id", id);
                    contact.put("created_at", created);
                    contact.put("field1", field1);
                    contact.put("field2", field2);

                    // adding contact to contact list
                    //   contactList.add(contact);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progres
            /**
             * Updating parsed JSON data into ListView
             * */
            Log.i("logggggggg", String.valueOf(result));
            //   brefresh.setText("Refresh: " + result);
            //   Toast.makeText(getApplicationContext(), "updated :"+result, Toast.LENGTH_SHORT).show();

            int value=Integer.parseInt(result);

        if(value!=lastValue) {
            Log.i("valuessssssss", result+" "+String.valueOf(lastValue));
            if (value > 5 && value < 30) {
                Log.i("logggggggg", "smaller than 60");

                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(MSIT2).title("hey"));
            } else {
                Log.i("logggggggg", "greater than 60");
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(MSIT2).title("hey"));
            }
        }
            Log.i("valuessssssss", String.valueOf(lastValue));
            lastValue=value;

        }
    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            // PerformBackgroundTask performBackgroundTask = new PerformBackgroundTask();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            // performBackgroundTask.execute();
                            new GetContacts().execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 7000); //execute in every 50000 ms
    }





}
