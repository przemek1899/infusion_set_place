package com.vcf.przemek.firstappsdk16;

import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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
        placeView.setText(values[position].getPlace() + " " + Integer.toString(values[position].getID()));

        TextView dateView = (TextView) rowView.findViewById(R.id.date_entry);
        dateView.setText(values[position].getDate_str());

        rowView.setTag(values[position].getID());
        return rowView;
    }

}
