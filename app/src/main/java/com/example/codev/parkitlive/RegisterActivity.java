package com.example.codev.parkitlive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    String regId;
    static final String SENDER_ID = "925713359044";


    public String getRegIdFromSharedPreferences() {
        SharedPreferences preferences = this.getSharedPreferences("gcm", Context.MODE_PRIVATE);
        return preferences.getString("gcm_reg", null);
    }

    public void saveRegIdInSharedPreferences(String s) {
        SharedPreferences.Editor preferences = this.getSharedPreferences("gcm", Context.MODE_PRIVATE).edit();
        preferences.putString("gcm_reg", s);
        preferences.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        regId = getRegIdFromSharedPreferences();
        if (regId == null) {
            final Activity a = this;
            AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(a);
                    String registerId = null;
                    try {
                        registerId = gcm.register(SENDER_ID);
                        Log.i("idddddd", registerId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return registerId;
                }

                @Override
                protected void onPostExecute(String s) {
                    Log.i("registration id", s);
                    Toast.makeText(a, s, Toast.LENGTH_LONG).show();

                    saveRegIdInSharedPreferences(s);
                }
            };
            task.execute();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
