package checkpoint4.andela.com.standingstill.model;

import org.joda.time.DateTime;

public class Movement {

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


    private Type movementType;

    private long timeSpent;

    private DateTime date;

    private String address;

    public Movement() {
    }

    public Movement(long timeSpent, DateTime date, String address) {
        this.timeSpent = timeSpent;
        this.date = date;
        this.address = address;
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
}
