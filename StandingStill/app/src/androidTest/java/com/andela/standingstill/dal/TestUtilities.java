package com.andela.standingstill.dal;

import android.content.ContentValues;

import com.andela.standingstill.model.Movement;

import org.joda.time.DateTime;

/**
 * Created by andeladev on 15/01/2016.
 */
public class TestUtilities {
    public static ContentValues createMovementValues() {
        Movement movement = new Movement();
        movement.setDate(DateTime.now());
        movement.setMovementType(Movement.Type.STILL);
        movement.setTimeSpent(152677);
        movement.setCoordinates("6.34554, 3.89992");
        ContentValues values = new ContentValues();
        values.put(SqliteContract.MovementTable.DATE_COLUMN, String.valueOf(movement.getDate()));
        values.put(SqliteContract.MovementTable.COORDINATE_COLUMN, movement.getCoordinates());
        values.put(SqliteContract.MovementTable.MOVEMENT_TYPE_COLUMN, movement.getMovementType().toString());
        values.put(SqliteContract.MovementTable.LOCATION_ADDRESS_COLUMN, movement.getAddress());
        values.put(SqliteContract.MovementTable.TIME_ELAPSED_COLUMN, String.valueOf(movement.getTimeSpent()));
        return values;
    }
}
