package com.vcf.przemek.firstappsdk16.db;

import android.provider.BaseColumns;

/**
 * Created by pteodorski on 09.02.2017.
 */

public final class InfusionSetReader {

    private InfusionSetReader(){}

    public static class InfusionSetEntry implements BaseColumns{
        public static final String TABLE_NAME = "infusion_set";
        public static final String COLUMN_NAME_PLACE = "place";
        public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
        public static final String COLUMN_NAME_NOT_WORKING = "not_working";
    }
}
