package com.andela.standingstill.dal;

import com.andela.standingstill.model.Movement;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by andeladev on 14/01/2016.
 */
public interface DataCollection {

    long save(Movement movement);

    Movement getByID(int id);

    List<Movement> getByDate(DateTime dateTime, Selection selection);

    List<Movement> getByLocationAddress(String address, Selection selection);

    List<Movement> getByCordinates(String coordinates, Selection selection);

    List<Movement> getByMovementType(Movement.Type movmentType, Selection selection);

    List<Movement> listAll();

    enum Selection {
        ID,
        WHEN,
        TYPE,
        LOCATION_COORDINATES,
        LOCATION_ADDRESS
    }


}
