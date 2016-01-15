package com.andela.standingstill.dal;

import com.andela.standingstill.model.Movement;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by andeladev on 14/01/2016.
 */
public interface DataCollection {

    public long save(Movement movement);

    public Movement getByID(int id);

    public List<Movement> getByDate(DateTime dateTime, Selection selection );

    public List<Movement> getByLocationAddress(String address, Selection selection);

    public List<Movement> getByCordinates(String coordinates, Selection selection);

    public List<Movement> getByMovementType(Movement.Type movmentType, Selection selection);

    public List<Movement> listAll(Selection selection);

    enum Selection {
        ID,
        WHEN,
        TYPE,
        LOCATION_COORDINATES,
        LOCATION_ADDRESS
    }


}
