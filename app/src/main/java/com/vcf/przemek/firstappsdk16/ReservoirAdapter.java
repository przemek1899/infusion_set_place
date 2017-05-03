package com.vcf.przemek.firstappsdk16;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by Przemek on 2017-05-02.
 */

public class ReservoirAdapter extends ArrayAdapter<ReservoirChange> {

    private final Context context;
    private final ReservoirChange[] values;

    public ReservoirAdapter(Context context, ReservoirChange[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.reservoir_change_entry, parent, false);

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

        rowView.setTag(values[position].getID());
        return rowView;
    }
}
