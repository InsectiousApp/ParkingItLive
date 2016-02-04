package com.example.codev.parkitlive;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {



    Button bregister, bmaps, brefresh;
    private static String url = "https://api.thingspeak.com/channels/82010/feeds/last";

    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);


        bregister = (Button) findViewById(R.id.bregister);
        bmaps = (Button) findViewById(R.id.bpark);
        brefresh=(Button)findViewById(R.id.brefresh);


        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        bmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });


        brefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               callAsynchronousTask();

            }
        });

    }

    String data=null;

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
            brefresh.setText("Refresh: " + result);
            Toast.makeText(getApplicationContext(), "updated :"+result, Toast.LENGTH_SHORT).show();
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






