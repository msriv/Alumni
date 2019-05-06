package com.avantika.alumni.support;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Directory;
import com.avantika.alumni.server.BaseURL;
import com.squareup.picasso.Picasso;

public class DirectoryAdapter extends RecyclerView.Adapter {

    Directory[] directory;

    public static final String TAG = "DirectoryAdapter";

    public DirectoryAdapter(Directory[] directory) {
        this.directory = directory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View directoryLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.usercard, viewGroup, false);
        return new DirectoryViewHolder(directoryLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Directory user = directory[i];
        bindUser((DirectoryViewHolder) viewHolder, user);
    }

    private void bindUser(DirectoryViewHolder viewHolder, Directory directory) {
        viewHolder.userCard.setTag(directory.Email_ID);
        viewHolder.displayName.setText(directory.Name);
        viewHolder.batch.setText("Batch: " + directory.Batch);
        viewHolder.course.setText("Course: " + directory.Program);
        Picasso
                .get()
                .load(BaseURL.BASE_URL + directory.profile_pics)
                .transform(new CircleTransform())
                .centerCrop()
                .resize(130, 130)
                .into(viewHolder.profilePhoto);
    }

    @Override
    public int getItemCount() {
        return directory.length;
    }

    private class DirectoryViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePhoto;
        TextView displayName;
        TextView batch;
        TextView course;
        CardView userCard;

        public DirectoryViewHolder(View directoryLayout) {
            super(directoryLayout);
            profilePhoto = directoryLayout.findViewById(R.id.userImage);
            displayName = directoryLayout.findViewById(R.id.userDisplayName);
            batch = directoryLayout.findViewById(R.id.userBatch);
            course = directoryLayout.findViewById(R.id.userCourse);
            userCard = directoryLayout.findViewById(R.id.userCard);
        }
    }
}
