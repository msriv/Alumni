package com.avantika.alumni.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.WallPosts;
import com.avantika.alumni.server.ServerFetch;
import com.avantika.alumni.support.WallPostAdapter;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.POSTS_ACTION;
import static com.avantika.alumni.support.OffersAdapter.TAG;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, null);

        getPostsFromServer();

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            /*Snackbar.make(v, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
            final Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setTitle("Send Posts");
            ImageView img = dialog.findViewById(R.id.uploadedImage);
            img.setImageResource(R.drawable.project_diagram);

            Button sendBtn = dialog.findViewById(R.id.sendBtn);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "New Post Made", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        });
        return rootView;
    }

    private void savePostsToServer(String content) {

    }

    private void getPostsFromServer() {
        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFetch.class);
        intent.putExtra("request", "posts");
        getActivity().startService(intent);
    }

    private void showAllPosts(WallPosts[] posts) {
        RecyclerView postsRecView = getActivity().findViewById(R.id.postsRec);
        WallPostAdapter wallPostAdapter = new WallPostAdapter(posts);
        postsRecView.setAdapter(wallPostAdapter);
        postsRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(wallPostReciever, new IntentFilter(POSTS_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(wallPostReciever);
    }

    BroadcastReceiver wallPostReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action: " + action);
            if (action.equalsIgnoreCase(POSTS_ACTION)) {
                String postsJson = intent.getStringExtra("posts");
                WallPosts[] posts = new Gson().fromJson(postsJson, WallPosts[].class);
                Log.d(TAG, "After Receiving" + posts[0].Content);
                showAllPosts(posts);
            } else {
                Log.d(TAG, "Maybe useful intent action, but not of our interest");
            }

        }
    };


}
