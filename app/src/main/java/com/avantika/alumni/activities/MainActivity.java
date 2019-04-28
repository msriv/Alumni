package com.avantika.alumni.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Authentication;
import com.avantika.alumni.parameters.Events;
import com.avantika.alumni.parameters.News;
import com.avantika.alumni.server.ServerFetch;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.AUTHENTICATION_ACTION;
import static com.avantika.alumni.parameters.Intents.EVENTS_ACTION;
import static com.avantika.alumni.parameters.Intents.NEWS_ACTION;


public class MainActivity extends AppCompatActivity {


    public static final String TAG = MainActivity.class.getSimpleName();
    public Authentication userProfile;
    public News newsArticles;
    public Events eventsArticles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ServerFetch.class);
        intent.putExtra("email", "mihir.srivastava@avantika.edu.in");
        intent.putExtra("request", "news");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(reciever, new IntentFilter(NEWS_ACTION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(reciever);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(reciever, new IntentFilter(NEWS_ACTION));
    }

    BroadcastReceiver reciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, action);
            if (action.equalsIgnoreCase(AUTHENTICATION_ACTION)){
                Log.d(TAG, "action: " + action);
                String profileJson = intent.getStringExtra("profile");
                userProfile = new Gson().fromJson(profileJson, Authentication.class);
            }
        }
    };
}

