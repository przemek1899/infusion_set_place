package com.vcf.przemek.firstappsdk16;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.vcf.przemek.firstappsdk16.InfusionSetReader.InfusionSetEntry;

public class MainActivity extends AppCompatActivity {

    private static final Map<String, String> myMap;
    private static final Calendar calendar = Calendar.getInstance();
    public java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, Locale.FRANCE);

    private String selected_place = null;
    private Integer selected_month = null;
    private Integer selected_day = null;
    private Integer selected_year = null;
    private Integer selected_hour = null;
    private Integer selected_minute = null;

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


    CharSequence infusion_places[] = new CharSequence[]{"Lewa noga", "Prawa noga", "Lewa ręka",
            "Prawa ręka", "Lewy pośladek", "Prawy pośladek"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initInfusionSetListView();
        initListViewWithCustomAdapter();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void initListViewWithCustomAdapter() {
        Cursor c = getCursorForLayout();
        List<InfusionSetPlace> list = mapEntriesFromDB(c);
        InfusionSetPlace[] array = list.toArray(new InfusionSetPlace[list.size()]);
        c.close();
        CustomAdapter adapter = new CustomAdapter(this, array);
        initInfusionSetListView(adapter);
    }

    public void initInfusionSetListView(BaseAdapter adapter) {
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                View firstTextView = ((ViewGroup)arg1).getChildAt(0);
                TextView placeTextView = (TextView) firstTextView;

                View secondTextView = ((ViewGroup)arg1).getChildAt(1);
                TextView dateTextView = (TextView) secondTextView;

                String rowText = placeTextView.getText().toString() + " " + dateTextView.getText().toString();
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

    public void refreshView(View v){
        initListViewWithCustomAdapter();
    }

    public void dialogOnItemLongClick(final Integer id_row, String rowText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usunąć pozycję?");
        builder.setMessage("Na pewno chcesz usunąć wkłucie:\n"+ rowText + "?");
        builder.setPositiveButton("Tak", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteInfusionSet(id_row);
                initListViewWithCustomAdapter();
                return;
            }
        });
        builder.setNegativeButton("Nie", null);
        builder.show();
    }

    public void addInfusionSetDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Miejsce wkucia");
        builder.setItems(infusion_places, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on infusion_places[which]

                selected_place = infusion_places[which].toString();
                // tutaj powinno wywoływać nowy dialog
//                showDatePickerDialog();
                showTimePickerDialog();
                // addInfousionSetConfirmDialog();
                //insertInfusionSet(infusion_places[which].toString(), new Date(), false);
            }
        });
        builder.show();
    }

    public Date getSelectedDate(){
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, selected_day);
        calendar.set(Calendar.MONTH, selected_month);
        calendar.set(Calendar.YEAR, selected_year);
        calendar.set(Calendar.HOUR_OF_DAY, selected_hour);
        calendar.set(Calendar.MINUTE, selected_minute);
        return calendar.getTime();
    }

    public void addInfousionSetConfirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Podsumowanie");

        final Date creationDate = getSelectedDate();
        String custom_date_string = df.format(creationDate);
        builder.setMessage("Miejsce: " + selected_place + "  " + custom_date_string);
        builder.setPositiveButton("Tak", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO
                boolean not_working = false;
                insertInfusionSet(selected_place, creationDate, not_working);
                initListViewWithCustomAdapter();
            }
        });
        builder.setNegativeButton("Nie", null);
        builder.show();
    }

    public String getStringBoolean(Boolean value) {
        if (value == true) {
            return "1";
        } else {
            return "0";
        }
    }

    public void insertInfusionSet(String place, Date creation_date, boolean not_working) {

//      InfusionSetDatabase dbHelper = new InfusionSetDatabase(getContext());
        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getApplicationContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(InfusionSetEntry.COLUMN_NAME_PLACE, place);
        values.put(InfusionSetEntry.COLUMN_NAME_CREATION_DATE, creation_date.getTime());
        values.put(InfusionSetEntry.COLUMN_NAME_NOT_WORKING, getStringBoolean(not_working));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(InfusionSetEntry.TABLE_NAME, null, values);
    }

    public void deleteInfusionSet(Integer row_id) {

        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = InfusionSetEntry._ID + " = ?";
        db.delete(InfusionSetEntry.TABLE_NAME, selection, new String[]{Integer.toString(row_id)});
    }

    public Cursor getCursorForLastInfusionSet() {
        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                InfusionSetEntry._ID,
                InfusionSetEntry.COLUMN_NAME_PLACE,
                InfusionSetEntry.COLUMN_NAME_CREATION_DATE,
                InfusionSetEntry.COLUMN_NAME_NOT_WORKING
        };

        // Filter results WHERE "title" = 'My Title'
//        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = InfusionSetEntry.COLUMN_NAME_CREATION_DATE + " DESC";
        String limit = "10";

        Cursor cursor = db.query(
                InfusionSetEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null, //selection,                                // The columns for the WHERE clause
                null, //selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                 // The sort order
                limit
        );
        return cursor;
        // remember about closing the cursor
    }

    public String getNextInfusionSetPlace() {
        Cursor c = getCursorForLastInfusionSet();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String place = c.getString(c.getColumnIndex(InfusionSetEntry.COLUMN_NAME_PLACE));
                    String is_working = c.getString(c.getColumnIndex(InfusionSetEntry.COLUMN_NAME_NOT_WORKING));
//                    int age = c.getInt(c.getColumnIndex("Age"));
                } while (c.moveToNext());
            }
        }
        c.close();
        // TODO
        return "Nothing";
    }

    public List<InfusionSetPlace> mapEntriesFromDB(Cursor c) {
        List<InfusionSetPlace> list = new ArrayList<InfusionSetPlace>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String place = c.getString(c.getColumnIndex(InfusionSetEntry.COLUMN_NAME_PLACE));
//                    String not_working_str = c.getString(c.getColumnIndex(InfusionSetEntry.COLUMN_NAME_NOT_WORKING));
                    long longTime = c.getLong(c.getColumnIndex(InfusionSetEntry.COLUMN_NAME_CREATION_DATE));
                    Date date = new Date(longTime);

                    int id = c.getInt(c.getColumnIndex(InfusionSetEntry._ID));
//                    boolean not_working = not_working_str.toLowerCase().equals("true");
                    list.add(new InfusionSetPlace(id, place, date, false));
                } while (c.moveToNext());
            }
        }
        return list;
    }

    public Cursor getCursorForLayout() {

        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                InfusionSetEntry._ID,
                InfusionSetEntry.COLUMN_NAME_PLACE,
                InfusionSetEntry.COLUMN_NAME_CREATION_DATE
        };

        String sortOrder = InfusionSetEntry.COLUMN_NAME_CREATION_DATE + " DESC";

        Cursor cursor = db.query(
                InfusionSetEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null, //selection,                                // The columns for the WHERE clause
                null, //selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return cursor;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            MainActivity a = (MainActivity)getActivity();
            a.setDate(year, month, day);
            a.addInfousionSetConfirmDialog();
        }
    }

    public void setDate(final int year, final int month, final int day){
        selected_day = day;
        selected_month = month;
        selected_year = year;
    }

    public void setTime(final int hourOfDay, final int minute){
        selected_hour = hourOfDay;
        selected_minute = minute;
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            MainActivity a = (MainActivity)getActivity();
            a.setTime(hourOfDay, minute);
            a.showDatePickerDialog();
        }
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
}
