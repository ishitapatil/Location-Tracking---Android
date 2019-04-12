package com.ishita.locationupdater.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.ishita.locationupdater.utility.Category;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationInformation {
    private String postalCode, country, state, city, locationName, addressLine;
    private double latitude, longitude;
    private Date visitTime;


    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public void setAddressParams(double latitude, double longitude, Context context) {
        Log.d(Category.CATEGORY_GENERAL, "LocationInformation getAddress ");
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            postalCode = obj.getPostalCode();
            this.latitude = latitude;
            this.longitude = longitude;
            visitTime = new Date(System.currentTimeMillis());
            country = obj.getCountryName();
            city = obj.getSubAdminArea();
            state = obj.getAdminArea();
            locationName = obj.getFeatureName();
            addressLine = obj.getAddressLine(0);
            CurrentLocationInformation.getInstance().setInformation(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
