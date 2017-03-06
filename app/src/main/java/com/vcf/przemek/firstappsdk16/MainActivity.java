package com.vcf.przemek.firstappsdk16;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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
import java.util.Map;

import static com.vcf.przemek.firstappsdk16.InfusionSetReader.InfusionSetEntry;

public class MainActivity extends AppCompatActivity {

    private static final Map<String, String> myMap;
    private static final Calendar calendar = Calendar.getInstance();
    private DatePicker datePicker;

    private String selected_place = null;
    private Integer selected_month = null;
    private Integer selected_day = null;
    private Integer selected_year = null;

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
                Object o = arg1.getTag();
                Integer id_row = null;
                if (o != null) {
                    id_row = (Integer) o;
                }
                dialogOnItemLongClick(id_row);
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

    public void dialogOnItemLongClick(final Integer id_row) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usunąć pozycję?");
        builder.setMessage("Na pewno chcesz usunąć wkłucie?" + " " + id_row.toString());
        builder.setPositiveButton("Tak", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteInfusionSet(id_row);
                // TODO
                // refresh view
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
                //showDatePickerDialog();
                showDialog(999);
                addInfousionSetConfirmDialog();
                //insertInfusionSet(infusion_places[which].toString(), new Date(), false);
            }
        });
        builder.show();
    }

    public void addInfousionSetConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Podsumowanie");

        builder.setMessage("Miejsce: " + selected_place + "\nData: " + selected_day.toString() +
                "-" + selected_month.toString());
        builder.setPositiveButton("Tak", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertInfusionSet();
                //insertInfusionSet(infusion_places[which].toString(), new Date(), false);
            }
        });
        builder.setNegativeButton("Nie", null);
        builder.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, selected_year, selected_month, selected_day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    selected_day = dayOfMonth;
                    selected_month = month;
                    selected_year = year;
                    addInfousionSetConfirmDialog();
                }
            };

    public String getStringBoolean(Boolean value) {
        if (value == true) {
            return "1";
        } else {
            return "0";
        }
    }

    public void insertInfusionSet() {

        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, selected_day);
        calendar.set(Calendar.MONTH, selected_month);
        calendar.set(Calendar.YEAR, selected_year);
        Date creationDate = calendar.getTime();

        // TODO not_working
        boolean not_working = false;
        insertInfusionSet(selected_place, creationDate, not_working);
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

}

//public class InfusionSetDateDialog extends DialogFragment {
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        return new DatePickerDialog(this, myDateListener, year, month, day);
//    }
//}
