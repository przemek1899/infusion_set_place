package com.vcf.przemek.firstappsdk16.provider;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.vcf.przemek.firstappsdk16.db.InfusionSetDatabase;
import com.vcf.przemek.firstappsdk16.db.InfusionSetReader;

/**
 * Created by Przemek on 2017-07-23.
 */

public class DatabaseProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static String MIME_PREFIX = "vnd.infusion_set.cursor.";
    private static final String PROVIDER_NAME = "com.vcf.przemek.firstappsdk16.provider.databaseprovider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME);
//    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/images");

    InfusionSetDatabase dbHelper;

    public static final String[] INFUSION_SET_SUMMARY_PROJECTION = new String[] {
            InfusionSetReader.InfusionSetEntry._ID,
            InfusionSetReader.InfusionSetEntry.COLUMN_NAME_PLACE,
            InfusionSetReader.InfusionSetEntry.COLUMN_NAME_CREATION_DATE,
            InfusionSetReader.InfusionSetEntry.COLUMN_NAME_NOT_WORKING
    };

    static {
        sUriMatcher.addURI(PROVIDER_NAME, InfusionSetDatabase.INFUSION_SET_TABLE_NAME, 1);
        sUriMatcher.addURI(PROVIDER_NAME, InfusionSetDatabase.INFUSION_SET_TABLE_NAME + "/#", 2);

        sUriMatcher.addURI(PROVIDER_NAME, InfusionSetDatabase.INSULIN_CONTAINER_TABLE_NAME, 3);
        sUriMatcher.addURI(PROVIDER_NAME, InfusionSetDatabase.INSULIN_CONTAINER_TABLE_NAME + "/#", 4);
    }

    public boolean onCreate (){
        dbHelper = new InfusionSetDatabase(getContext());
        return true;
    }

    public Cursor query (Uri uri,
                         String[] projection,
                         String selection,
                         String[] selectionArgs,
                         String sortOrder){


        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                InfusionSetReader.InfusionSetEntry.TABLE_NAME,
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return cursor;
    }

    public String getType (Uri uri){
        switch (sUriMatcher.match(uri)){
            // TODO
            case 1:
                return "vnd.android.cursor.dir/vnd.example.students";
            case 2:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    public Uri insert (Uri uri, ContentValues values){

        // TODO
        return uri;
    }

    public int delete (Uri uri, String selection, String[] selectionArgs){

        return 0;
    }

    public int update (Uri uri, ContentValues values, String selection, String[] selectionArgs){

        return 0;
    }
}
