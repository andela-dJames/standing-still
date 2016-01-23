package com.andela.standingstill.model;

import com.andela.standingstill.activity.Constants;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Movement{

    public enum Type{
        STILL,
        IN_VEHICLE,
        ON_FOOT,
        ON_BICYCLE,
        WALKING,
        RUNNING,
        TILTING,
        UNKNOWN
    }

    private int ID;
    private Type movementType;

    private long timeSpent;

    private DateTime date;

    private String address;

    private String coordinates;

    public static List<Movement> movements;

    public Movement() {
    }

    public Movement(long timeSpent, DateTime date, String address, String coordinates) {
        this.timeSpent = timeSpent;
        this.date = date;
        this.address = address;
        this.coordinates = coordinates;
        movements = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Type getMovementType() {
        return movementType;
    }

    public void setMovementType(Type movementType) {
        this.movementType = movementType;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String movementTypeToString() {
        switch (getMovementType()){
            case STILL:
                return Constants.STILL;
            case IN_VEHICLE:
                return Constants.IN_VEHICLE;
            case WALKING:
                return Constants.WALKING;
            case ON_FOOT:
                return Constants.ON_FOOT;
            case RUNNING:
                return Constants.RUNNING;
            case TILTING:
                return Constants.TILTING;
            case UNKNOWN:
                return Constants.UKNOWN;
            case ON_BICYCLE:
                return Constants.ON_BICYCLE;

            default:
                return "";

        }
    }

    public CharSequence getTimeSpentToString() {
        long sec = timeSpent / 1000;
        long min = sec / 60;
        long hr = min / 60;
        if (hr < 1){
            return (min <= 1) ? min + " min" : min+ " mins";
        }

        return ((hr <= 1) ? hr + " hr" : hr + " hrs") + ((min <= 1) ? min + " min" : min+ " mins");

    }

    public static Type stringToActivity(String activity) {
        switch (activity){
            case Constants.STILL:
                return Type.STILL;
            case Constants.IN_VEHICLE:
                return Type.IN_VEHICLE;
            case Constants.WALKING:
                return Type.WALKING;
            case Constants.ON_FOOT:
                return Type.ON_FOOT;
            case Constants.RUNNING:
                return Type.RUNNING;
            case Constants.TILTING:
                return Type.TILTING;
            case Constants.UKNOWN:
                return Type.UNKNOWN;
            default:
                return null;
        }
    }

}
