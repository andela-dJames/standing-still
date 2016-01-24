package com.andela.standingstill.dal;

import android.content.ContentValues;
import android.database.Cursor;

import com.andela.standingstill.model.Movement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for performing CRUD operations
 */
public class DataAccess implements DataCollection {

    private SqliteDBHelper sqliteDBHelper;

    public DataAccess(SqliteDBHelper sqliteDBHelper) {
        this.sqliteDBHelper = sqliteDBHelper;
    }

    /**
     * Insert into Database
     * @param movement
     * @return
     */
    private long insert(Movement movement) {
        ContentValues values = insertValues(movement);

        return sqliteDBHelper.getDatabase().insert(SqliteContract.MovementTable.TABLE_NAME, null, values);
    }

    /**
     * Get Data from Cursor
     * @param cursor
     * @return
     */
    private Movement getFromCusor(Cursor cursor) {
        Movement movement = new Movement();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, MMMM d, y");

        movement.setID((int) cursor.getLong(0));
        movement.setDate(DateTime.parse(cursor.getString(SqliteContract.MovementTable.DATE_COLUMN_INDEX), formatter));
        movement.setCoordinates(cursor.getString(SqliteContract.MovementTable.COORDINATE_COLUMN_INDEX));
        movement.setAddress(cursor.getString(SqliteContract.MovementTable.LOCATION_ADDRESS_COLUMN_INDEX));
        movement.setTimeSpent(Long.parseLong(cursor.getString(SqliteContract.MovementTable.TIME_SPENT_ELAPSED_COLUMN_INDEX)));
        movement.setMovementType(Movement.Type.valueOf(cursor.getString(SqliteContract.MovementTable.MOVEMENT_TYPE_COLUMN_INDEX)));
        return movement;
    }

    /**
     * Update DB
     * @param movement
     * @return
     */
    public int updateTable(Movement movement) {
        ContentValues values = insertValues(movement);

        String whereClause = SqliteContract.MovementTable._ID + " = ?";
        String[] whereArgs = { String.valueOf(movement.getID()) };

        return sqliteDBHelper.getDatabase().update(SqliteContract.MovementTable.TABLE_NAME, values, whereClause, whereArgs);
    }

    /**
     * Query Database for a record
     * @param selection
     * @param selectionargs
     * @param groupBy
     * @param sortOrder
     * @return
     */
    public List<Movement> query( String selection, String[] selectionargs, String groupBy, String sortOrder ) {

        Cursor cursor = sqliteDBHelper.getDatabase().query(SqliteContract.MovementTable.TABLE_NAME,
                columnNames(), selection, selectionargs, groupBy, null, sortOrder);

        List<Movement> movements = new ArrayList<>();

        while (cursor.moveToNext()) {
            movements.add(getFromCusor(cursor));
        }

        cursor.close();

        return movements;

    }

    /**
     * Create Content values
     * @param movement
     * @return
     */
    private ContentValues insertValues(Movement movement) {
        ContentValues values = new ContentValues();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, MMMM d, y");
        values.put(SqliteContract.MovementTable.DATE_COLUMN, movement.getDate().toString(formatter));
        values.put(SqliteContract.MovementTable.COORDINATE_COLUMN, movement.getCoordinates());
        values.put(SqliteContract.MovementTable.LOCATION_ADDRESS_COLUMN, movement.getAddress());
        values.put(SqliteContract.MovementTable.TIME_ELAPSED_COLUMN, movement.getTimeSpent());
        values.put(SqliteContract.MovementTable.MOVEMENT_TYPE_COLUMN, movement.getMovementType().toString());
        return values;

    }

    /**
     * Retreive record
     * @param selection
     * @param selectionargs
     * @param sortOrder
     * @return
     */
    private Movement retrieve(String selection, String[] selectionargs, String sortOrder){

        Cursor cursor = sqliteDBHelper.getDatabase().query(SqliteContract.MovementTable.TABLE_NAME,
                columnNames(), selection, selectionargs, null, null, sortOrder);
        if (!cursor.moveToNext()) {
            return null;
        }

        Movement movement = getFromCusor(cursor);

        cursor.close();

        return movement;

    }


    @Override
    public long save(Movement movement) {

        return insert(movement);

    }

    @Override
    public Movement getByID(int id) {
        String selection = SqliteContract.MovementTable._ID + " = ?";

        String[] args = { String.valueOf(id) };

        String orderBy = SqliteContract.MovementTable._ID;

        return retrieve(selection, args, orderBy);
    }

    @Override
    public List<Movement> getByDate(DateTime dateTime, Selection selection) {
        String querySelection = SqliteContract.MovementTable.DATE_COLUMN + " = ?";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, MMMM d, y");


        String[] args = {dateTime.toString(formatter)};

        String sortOrder = SqliteContract.MovementTable._ID;

        return query(querySelection, args, null, sortOrder);
    }

    @Override
    public List<Movement> getByLocationAddress(String address, Selection selection) {
        String querySelection = SqliteContract.MovementTable.LOCATION_ADDRESS_COLUMN + " = ?";

        String[] args = {address};

        String sortOrder = SqliteContract.MovementTable._ID;

        return query(querySelection, args, null, sortOrder);
    }

    @Override
    public List<Movement> getByCordinates(String coordinates, Selection selection) {

        String querySelection = SqliteContract.MovementTable.COORDINATE_COLUMN + " = ?";

        String[] args = {coordinates};

        String sortOrder = SqliteContract.MovementTable._ID;

        return query(querySelection, args, null, sortOrder);    }

    @Override
    public List<Movement> getByMovementType(Movement.Type movmentType, Selection selection) {
        String querySelection = SqliteContract.MovementTable.MOVEMENT_TYPE_COLUMN + " = ?";

        String[] args = {movmentType.toString()};

        String sortOrder = SqliteContract.MovementTable._ID;

        return query(querySelection, args, null, sortOrder);
    }

    @Override
    public List<Movement> listAll() {
        return query(null, null, null, null);
    }

    public String[] columnNames() {
        String[] columns = {
                SqliteContract.MovementTable._ID,
                SqliteContract.MovementTable.DATE_COLUMN,
                SqliteContract.MovementTable.COORDINATE_COLUMN,
                SqliteContract.MovementTable.LOCATION_ADDRESS_COLUMN,
                SqliteContract.MovementTable.TIME_ELAPSED_COLUMN,
                SqliteContract.MovementTable.MOVEMENT_TYPE_COLUMN,
        };
        return columns;
    }
}
