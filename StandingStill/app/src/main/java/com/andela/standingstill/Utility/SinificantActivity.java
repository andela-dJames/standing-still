package com.andela.standingstill.Utility;

/**
 * Created by andeladev on 16/01/2016.
 */
public enum SinificantActivity {

    STILL ("Standing still", 1),
    WALKING ("Walking", 2),
    IN_VEHICLE ("In vehicle", 3),
    ON_BICYCLE ("On Bicycle", 4);

    private String activity;
    private int code;
    private String imageName;

    SinificantActivity(String activity, int code) {
        this.activity = activity;
        this.code = code;
    }

    SinificantActivity(String activity, int code, String imageName) {
        this.activity = activity;
        this.code = code;
        this.imageName = imageName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
