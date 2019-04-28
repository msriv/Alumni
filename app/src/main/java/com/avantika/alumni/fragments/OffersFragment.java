package com.avantika.alumni.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.avantika.alumni.support.PagerAdapter;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.ALL_INDUSTRY_ACTION;
import static com.avantika.alumni.parameters.Intents.RECOMMENDED_INDUSTRY_ACTION;
import static com.avantika.alumni.support.OffersAdapter.TAG;

public class OffersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_offers, null);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) rootView.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);




        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new RecommendedOffersFragment(), "Recommended Offers");
        adapter.addFragment(new AllOffersFragment(), "All Offers");
        viewPager.setAdapter(adapter);
    }










}
