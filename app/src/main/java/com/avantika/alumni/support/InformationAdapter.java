package com.avantika.alumni.support;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Events;
import com.avantika.alumni.parameters.News;
import com.avantika.alumni.server.BaseURL;
import com.squareup.picasso.Picasso;

public class InformationAdapter extends RecyclerView.Adapter {
    public static final String TAG = InformationAdapter.class.getSimpleName();

    Object[] list;
    public static final int ITEM_TYPE_NEWS = 0;
    public static final int ITEM_TYPE_EVENTS = 1;

    public InformationAdapter(Object[] list1) {
        this.list = list1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_TYPE_NEWS) {
            View newsLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.infocard,
                    viewGroup, false);
            return new NewsViewHolder(newsLayout);
        } else if (viewType == ITEM_TYPE_EVENTS) {
            View eventsLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.infocard,
                    viewGroup, false);
            return new EventsViewHolder(eventsLayout);
        } else {
            Log.w(TAG, "Invalid type of view received");
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof NewsViewHolder) {
            Object possiblyNews = list[i];
            bindNews((NewsViewHolder) viewHolder, (News) possiblyNews);
        } else if (viewHolder instanceof EventsViewHolder) {
            Object possiblyEvents = list[i];
            bindEvents((EventsViewHolder) viewHolder, (Events) possiblyEvents);
        } else {
            Log.e(TAG, "Unable to bind this type");
        }
    }


    public void bindNews(NewsViewHolder viewHolder, News news) {
        String title = news.Title;
        if (news.Title.length() > 40) {
            title = news.Title.substring(0, 39) + "...";
        }
        viewHolder.heading.setText(title);
        viewHolder.author.setText(news.Author);
        Picasso.get().load(BaseURL.BASE_URL + news.Thumbnail).resize(375, 100).centerCrop().into(viewHolder.cardImage);
        viewHolder.cardTimestamp.setText(news.Date_Time);
        viewHolder.card.setTag(news.News_ID);
        viewHolder.card.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
        });
    }

    public void bindEvents(EventsViewHolder viewHolder, Events events) {
        String title = events.Title;
        if (events.Title.length() > 40) {
            title = events.Title.substring(0, 39) + "...";
        }
        viewHolder.heading.setText(title);
        viewHolder.author.setText(events.Author);
        Picasso.get().load(BaseURL.BASE_URL + events.Thumbnail).resize(375, 100).centerCrop().into(viewHolder.cardImage);
        viewHolder.cardTimestamp.setText(events.Date_Time);
        viewHolder.card.setTag(events.Event_ID);
        viewHolder.card.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = list[position];
        if (object instanceof News) {
            return ITEM_TYPE_NEWS;
        } else if (object instanceof Events) {
            return ITEM_TYPE_EVENTS;
        } else {
            Log.e(TAG, "Unknowntype of view");
            return -1;
        }
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;
        public ImageView cardImage;
        public TextView author;
        public TextView cardTimestamp;
        public CardView card;

        public NewsViewHolder(View newsLayout) {
            super(newsLayout);
            heading = newsLayout.findViewById(R.id.heading);
            cardImage = newsLayout.findViewById(R.id.cardImage);
            author = newsLayout.findViewById(R.id.author);
            cardTimestamp = newsLayout.findViewById(R.id.cardTimestamp);
            card = newsLayout.findViewById(R.id.infoCard);
        }
    }

    private class EventsViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;
        public ImageView cardImage;
        public TextView author;
        public TextView cardTimestamp;
        public CardView card;

        public EventsViewHolder(View eventsLayout) {
            super(eventsLayout);
            heading = eventsLayout.findViewById(R.id.heading);
            cardImage = eventsLayout.findViewById(R.id.cardImage);
            author = eventsLayout.findViewById(R.id.author);
            cardTimestamp = eventsLayout.findViewById(R.id.cardTimestamp);
            card = eventsLayout.findViewById(R.id.infoCard);
        }
    }
}
