package com.avantika.alumni.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Authentication;
import com.avantika.alumni.server.BaseURL;
import com.avantika.alumni.support.CircleTransform;
import com.avantika.alumni.support.EducationListAdapter;
import com.avantika.alumni.support.ExperienceListAdapter;
import com.avantika.alumni.support.NonScrollListView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import static com.avantika.alumni.parameters.SharedPrefFiles.STORAGE_FILE;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, null);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(STORAGE_FILE, Context.MODE_PRIVATE);
        Authentication.Profile profile = new Gson().fromJson(sharedPref.getString("profile", ""), Authentication.Profile.class);
        populateProfile(profile, rootView);
        return rootView;
    }


    private void populateProfile(Authentication.Profile profile, View rootView) {

        ImageView imgView = rootView.findViewById(R.id.profile_ppic);
        Log.d(TAG, "PhotoURL: " + BaseURL.BASE_URL + profile.personal_information.profile_pics);
        Picasso.get().load(BaseURL.BASE_URL + profile.personal_information.profile_pics).transform(new CircleTransform()).into(imgView);

        TextView txtName = rootView.findViewById(R.id.profile_displayName);
        txtName.setText(getResources().getString(R.string.displayName, profile.personal_information.F_Name, profile.personal_information.L_Name));

        TextView txtEmail = rootView.findViewById(R.id.profile_email);
        txtEmail.setText(profile.personal_information.Email_ID);

        NonScrollListView experienceListView = rootView.findViewById(R.id.experienceListView);
        NonScrollListView educationListView = rootView.findViewById(R.id.educationListView);

        EducationListAdapter educationListAdapter = new EducationListAdapter(getActivity().getApplicationContext(), R.layout.profileinfocards, profile.education);
        ExperienceListAdapter experienceListAdapter = new ExperienceListAdapter(getActivity().getApplicationContext(), R.layout.profileinfocards, profile.experience);

        experienceListView.setAdapter(experienceListAdapter);
        educationListView.setAdapter(educationListAdapter);
    }

}
