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
import com.avantika.alumni.parameters.IndustryOffers;
import com.avantika.alumni.server.ServerFetch;
import com.avantika.alumni.support.OffersAdapter;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.ALL_INDUSTRY_ACTION;
import static com.avantika.alumni.parameters.Intents.RECOMMENDED_INDUSTRY_ACTION;
import static com.avantika.alumni.support.OffersAdapter.TAG;

public class AllOffersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.all_offers_fragment, null);

        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFetch.class);
        intent.putExtra("request", "allOffers");
        getActivity().startService(intent);

        return rootView;
    }

    private void showAllOffers(IndustryOffers[] allOffers){
        RecyclerView allOffersrecView = getActivity().findViewById(R.id.allOffersRec);
        OffersAdapter allOffersAdapter = new OffersAdapter(allOffers);
        allOffersrecView.setAdapter(allOffersAdapter);
        allOffersrecView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(allOffersReciever, new IntentFilter(ALL_INDUSTRY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(allOffersReciever);
    }

    BroadcastReceiver allOffersReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action: " + action);
            if (action.equalsIgnoreCase(ALL_INDUSTRY_ACTION)) {
                String offersJson = intent.getStringExtra("allOffers");
                IndustryOffers[] offers =  new Gson().fromJson(offersJson, IndustryOffers[].class);
                Log.d(TAG, "After Receiving" + offers[0].Domain_ID);
                showAllOffers(offers);
            } else {
                Log.d(TAG, "Maybe useful intent action, but not of our interest");
            }

        }
    };

}
