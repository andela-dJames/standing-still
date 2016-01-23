package com.andela.standingstill.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public CharSequence getTimeSpentToString() {
        long sec = timeSpent / 1000;
        long min = sec / 60;
        long hr = min / 60;
        if (hr < 1){
            return (min <= 1) ? min + " min" : min+ " mins";
        }

        return ((hr <= 1) ? hr + " hr" : hr + " hrs") + ((min <= 1) ? min + " min" : min+ " mins");

    }

    public static Map<String, List<Movement>> groupByDate(List<Movement> list){
        Map<String, List<Movement>> group = new HashMap<>();
        for (Movement movement: list){
            String key = movement.getAddress();
            if (!group.containsKey(key)){
                List<Movement> places = new ArrayList<>();
                places.add(movement);
                group.put(key, places);
            }
            else {
                group.get(key).add(movement);
            }
        }


        return group;
    }

    public List<Place> getPlaces(List<Movement> movements) {
        List<Place>places = new ArrayList<>();
        Place p;
        Map<String, List<Movement>> map;
        map = groupByDate(movements);
        for (String key: map.keySet()){
            p = new Place();
            p.setAddress(key);
            p.setTimeSpent(totalTimeSpent(map.get(key)));
            p.setMovementList(map.get(key));
            places.add(p);
        }

        return places;
    }

    public long totalTimeSpent(List<Movement> movements){
        long val = 0;
        for (Movement movement : movements){
            val += movement.getTimeSpent();
        }
        return val;
    }
}
