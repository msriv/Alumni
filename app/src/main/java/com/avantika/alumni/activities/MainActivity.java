package com.avantika.alumni.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Authentication;
import com.avantika.alumni.server.ServerFetch;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import static com.avantika.alumni.parameters.Intents.AUTHENTICATION_ACTION;
import static com.avantika.alumni.parameters.SharedPrefFiles.STORAGE_FILE;


public class MainActivity extends AppCompatActivity {
    public static final int GOOGLE_SIGN = 123;
    FirebaseAuth mAuth;
    Button btn_Login;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;
    public static final String TAG = MainActivity.class.getSimpleName();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(STORAGE_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        startGoogleSignIn();

    }

    private void startGoogleSignIn() {
        btn_Login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress_circular);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        btn_Login.setOnClickListener(v -> SignInGoogle());

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, HotLinksActivity.class);
            this.finish();
            startActivity(intent);
        }
    }

    private void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Task is Successful");
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG", "signin success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        serverAuthentication(user);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.w("TAG", "signin failure", task.getException());
                        Toast.makeText(this, "SignIn Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void serverAuthentication(FirebaseUser user) {
        Intent intent = new Intent(this, ServerFetch.class);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("request", "auth");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(authReciever, new IntentFilter(AUTHENTICATION_ACTION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(authReciever);
    }

    BroadcastReceiver authReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action:" + action);
            if (action.equalsIgnoreCase(AUTHENTICATION_ACTION)) {
                String authJson = intent.getStringExtra("profile");
                Authentication profile = new Gson().fromJson(authJson, Authentication.class);

                // Storing profile data in file
                editor.putString("email", profile.profile.personal_information.Email_ID);
                editor.putString("profile", new Gson().toJson(profile.profile));
                editor.commit();
                //Toast.makeText(context, "Saved in File "+profile.profile.personal_information.Email_ID , Toast.LENGTH_SHORT).show();
                if (profile.error) {
                    Logout();
                } else {
                    updateUI(profile);
                }
            }
        }
    };

    private void updateUI(Authentication profile) {
        Log.d(TAG, "Updating UI..." + profile);
        if (!profile.error) {
            //Toast.makeText(this, "Success" + profile.profile.personal_information.Email_ID, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HotLinksActivity.class);
            editor.putString("email", profile.profile.personal_information.Email_ID);
            editor.putString("profile", new Gson().toJson(profile.profile));
            editor.commit();
            this.finish();
            startActivity(intent);
        } else {
            Log.d(TAG, "Failure");
            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
            //authReciever.abortBroadcast();
        }
    }

    public void Logout() {
        Authentication profile = new Authentication();
        profile.error = true;
        profile.profile = null;
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            updateUI(profile);
        });
    }
}

