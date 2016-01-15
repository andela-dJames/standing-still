package com.andela.standingstill.dal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;


public class TestDB extends AndroidTestCase {
    public static final String TAG = TestDB.class.getSimpleName();

    void deleteDb(){
        mContext.deleteDatabase(SqliteDBHelper.DATABASE_NAME);
    }

    @Override
    public void setUp() throws Exception {
        deleteDb();
    }

    public void testCreateDB(){
        final HashSet<String> tableNames = new HashSet<String>();
        tableNames.add(SqliteContract.MovementTable.TABLE_NAME);
        mContext.deleteDatabase(SqliteDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new SqliteDBHelper(this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());
        Cursor cr = db.rawQuery("SELECT name FROM sqlite_master where type ='table'", null);
        assertTrue("Error: database has not been created correctly", cr.moveToFirst());

        do {
            tableNames.remove(cr.getString(0));
        }while (cr.moveToNext());
        assertTrue("Error: Your database was created without both the rates entry and currency entry tables",
                tableNames.isEmpty());

        cr = db.rawQuery("PRAGMA table_info(" + SqliteContract.MovementTable.TABLE_NAME + ")",
                null);

        assertTrue("Error: this means that we are not able to query database for table information", cr.moveToFirst());

        final HashSet<String> movementTableColumnNames = new HashSet<>();
        movementTableColumnNames.add(SqliteContract.MovementTable.DATE_COLUMN);
        movementTableColumnNames.add(SqliteContract.MovementTable.COORDINATE_COLUMN);
        movementTableColumnNames.add(SqliteContract.MovementTable.LOCATION_ADDRESS_COLUMN);
        movementTableColumnNames.add(SqliteContract.MovementTable.TIME_ELAPSED_COLUMN);
        movementTableColumnNames.add(SqliteContract.MovementTable.MOVEMENT_TYPE_COLUMN);
        int columnNameIndex = cr.getColumnIndex("name");

        do {
            String columnName = cr.getString(columnNameIndex);
            movementTableColumnNames.remove(columnName);
        } while(cr.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movementTableColumnNames.isEmpty());
        db.close();
    }

}