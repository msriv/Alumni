package com.avantika.alumni.support;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.IndustryOffers;

public class OffersAdapter extends RecyclerView.Adapter {
    public static final String TAG = OffersAdapter.class.getSimpleName();

    IndustryOffers[] offers;

    public OffersAdapter(IndustryOffers[] list){
        this.offers = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View offersLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offerscard,
                viewGroup,false);
        return new OfferViewHolder(offersLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        IndustryOffers industryOffers = offers[i];
        bindData((OfferViewHolder) viewHolder, industryOffers);
    }

    public void bindData(OfferViewHolder viewHolder, IndustryOffers offers) {
        viewHolder.heading.setText(offers.Offer_Title);
        viewHolder.description.setText(offers.Description);
        viewHolder.btn_apply.setTag(offers.Offer_ID);
        viewHolder.btn_apply.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return offers.length;
    }

    private class OfferViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;
        public TextView description;
        public Button btn_apply;

        public OfferViewHolder(View offersLayout) {
            super(offersLayout);
            heading = offersLayout.findViewById(R.id.heading);
            description = offersLayout.findViewById(R.id.description);
            btn_apply = offersLayout.findViewById(R.id.btn_apply);
        }
    }
}
