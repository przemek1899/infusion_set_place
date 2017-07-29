package com.vcf.przemek.firstappsdk16;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vcf.przemek.firstappsdk16.db.InfusionSetDatabase;
import com.vcf.przemek.firstappsdk16.db.InsulinContainerReader;
import com.vcf.przemek.firstappsdk16.objects.ReservoirChange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsulinReservoirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulin_reservoir);

        initListViewWithCustomAdapter();
    }

    public void initListViewWithCustomAdapter() {
        Cursor c = getCursorForLayout();
        List<ReservoirChange> list = mapEntriesFromDB(c);
        ReservoirChange[] array = list.toArray(new ReservoirChange[list.size()]);
        c.close();
        ReservoirAdapter adapter = new ReservoirAdapter(this, array);
        initReservoirSetListView(adapter);
    }

    public void initReservoirSetListView(BaseAdapter adapter) {
        ListView listView = (ListView) findViewById(R.id.insulin_reservoir_list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                View firstTextView = ((ViewGroup)arg1).getChildAt(0);
                TextView dateTextView = (TextView) firstTextView;


                String rowText = dateTextView.getText().toString();
                Object o = arg1.getTag();
                Integer id_row = null;
                if (o != null) {
                    id_row = (Integer) o;
                }
                dialogOnItemLongClick(id_row, rowText);
                /*
                 return true from the onItemLongClick() - it means that the View that currently
                 received the event is the true event receiver and the event should not be
                 propagated to the other Views in the tree; when you return false -
                 you let the event be passed to the other Views that may consume it
                 */
                return true;
            }
        });
    }

    public Cursor getCursorForLayout() {

        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = new String[] {InsulinContainerReader.InsulinContainerEntry._ID,
                InsulinContainerReader.InsulinContainerEntry.COLUMN_NAME_CREATION_DATE};

        String sortOrder = InsulinContainerReader.InsulinContainerEntry.COLUMN_NAME_CREATION_DATE + " DESC";

        Cursor cursor = db.query(
                InsulinContainerReader.InsulinContainerEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null, //selection,                                // The columns for the WHERE clause
                null, //selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return cursor;
    }

    public List<ReservoirChange> mapEntriesFromDB(Cursor c) {
        List<ReservoirChange> list = new ArrayList<ReservoirChange>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    long longTime = c.getLong(c.getColumnIndex(InsulinContainerReader.InsulinContainerEntry.COLUMN_NAME_CREATION_DATE));
                    Date date = new Date(longTime);

                    int id = c.getInt(c.getColumnIndex(InsulinContainerReader.InsulinContainerEntry._ID));

                    list.add(new ReservoirChange(id, date));
                } while (c.moveToNext());
            }
        }
        return list;
    }

    public void dialogOnItemLongClick(final Integer id_row, final String rowText){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.long_item_dialog_title) //R.string.pick_color
//                .setItems(R.array.long_item_dialog_options, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0){
//                            dialogOnDeleteInfusionSet(id_row, rowText);
//                        }
//                        else if (which == 1){
//                            markInfusionSetAsFailed(id_row);
//                        }
//                    }
//                });
        builder.show();
    }
}
