package com.ishita.locationupdater.model;

public class CurrentLocationInformation {
    private static CurrentLocationInformation ourInstance;

    private LocationInformation information;
    public static CurrentLocationInformation getInstance() {
        if(ourInstance == null){
            ourInstance = new CurrentLocationInformation();
        }
        return ourInstance;
    }

    private CurrentLocationInformation() {

    }

    public LocationInformation getInformation() {
        return information;
    }

    public void setInformation(LocationInformation information) {
        this.information = information;
    }
}
