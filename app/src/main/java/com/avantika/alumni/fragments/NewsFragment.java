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
import com.avantika.alumni.parameters.News;
import com.avantika.alumni.server.ServerFunctions;
import com.avantika.alumni.support.InformationAdapter;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.NEWS_ACTION;

public class NewsFragment extends Fragment {
    public static final String TAG = NewsFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, null);

        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFunctions.class);

        intent.putExtra("request", "news");
        getActivity().startService(intent);

        return rootView;
    }

    private void showNews(News[] news) {
        RecyclerView newsRecView = getActivity().findViewById(R.id.newsRec);
        InformationAdapter informationAdapter = new InformationAdapter(news);
        newsRecView.setAdapter(informationAdapter);
        newsRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(newsReciever, new IntentFilter(NEWS_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(newsReciever);
    }

    BroadcastReceiver newsReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action: " + action);
            if (action.equalsIgnoreCase(NEWS_ACTION)) {
                String newsJson = intent.getStringExtra("news");
                News[] news = new Gson().fromJson(newsJson, News[].class);
                showNews(news);
            } else {
                Log.d(TAG, "Maybe useful intent action, but not of our interest");
            }

        }
    };
}
