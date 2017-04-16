package com.vcf.przemek.firstappsdk16.db;

import android.provider.BaseColumns;

/**
 * Created by Przemek on 2017-04-14.
 */

public class InsulinContainerReader {

    private InsulinContainerReader(){}

    public static class InsulinContainerEntry implements BaseColumns {
        public static final String TABLE_NAME = "insulin_container";
        public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
    }
}
