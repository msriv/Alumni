package com.avantika.alumni.fragments;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avantika.alumni.R;
import com.avantika.alumni.database.AlumniDatabase;
import com.avantika.alumni.parameters.Directory;
import com.avantika.alumni.server.ServerFunctions;
import com.avantika.alumni.support.DirectoryAdapter;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.DIRECTORY_ACTION;

public class DirectoryFragment extends Fragment {
    private static final String TAG = "DirectoryFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_directory, null);
        AlumniDatabase db = Room.databaseBuilder(getContext(),
                AlumniDatabase.class, "alumni.db").fallbackToDestructiveMigration().build();
        getUsers();


        return rootView;
    }

    private void getUsers() {
        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFunctions.class);
        intent.putExtra("request", "directory");
        getActivity().startService(intent);
    }

    private void showAllUsers(Directory[] directory) {
        RecyclerView directoryRecView = getActivity().findViewById(R.id.directoryRec);
        DirectoryAdapter directoryAdapter = new DirectoryAdapter(directory);
        directoryRecView.setAdapter(directoryAdapter);
        directoryRecView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(directoryReciever, new IntentFilter(DIRECTORY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(directoryReciever);
    }

    BroadcastReceiver directoryReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action: " + action);
            if (action.equalsIgnoreCase(DIRECTORY_ACTION)) {
                String directoryJson = intent.getStringExtra("directory");
                Directory[] directory = new Gson().fromJson(directoryJson, Directory[].class);
                Log.d(TAG, "After Receiving" + directory[0].Email_ID);
                showAllUsers(directory);
            } else {
                Log.d(TAG, "Maybe useful intent action, but not of our interest");
            }

        }
    };
}
