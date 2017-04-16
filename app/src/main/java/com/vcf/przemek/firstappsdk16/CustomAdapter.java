package com.vcf.przemek.firstappsdk16;

import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.TimeZone;

/**
 * Created by Przemek on 2017-02-19.
 */

public class CustomAdapter extends ArrayAdapter<InfusionSetPlace> {

    private final Context context;
//    private final String[] values;
    private final InfusionSetPlace[] values;

    public CustomAdapter(Context context, InfusionSetPlace[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.infusion_set_entry, parent, false);

        TextView placeView = (TextView) rowView.findViewById(R.id.place_entry);
        placeView.setText(values[position].getPlace()); // + " " + Integer.toString(values[position].getID()));

        long when = values[position].getDate().getTime();
        int flags = 0;

        flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
//                    flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

        String finalDateTime = android.text.format.DateUtils.formatDateTime(context, when, flags);
//                when + TimeZone.getTimeZone("Europe/Warsaw").getOffset(when), flags);
//        TimeZone.getTimeZone("Europe/Warsaw")
//        TimeZone.getDefault().getOffset(when)

        TextView dateView = (TextView) rowView.findViewById(R.id.date_entry);
        dateView.setText(finalDateTime);

        if (values[position].is_not_working()) {
            ImageView imgView = (ImageView)rowView.findViewById(R.id.not_working_icon);
            imgView.setVisibility(View.VISIBLE);
        }

        rowView.setTag(values[position].getID());
        return rowView;
    }

}
