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
import com.avantika.alumni.parameters.Assoc_Projects;
import com.avantika.alumni.server.ServerFunctions;
import com.avantika.alumni.support.ProjectsAdapter;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.ASSOC_PROJ_ACTION;

public class AssociationProjectsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_association_projects, null);

        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFunctions.class);
        intent.putExtra("request", "assoc_projects");
        getActivity().startService(intent);

        return rootView;
    }

    private void showProjects(Assoc_Projects[] projects) {
        RecyclerView assocProjectRecView = getActivity().findViewById(R.id.assocProjectsRec);
        ProjectsAdapter projectsAdapter = new ProjectsAdapter(projects);
        assocProjectRecView.setAdapter(projectsAdapter);
        assocProjectRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(projectReciever, new IntentFilter(ASSOC_PROJ_ACTION));
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
            Log.d("AssocProj", "action: " + action);
            if (action.equalsIgnoreCase(ASSOC_PROJ_ACTION)) {
                String assocProjectJson = intent.getStringExtra("projectData");
                Assoc_Projects[] assocProjects = new Gson().fromJson(assocProjectJson, Assoc_Projects[].class);
                //Log.d(TAG, "ID1: " + events[0].Event_ID + " ID2: " + events[1].Event_ID);
                showProjects(assocProjects);
            } else {
                Log.d("AssocProj", "Maybe useful intent action, but not of our interest");
            }

        }
    };
}
