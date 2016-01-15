package com.andela.standingstill.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqliteDBHelper extends SQLiteOpenHelper {

    /**
     * An Sqlite database
     */
    private SQLiteDatabase database;
    /**
     * The verion of the databse inorder to monitor upgrades and manage new versions
     */
    public final static int DATABASE_VERSION = 2;
    /**
     * The name of the database
     */
    public static final String DATABASE_NAME = "movement.db";

    public SqliteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqliteContract.CREATE_MOVEMENT_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqliteContract.DROP_MOVEMENT_TABLE_STATEMENT);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public  SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
