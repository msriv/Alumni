package com.avantika.alumni.support;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Assoc_Projects;
import com.avantika.alumni.parameters.Univ_Projects;

public class ProjectsAdapter extends RecyclerView.Adapter {
    public static final String TAG = ProjectsAdapter.class.getSimpleName();

    Object[] list;
    public static final int ITEM_TYPE_ASSOCIATION_PROJECT = 0;
    public static final int ITEM_TYPE_UNIVERSITY_PROJECT = 1;

    public ProjectsAdapter(Object[] list1) {
        this.list = list1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_TYPE_ASSOCIATION_PROJECT) {
            View assocProjectLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_card,
                    viewGroup, false);
            return new AssociationProjectViewHolder(assocProjectLayout);
        } else if (viewType == ITEM_TYPE_UNIVERSITY_PROJECT) {
            View univProjectLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_card,
                    viewGroup, false);
            return new UniversityProjectViewHolder(univProjectLayout);
        } else {
            Log.w(TAG, "Invalid type of view received");
            return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof AssociationProjectViewHolder) {
            Object possiblyAssocProject = list[i];
            bindAssocProjects((AssociationProjectViewHolder) viewHolder, (Assoc_Projects) possiblyAssocProject);
        } else if (viewHolder instanceof UniversityProjectViewHolder) {
            Object possiblyUnivProject = list[i];
            bindUnivProjects((UniversityProjectViewHolder) viewHolder, (Univ_Projects) possiblyUnivProject);
        } else {
            Log.e(TAG, "Unable to bind this type");
        }
    }

    public void bindUnivProjects(UniversityProjectViewHolder viewHolder, Univ_Projects projects) {
        String title = projects.Title;
        if (projects.Title.length() > 40) {
            title = projects.Title.substring(0, 39) + "...";
        }
        viewHolder.heading.setText(title);
        viewHolder.description.setText(projects.Description);
        viewHolder.fundsRequired.setText("Funds Required: " + projects.Funds);
        viewHolder.card.setTag(projects.U_Proj_ID);
        viewHolder.card.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
        });
    }

    public void bindAssocProjects(AssociationProjectViewHolder viewHolder, Assoc_Projects projects) {
        String title = projects.Title;
        if (projects.Title.length() > 40) {
            title = projects.Title.substring(0, 39) + "...";
        }
        viewHolder.heading.setText(title);
        viewHolder.description.setText(projects.Description);
        viewHolder.fundsRequired.setText("Funds Required: " + projects.Funds);
        viewHolder.card.setTag(projects.A_Proj_ID);
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
        if (object instanceof Univ_Projects) {
            return ITEM_TYPE_UNIVERSITY_PROJECT;
        } else if (object instanceof Assoc_Projects) {
            return ITEM_TYPE_ASSOCIATION_PROJECT;
        } else {
            Log.e(TAG, "Unknowntype of view");
            return -1;
        }
    }

    private class UniversityProjectViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;
        public TextView description;
        public TextView fundsRequired;
        public Button btnPay;
        public CardView card;

        public UniversityProjectViewHolder(View univProjectLayout) {
            super(univProjectLayout);
            heading = univProjectLayout.findViewById(R.id.heading);
            description = univProjectLayout.findViewById(R.id.description);
            fundsRequired = univProjectLayout.findViewById(R.id.funds_required);
            btnPay = univProjectLayout.findViewById(R.id.btn_pay);
            card = univProjectLayout.findViewById(R.id.projectCard);
        }
    }

    private class AssociationProjectViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;
        public TextView description;
        public TextView fundsRequired;
        public Button btnPay;
        public CardView card;


        public AssociationProjectViewHolder(View assocProjectLayout) {
            super(assocProjectLayout);
            heading = assocProjectLayout.findViewById(R.id.heading);
            description = assocProjectLayout.findViewById(R.id.description);
            fundsRequired = assocProjectLayout.findViewById(R.id.funds_required);
            btnPay = assocProjectLayout.findViewById(R.id.btn_pay);
            card = assocProjectLayout.findViewById(R.id.projectCard);
        }
    }
}
