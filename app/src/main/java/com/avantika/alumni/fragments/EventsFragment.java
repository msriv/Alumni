package com.avantika.alumni.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Events;
import com.avantika.alumni.server.ServerFunctions;
import com.avantika.alumni.support.InformationAdapter;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.EVENTS_ACTION;

public class EventsFragment extends Fragment {
    public static final String TAG = EventsFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, null);

        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFunctions.class);
        intent.putExtra("request", "events");
        getActivity().startService(intent);

        return rootView;
    }

    private void showEvents(Events[] events) {
        RecyclerView eventsRecView = getActivity().findViewById(R.id.eventsRec);
        InformationAdapter informationAdapter = new InformationAdapter(events);
        eventsRecView.setAdapter(informationAdapter);
        eventsRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(eventsReciever, new IntentFilter(EVENTS_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(eventsReciever);
    }

    BroadcastReceiver eventsReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action: " + action);
            if (action.equalsIgnoreCase(EVENTS_ACTION)) {
                String eventsJson = intent.getStringExtra("events");
                Events[] events = new Gson().fromJson(eventsJson, Events[].class);
                Log.d(TAG, "ID1: " + events[0].Event_ID + " ID2: " + events[1].Event_ID);
                showEvents(events);
            } else {
                Log.d(TAG, "Maybe useful intent action, but not of our interest");
            }

        }
    };
}
