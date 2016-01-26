package com.andela.standingstill.dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.andela.standingstill.model.Movement;

import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.List;


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

    public long insertMovement() {
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(mContext);
        //SQLiteDatabase db = sqliteDBHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovementValues();
        long rowID = sqliteDBHelper.getDatabase().insert(SqliteContract.MovementTable.TABLE_NAME, null, testValues);
        assertTrue(rowID !=-1);

        Cursor cursor = sqliteDBHelper.getDatabase().query(
                SqliteContract.MovementTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );

        return rowID;
    }

    public void testRateTable() {
        long id = insertMovement();
        assertFalse("Error: Location Not Inserted Correctly", id == -1L);

    }

    public void testGetByID() {
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(mContext);
        DataAccess dataAccess = new DataAccess(sqliteDBHelper);
        Movement movement = new Movement();
        movement.setDate(DateTime.now());
        movement.setMovementType(Movement.Type.STILL);
        movement.setAddress("55, Moleye Street");
        movement.setTimeSpent(152677);
        movement.setCoordinates("6.34554, 3.89992");

        dataAccess.save(movement);
        Movement myMovement = dataAccess.getByID(1);
        assertEquals("55, Moleye Street", myMovement.getAddress());
        assertEquals("STILL", myMovement.getMovementType().toString());


    }

    public void testGetAll() {
        deleteDb();
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(mContext);
        DataAccess dataAccess = new DataAccess(sqliteDBHelper);
        Movement movement = new Movement();
        movement.setDate(DateTime.now());
        movement.setMovementType(Movement.Type.STILL);
        movement.setAddress("55, Moleye Street");
        movement.setTimeSpent(152677);
        movement.setCoordinates("6.34554, 3.89992");

        Movement movement2 = new Movement();
        movement2.setDate(DateTime.now());
        movement2.setMovementType(Movement.Type.STILL);
        movement2.setAddress("55, Moleye Street");
        movement2.setTimeSpent(152677);
        movement2.setCoordinates("6.34554, 3.89992");

        dataAccess.save(movement);
        dataAccess.save(movement2);
        Movement myMovement = dataAccess.getByID(1);
        List<Movement> movements = dataAccess.listAll();

        assertEquals(2, movements.size());

        System.out.print(movement.getDate());

    }

    public void testGetByMovementType() {

        deleteDb();
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(mContext);
        DataAccess dataAccess = new DataAccess(sqliteDBHelper);
        Movement movement = new Movement();
        movement.setDate(DateTime.now());
        movement.setMovementType(Movement.Type.WALKING);
        movement.setAddress("55, Moleye Street");
        movement.setTimeSpent(152677);
        movement.setCoordinates("6.34554, 3.89992");

        Movement movement2 = new Movement();
        movement2.setDate(DateTime.now());
        movement2.setMovementType(Movement.Type.STILL);
        movement2.setAddress("55, Moleye Street");
        movement2.setTimeSpent(152677);
        movement2.setCoordinates("6.34554, 3.89992");

        dataAccess.save(movement);
        dataAccess.save(movement2);
        List<Movement> movements = dataAccess.getByMovementType(Movement.Type.WALKING, DataCollection.Selection.LOCATION_ADDRESS);

        assertEquals(1, movements.size());


    }

    public void testGetByCoordinate(){

        deleteDb();
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(mContext);
        DataAccess dataAccess = new DataAccess(sqliteDBHelper);
        Movement movement = new Movement();
        movement.setDate(DateTime.now());
        movement.setMovementType(Movement.Type.WALKING);
        movement.setAddress("55, Moleye Street");
        movement.setTimeSpent(152677);
        movement.setCoordinates("6.34554, 3.89992");

        Movement movement2 = new Movement();
        movement2.setDate(DateTime.now());
        movement2.setMovementType(Movement.Type.STILL);
        movement2.setAddress("55, Moleye Street");
        movement2.setTimeSpent(152677);
        movement2.setCoordinates("6.34554, 3.89992");

        dataAccess.save(movement);
        dataAccess.save(movement2);
        //Movement myMovement = dataAccess.getByID(1);
        List<Movement> movements = dataAccess.getByCordinates(movement.getCoordinates(), DataCollection.Selection.LOCATION_ADDRESS);

        assertEquals(2, movements.size());

    }

    public void testGetByAddress(){

        deleteDb();
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(mContext);
        DataAccess dataAccess = new DataAccess(sqliteDBHelper);
        Movement movement = new Movement();
        movement.setDate(DateTime.now());
        movement.setMovementType(Movement.Type.WALKING);
        movement.setAddress("55, Moleye Street");
        movement.setTimeSpent(152677);
        movement.setCoordinates("6.34554, 3.89992");

        Movement movement2 = new Movement();
        movement2.setDate(DateTime.now());
        movement2.setMovementType(Movement.Type.STILL);
        movement2.setAddress("55, Moleye Street");
        movement2.setTimeSpent(152677);
        movement2.setCoordinates("6.34554, 3.89992");

        dataAccess.save(movement);
        dataAccess.save(movement2);
        //Movement myMovement = dataAccess.getByID(1);
        List<Movement> movements = dataAccess.getByLocationAddress("55, Moleye Street", DataCollection.Selection.LOCATION_ADDRESS);

        assertEquals(2, movements.size());

    }

}