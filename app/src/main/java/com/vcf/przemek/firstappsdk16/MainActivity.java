package com.vcf.przemek.firstappsdk16;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.vcf.przemek.firstappsdk16.InfusionSetReader.InfusionSetEntry;

public class MainActivity extends AppCompatActivity {

    private static final Map<String, String> myMap;
    static {
        // LEFT_ARM, RIGHT_ARM, LEFT_THIGH, RIGHT_THIGH, LEFT_BUTTOCK, RIGHT_BUTTOCK
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("LEFT_THIGH", "Lewa noga");
        aMap.put("RIGHT_THIGH", "Prawa noga");

        aMap.put("LEFT_ARM", "Lewa ręka");
        aMap.put("RIGHT_ARM", "Prawa ręka");

        aMap.put("LEFT_BUTTOCK", "Lewy pośladek");
        aMap.put("RIGHT_BUTTOCK", "Prawy pośladek");
        myMap = Collections.unmodifiableMap(aMap);
    }

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;


    CharSequence infusion_places[] = new CharSequence[] {"Lewa noga", "Prawa noga", "Lewa ręka",
            "Prawa ręka", "Lewy pośladek", "Prawy pośladek"};

    String testArray[] = new String[] {"Lewa noga 10-12", "Lewa ręka 13-02", "Lewy pośladek 16-2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void initInfusionSetListView(){
        //        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, testArray);

        String[] fromColumns = {InfusionSetEntry.COLUMN_NAME_PLACE,
                InfusionSetEntry.COLUMN_NAME_CREATION_DATE};
        int[] toViews = {R.id.place_entry, R.id.date_entry};

        Cursor cursor = getCursorForLayout();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.infusion_set_entry, cursor, fromColumns, toViews, 0);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }

    public void addInfusionSetDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Miejsce wkucia");
        builder.setItems(infusion_places, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on infusion_places[which]
                insertInfusionSet(infusion_places[which].toString(), new Date(), false);
            }
        });
        builder.show();
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }
            };

    public String getStringBoolean(Boolean value){
        if (value == true){
            return "1";
        }
        else{
            return "0";
        }
    }

    public void insertInfusionSet(String place, Date creation_date, boolean not_working){

//        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getContext());
        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getApplicationContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(InfusionSetEntry.COLUMN_NAME_PLACE, place);
        values.put(InfusionSetEntry.COLUMN_NAME_CREATION_DATE, creation_date.toString());
        values.put(InfusionSetEntry.COLUMN_NAME_NOT_WORKING, getStringBoolean(not_working));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(InfusionSetEntry.TABLE_NAME, null, values);
    }

    public Cursor getCursorForLayout(){

        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                InfusionSetEntry._ID,
                InfusionSetEntry.COLUMN_NAME_PLACE,
                InfusionSetEntry.COLUMN_NAME_CREATION_DATE
        };

        // Filter results WHERE "title" = 'My Title'
//        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                InfusionSetEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null, //selection,                                // The columns for the WHERE clause
                null, //selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null //sortOrder                                 // The sort order
        );
        return cursor;
    }
}

//public class InfusionSetDateDialog extends DialogFragment {
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        return new DatePickerDialog(this, myDateListener, year, month, day);
//    }
//}
