package com.avantika.alumni.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.WallPosts;
import com.avantika.alumni.server.ServerFunctions;
import com.avantika.alumni.support.WallPostAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.avantika.alumni.parameters.Intents.POSTS_ACTION;
import static com.avantika.alumni.parameters.SharedPrefFiles.STORAGE_FILE;
import static com.avantika.alumni.support.OffersAdapter.TAG;

public class HomeFragment extends Fragment {

    ImageButton choosePhoto;
    ImageButton savePost;
    TextView content;
    ImageView uploadedImg;
    String imagePath;
    SharedPreferences sharedPref;
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

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            uploadedImg = dialog.findViewById(R.id.uploadedImage);
            choosePhoto = dialog.findViewById(R.id.uploadBtn);
            content = dialog.findViewById(R.id.editPostContent);
            sharedPref = getActivity().getSharedPreferences(STORAGE_FILE, Context.MODE_PRIVATE);

            choosePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                }
            });

            ImageButton sendBtn = dialog.findViewById(R.id.sendBtn);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(imagePath);
                    String postContent = content.getText().toString();
                    Log.d(TAG, "==> POST DEBUGGING");
                    Log.d(TAG, "Post Content: " + postContent);
                    Log.d(TAG, "Email: " + sharedPref.getString("email", ""));
                    Intent intent = new Intent(dialog.getContext(), ServerFunctions.class);
                    intent.putExtra("request", "savePost");
                    intent.putExtra("imageFile", file);
                    intent.putExtra("content", postContent);
                    intent.putExtra("email", sharedPref.getString("email", ""));
                    getActivity().startService(intent);
                    dialog.dismiss();
                }
            });

            dialog.show();
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(getContext(), "Unable to Choose image!", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri imageUri = data.getData();
            imagePath = getRealPathFromUri(imageUri);
            uploadedImg.setVisibility(View.VISIBLE);
            Picasso.get().load(imagePath).resize(50, 50).into(uploadedImg);
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }

    private void savePostsToServer(String content) {

    }

    private void getPostsFromServer() {
        Intent intent = new Intent(getActivity().getApplicationContext(), ServerFunctions.class);
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
