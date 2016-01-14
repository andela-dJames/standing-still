package checkpoint4.andela.com.standingstill.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


public class Place {

    private String Address;

    private double longitude;

    private double latitude;

    private long timeSpent;

    private DateTime dateVisited;

    private List<Movement> movementList;

    public Place() {
        movementList = new ArrayList<>();
    }

    public Place(String address, double longitude, double latitude, long timeSpent, DateTime dateVisited) {
        this();
        Address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeSpent = timeSpent;
        this.dateVisited = dateVisited;
    }

    public void addMovement(Movement movement) {
        movementList.add(movement);

    }

    public List<Movement> getMovementList() {
        return movementList;
    }

    public void setMovementList(List<Movement> movementList) {
        this.movementList = movementList;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public DateTime getDateVisited() {
        return dateVisited;
    }

    public void setDateVisited(DateTime dateVisited) {
        this.dateVisited = dateVisited;
    }
}
