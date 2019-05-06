package com.avantika.alumni.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avantika.alumni.R;
import com.avantika.alumni.fragments.DirectoryFragment;
import com.avantika.alumni.fragments.HomeFragment;
import com.avantika.alumni.fragments.InformationFragment;
import com.avantika.alumni.fragments.OffersFragment;
import com.avantika.alumni.fragments.ProjectsFragment;

public class HotLinksActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    TextView labelActionBar;

    private static final String TAG = "HotLinksActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar topToolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(topToolbar);

        labelActionBar = findViewById(R.id.toolbarTitle);

        ImageButton profileBtn = findViewById(R.id.profileIcon);
        profileBtn.setOnClickListener(v -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
        });
        //loading the default fragment
        labelActionBar.setText("alumni");
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(this);


    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                labelActionBar.setText("alumni");
                fragment = new HomeFragment();
                break;

            case R.id.navigation_directory:
                labelActionBar.setText("Directory");

                fragment = new DirectoryFragment();
                break;

            case R.id.navigation_information:
                labelActionBar.setText("Information");
                fragment = new InformationFragment();
                break;

            case R.id.navigation_projects:
                labelActionBar.setText("Projects");
                fragment = new ProjectsFragment();
                break;

            case R.id.navigation_offers:
                labelActionBar.setText("Industry Offers");
                fragment = new OffersFragment();
                break;

        }

        return loadFragment(fragment);
    }
}
