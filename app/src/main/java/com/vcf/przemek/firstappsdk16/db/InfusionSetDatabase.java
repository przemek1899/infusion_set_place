package com.vcf.przemek.firstappsdk16.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Przemek on 2017-02-07.
 */

public class InfusionSetDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "INFUSION_SET_MEMORY";
    private static final int DATABASE_VERSION = 3;

    public static final String INFUSION_SET_TABLE_NAME = "infusion_set";
    private static final String INFUSION_SET_TABLE_CREATE =
            "CREATE TABLE " + INFUSION_SET_TABLE_NAME + " (" +
                    InfusionSetReader.InfusionSetEntry._ID + " INTEGER PRIMARY KEY," +
                    InfusionSetReader.InfusionSetEntry.COLUMN_NAME_PLACE + " TEXT NOT NULL," +
                    InfusionSetReader.InfusionSetEntry.COLUMN_NAME_CREATION_DATE + " INTEGER NOT NULL," +
                    InfusionSetReader.InfusionSetEntry.COLUMN_NAME_NOT_WORKING + " BOOLEAN);";

    public static final String INSULIN_CONTAINER_TABLE_NAME = "insulin_container";
    private static final String INSULIN_CONTAINER_TABLE_CREATE =
            "CREATE TABLE " + INSULIN_CONTAINER_TABLE_NAME + " (" +
                    InfusionSetReader.InfusionSetEntry._ID + " INTEGER PRIMARY KEY," +
                    InfusionSetReader.InfusionSetEntry.COLUMN_NAME_CREATION_DATE + " INTEGER NOT NULL);";

    public InfusionSetDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(INFUSION_SET_TABLE_CREATE);
        db.execSQL(INSULIN_CONTAINER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        if (DATABASE_VERSION == 2){
            db.execSQL(INSULIN_CONTAINER_TABLE_CREATE);
        }
    }
}
