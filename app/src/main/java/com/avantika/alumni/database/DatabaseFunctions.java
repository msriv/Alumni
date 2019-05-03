package com.avantika.alumni.database;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.avantika.alumni.parameters.Industry_Domains;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.DOMAIN_LIST_ACTION;

public class DatabaseFunctions extends IntentService {

    public static final String TAG = "DatabaseFunctions";
    private AlumniDatabase db;

    DatabaseFunctions() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Database Service Starting");
        db = Room.databaseBuilder(getApplicationContext(),
                AlumniDatabase.class, "alumni.db").fallbackToDestructiveMigration().build();

        String request = intent.getStringExtra("request");
        switch (request) {
            case "domainList": {
                Industry_Domains[] domainList = new Gson().fromJson
                        (intent.getStringExtra("domainList"), Industry_Domains[].class);
                Log.d(TAG, "Domain List: " + domainList);
                db.domainDao().deleteAllDomains();
                db.domainDao().insertAll(domainList);
                Intent returningIntent = new Intent(DOMAIN_LIST_ACTION);
                String domainListJson = new Gson().toJson(db.domainDao().getDomainList());
                returningIntent.putExtra("domainList", domainListJson);
                Log.d(TAG, "sending intent..." + domainListJson);
                sendBroadcast(returningIntent);
            }
            break;
        }
    }
}
