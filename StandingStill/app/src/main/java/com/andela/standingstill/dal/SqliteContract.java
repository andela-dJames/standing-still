package com.andela.standingstill.dal;

import android.net.Uri;

/**
 * Created by andeladev on 14/01/2016.
 */
public class SqliteContract {
    /**
     * The content authority
     */
    public static final String CONTENT_AUTHORITY = "";
    /**
     * The Base ccontent URi
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("contents://" + CONTENT_AUTHORITY);
    /**
     * The name of the the rates Table
     */
    public static final String PATH_RATES = "rates";
    /**
     * the name of the currency Table
     */
    public static final String PATH_CURRENCY = "currency";
    /**
     * Text datatype
     */
    private static final String TEXT_TYPE = "TEXT";
    /**
     * comma separator
     */
    private static final String SEPARATOR =  ", ";
}
