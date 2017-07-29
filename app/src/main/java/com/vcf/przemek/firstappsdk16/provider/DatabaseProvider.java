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
    private static final String provider_package = "com.vcf.przemek.firstappsdk16.provider.DatabaseProvider";

    InfusionSetDatabase dbHelper;

    static {
        sUriMatcher.addURI(provider_package, "infusion_set", 1);
        sUriMatcher.addURI(provider_package, "infusion_set/#", 2);
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