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
import com.avantika.alumni.parameters.Univ_Projects;
import com.avantika.alumni.server.ServerFunctions;
import com.avantika.alumni.support.ProjectsAdapter;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.UNIV_PROJ_ACTION;

public class UniversityProjectsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_university_projects, null);

        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFunctions.class);
        intent.putExtra("request", "univ_projects");
        getActivity().startService(intent);

        return rootView;
    }

    private void showProjects(Univ_Projects[] projects) {
        RecyclerView univProjectRecView = getActivity().findViewById(R.id.universityProjectsRec);
        ProjectsAdapter projectsAdapter = new ProjectsAdapter(projects);
        univProjectRecView.setAdapter(projectsAdapter);
        univProjectRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(projectReciever, new IntentFilter(UNIV_PROJ_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(projectReciever);
    }

    BroadcastReceiver projectReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("UnivProj", "action: " + action);
            if (action.equalsIgnoreCase(UNIV_PROJ_ACTION)) {
                String univProjectJson = intent.getStringExtra("projectData");
                Univ_Projects[] univProjects = new Gson().fromJson(univProjectJson, Univ_Projects[].class);
                //Log.d(TAG, "ID1: " + events[0].Event_ID + " ID2: " + events[1].Event_ID);
                showProjects(univProjects);
            } else {
                Log.d("UnivProj", "Maybe useful intent action, but not of our interest");
            }

        }
    };
}
