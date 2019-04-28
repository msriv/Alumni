package com.avantika.alumni.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class RecommendedOffersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommended_offers_fragment, null);
// Intent going to background service: ServerFetch.class
        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFetch.class);
        // Data sent with intent
        intent.putExtra("email", "mihir.srivastava@avantika.edu.in");
        intent.putExtra("request", "recommendedOffers");
        // Starting service using thisv
        getActivity().startService(intent);
        return rootView;
    }

    private void showRecommendedOffers(IndustryOffers[] recommendedOffers){
        RecyclerView recommendedOffersrecView = getActivity().findViewById(R.id.recommendedOffersRec);
        OffersAdapter recommendedOffersAdapter = new OffersAdapter(recommendedOffers);
        recommendedOffersrecView.setAdapter(recommendedOffersAdapter);
        recommendedOffersrecView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(recommendedOffersReciever, new IntentFilter(RECOMMENDED_INDUSTRY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(recommendedOffersReciever);
    }

    BroadcastReceiver recommendedOffersReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action: " + action);
            if (action.equalsIgnoreCase(RECOMMENDED_INDUSTRY_ACTION)) {
                String offersJson = intent.getStringExtra("recommendedOffers");
                IndustryOffers[] offers =  new Gson().fromJson(offersJson, IndustryOffers[].class);
                Log.d(TAG, "After Receiving" + offers[0].Domain_ID);
                showRecommendedOffers(offers);
            } else {
                Log.d(TAG, "Maybe useful intent action, but not of our interest");
            }

        }
    };

}
