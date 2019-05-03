package com.avantika.alumni.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avantika.alumni.R;
import com.avantika.alumni.parameters.Authentication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExperienceListAdapter extends ArrayAdapter<Authentication.Profile.Experience> {

    public static final String TAG = ExperienceListAdapter.class.getSimpleName();

    private Context mContext;
    private int mResource;

    public ExperienceListAdapter(Context context, int resource, Authentication.Profile.Experience[] objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        String experienceTitle = getItem(position).Job_Title;
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
        String startMonth = null;
        String startYear = null;
        String endMonth = null;
        String endYear = null;
        try {
            Date startd = dateParser.parse(getItem(position).Start_Date);
            Date endd = dateParser.parse(getItem(position).End_Date);
            DateFormat MonthFormat = new SimpleDateFormat("MMMM");
            DateFormat YearFormat = new SimpleDateFormat("yyyy");
            startMonth = MonthFormat.format(startd);
            startYear = YearFormat.format(startd);
            endMonth = MonthFormat.format(endd);
            endYear = YearFormat.format(endd);
            //  Log.d(TAG, "Month: " + startMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String duration = "From " + startMonth + ", " + startYear + " to " + endMonth + ", " + endYear;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewExperienceTitle = convertView.findViewById(R.id.profile_card_heading);
        TextView textViewExperienceDuration = convertView.findViewById(R.id.profile_card_duration);

        ImageButton updateButton = convertView.findViewById(R.id.editButton);

        updateButton.setTag(getItem(position).Job_Title);


        textViewExperienceTitle.setText(experienceTitle);

        textViewExperienceDuration.setText(duration);
        return convertView;
    }
}
