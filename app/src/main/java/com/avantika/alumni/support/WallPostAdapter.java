package com.avantika.alumni.support;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.WallPosts;
import com.avantika.alumni.server.BaseURL;
import com.squareup.picasso.Picasso;

public class WallPostAdapter extends RecyclerView.Adapter {

    WallPosts[] posts;

    public WallPostAdapter(WallPosts[] list) {
        this.posts = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View wallPostsLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_card, viewGroup, false);
        return new PostsViewHolder(wallPostsLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        WallPosts post = posts[i];
        bindPost((PostsViewHolder) viewHolder, post);
    }

    public void bindPost(PostsViewHolder postViewHolder, WallPosts post){
        postViewHolder.displayName.setText(post.Name);
        postViewHolder.timePosted.setText(post.postedTime);
        postViewHolder.postContent.setText(post.Content);
        if (post.postPhoto != null) {
            postViewHolder.contentPic.setVisibility(View.VISIBLE);
            Picasso.get().load(BaseURL.BASE_URL + post.postPhoto).into(postViewHolder.contentPic);
        } else {
            postViewHolder.contentPic.setVisibility(View.GONE);
        }
        Picasso.get().load(BaseURL.BASE_URL + post.Profile_Pic).transform(new CircleTransform()).into(postViewHolder.profilePic);
    }

    @Override
    public int getItemCount() {
        return posts.length;
    }

    private class PostsViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePic;
        public TextView displayName;
        public ImageView contentPic;
        public TextView postContent;
        public TextView timePosted;

        public PostsViewHolder(View wallPostsLayout) {
            super(wallPostsLayout);
            profilePic = wallPostsLayout.findViewById(R.id.ppic);
            displayName = wallPostsLayout.findViewById(R.id.displayName);
            contentPic = wallPostsLayout.findViewById(R.id.postImage);
            postContent = wallPostsLayout.findViewById(R.id.postContent);
            timePosted = wallPostsLayout.findViewById(R.id.timePosted);
        }
    }
}
