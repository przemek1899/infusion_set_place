package com.vcf.przemek.firstappsdk16;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;


    CharSequence infusion_places[] = new CharSequence[] {"Lewa noga", "Prawa noga", "Lewa ręka",
            "Prawa ręka", "Lewy pośladek", "Prawy pośladek"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void addInfusionSetDialog(View view){
//        Intent intent = new Intent(this, AddInfusionSet.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Miejsce wkucia");
        builder.setItems(infusion_places, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
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

    public void insertInfusionSet(String place, Date creation_date, boolean not_working){

        InfusionSetDatabase dbHelper = new InfusionSetDatabase(getContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
    }
}

public class InfusionSetDateDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new DatePickerDialog(this, myDateListener, year, month, day);

        //return builder.create();
    }
}
