package com.andela.standingstill.dal;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class SqliteContract {
    /**
     * The content authority
     */
    public static final String CONTENT_AUTHORITY = "com.andela.standingstill";
    /**
     * The Base ccontent URi
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("contents://" + CONTENT_AUTHORITY);
    /**
     * the name of the  Table
     */
    public static final String PATH_MOVEMENTS = "movements";
    /**
     * Text datatype
     */
    private static final String TEXT_TYPE = "TEXT";
    /**
     * double datatype
     */
    private static final String DOUBLE_TYPE = "real";
    /**
     * comma separator
     */
    private static final String SEPARATOR =  ", ";

    public static final String CREATE_MOVEMENT_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS " +
            MovementTable.TABLE_NAME + "(" +
            MovementTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + SEPARATOR +
            MovementTable.DATE_COLUMN + " "+TEXT_TYPE + SEPARATOR +
            MovementTable.COORDINATE_COLUMN + " "+ TEXT_TYPE + SEPARATOR +
            MovementTable.LOCATION_ADDRESS_COLUMN + " " + TEXT_TYPE + SEPARATOR +
            MovementTable.TIME_ELAPSED_COLUMN + " " + TEXT_TYPE + SEPARATOR +
            MovementTable.MOVEMENT_TYPE_COLUMN + " " + TEXT_TYPE +" );";

    public static final String DROP_MOVEMENT_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + MovementTable.TABLE_NAME;

    public static class MovementTable implements BaseColumns{

        /**
         * Content URi foe the table
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVEMENTS)
                .build();
        /**
         * The content Type
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVEMENTS;


        public static final String TABLE_NAME = "movement";

        public static final String DATE_COLUMN = "date";

        public static final int DATE_COLUMN_INDEX = 1;

        public static final String COORDINATE_COLUMN = "coordinate";

        public static final int COORDINATE_COLUMN_INDEX = 2;

        public static final String LOCATION_ADDRESS_COLUMN = "address";

        public static final int LOCATION_ADDRESS_COLUMN_INDEX = 3;

        public static final String TIME_ELAPSED_COLUMN = "time_elapsed";

        public static final int TIME_SPENT_ELAPSED_COLUMN_INDEX = 4;

        public static final String MOVEMENT_TYPE_COLUMN = "activity";

        public static final int MOVEMENT_TYPE_COLUMN_INDEX = 5;



    }


}
