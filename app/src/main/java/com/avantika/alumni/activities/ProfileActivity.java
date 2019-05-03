package com.avantika.alumni.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Authentication;
import com.avantika.alumni.parameters.Industry_Domains;
import com.avantika.alumni.server.BaseURL;
import com.avantika.alumni.server.ServerFunctions;
import com.avantika.alumni.support.CircleTransform;
import com.avantika.alumni.support.EducationListAdapter;
import com.avantika.alumni.support.ExperienceListAdapter;
import com.avantika.alumni.support.NonScrollListView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.avantika.alumni.parameters.Intents.ADD_QUALIFICATION_ACTION;
import static com.avantika.alumni.parameters.Intents.DOMAIN_LIST_ACTION;
import static com.avantika.alumni.parameters.SharedPrefFiles.STORAGE_FILE;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "ProfileFragment";
    Dialog dialog;
    Industry_Domains[] domainList;
    final Calendar startCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startDate, endDate;
    EditText editStartDate, editEndDate;
    Spinner domainSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sharedPref = getSharedPreferences(STORAGE_FILE, Context.MODE_PRIVATE);
        Authentication.Profile profile = new Gson().fromJson(sharedPref.getString("profile", ""), Authentication.Profile.class);
        populateProfile(profile);

        Intent intent = new Intent(this, ServerFunctions.class);
        intent.putExtra("request", "domains");
        startService(intent);
    }

    private void populateProfile(Authentication.Profile profile) {

        // Profile Image View
        ImageView imgView = findViewById(R.id.profile_ppic);
        //Log.d(TAG, "PhotoURL: " + BaseURL.BASE_URL + profile.personal_information.profile_pics);
        Picasso.get().load(BaseURL.BASE_URL + profile.personal_information.profile_pics).transform(new CircleTransform()).into(imgView);

        // Profile Name
        TextView txtName = findViewById(R.id.profile_displayName);
        txtName.setText(getResources().getString(R.string.displayName, profile.personal_information.F_Name, profile.personal_information.L_Name));

        // Profile Email Id
        TextView txtEmail = findViewById(R.id.profile_email);
        txtEmail.setText(profile.personal_information.Email_ID);

        // Education and Experience list views for dynamic data. Just so they don't scroll using NonScrollListView
        NonScrollListView experienceListView = findViewById(R.id.experienceListView);
        NonScrollListView educationListView = findViewById(R.id.educationListView);

        // Creating their adapters
        EducationListAdapter educationListAdapter = new EducationListAdapter(getApplicationContext(), R.layout.profileinfocards, profile.education);
        ExperienceListAdapter experienceListAdapter = new ExperienceListAdapter(getApplicationContext(), R.layout.profileinfocards, profile.experience);

        // Setting their adapters
        experienceListView.setAdapter(experienceListAdapter);
        educationListView.setAdapter(educationListAdapter);

        // Setting add job and education information dialog boxes. Handling POST request for them as well.
        ImageButton addEducationInfoButton = findViewById(R.id.addEducationInfo);
        ImageButton addExperienceInfoButton = findViewById(R.id.addExperienceInfo);

        addEducationInfoButton.setOnClickListener(v -> addEducationInfoButton());


    }

    private void addEducationInfoButton() {


        dialog = new Dialog(this);

        dialog.setContentView(R.layout.add_information_profile);

        TextView heading = dialog.findViewById(R.id.addInfoHeading);
        heading.setText("Add Education Information");

        EditText educationTitle = dialog.findViewById(R.id.inputInfoTitle);
        educationTitle.setHint("Qualification Name");

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageButton addBtn = dialog.findViewById(R.id.addInfoButton);
        ImageButton closeBtn = dialog.findViewById(R.id.closeBtn);

        startDatePicker();
        endDatePicker();

        addValuesToSpinner(domainList);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addQualification = new Intent(getApplicationContext(), ServerFunctions.class);
                addQualification.putExtra("request", "addQualification");
                addQualification.putExtra("title", educationTitle.getText().toString());
                addQualification.putExtra("startDate", editStartDate.getText().toString());
                addQualification.putExtra("endDate", editEndDate.getText().toString());
                addQualification.putExtra("domainName", domainSpinner.getSelectedItem().toString());
                startService(addQualification);
                dialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(domainListReciever, new IntentFilter(DOMAIN_LIST_ACTION));
        registerReceiver(addQualificationReciever, new IntentFilter(ADD_QUALIFICATION_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(domainListReciever);
        unregisterReceiver(addQualificationReciever);
    }

    BroadcastReceiver addQualificationReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("HotLinksActivity", "action:" + action);
            if (action.equalsIgnoreCase(ADD_QUALIFICATION_ACTION)) {
                boolean addResponseJson = intent.getBooleanExtra("error", false);
                if (addResponseJson) {
                    Toast.makeText(context, "Qualification Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Qualification Not Added", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(this.getClass().getSimpleName(), "Error recieving intent");

            }

        }
    };

    BroadcastReceiver domainListReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "domainListReciever Registered");
            String action = intent.getAction();
            Log.d("HotLinksActivity", "action:" + action);
            if (action.equalsIgnoreCase(DOMAIN_LIST_ACTION)) {
                String domainListJson = intent.getStringExtra("domainList");
                domainList = new Gson().fromJson(domainListJson, Industry_Domains[].class);
            } else {
                Log.d(this.getClass().getSimpleName(), "Error recieving intent");

            }
        }
    };

    private void startDatePicker() {
        editStartDate = dialog.findViewById(R.id.infoStartDate);
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartLabel();
            }

        };

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new DatePickerDialog(getApplicationContext(), startDate, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    private void endDatePicker() {
        editEndDate = dialog.findViewById(R.id.infoEndDate);
        endDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndLabel();
            }

        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(), endDate, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    private void updateStartLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(startCalendar.getTime()));
    }

    private void updateEndLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(endCalendar.getTime()));
    }

    private void addValuesToSpinner(Industry_Domains[] domainList) {
        domainSpinner = dialog.findViewById(R.id.domainListSpinner);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < domainList.length; i++) {
            list.add(domainList[i].toString());
        }
        list.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domainSpinner.setAdapter(dataAdapter);
    }

}
