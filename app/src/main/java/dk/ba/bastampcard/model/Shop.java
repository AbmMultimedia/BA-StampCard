package dk.ba.bastampcard.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anders on 03-11-2014.
 */

public class Shop {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String city;
    private double latitude;
    private double longitude;
    private List<PriceListProduct> priceList;

    //Constructor
    public Shop(String name, String address, String postalCode, String city, double latitude, double longitude){
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;

        priceList = new ArrayList<PriceListProduct>();
    }

    public Shop(int id){
        this.id = id;
    }


    //Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    //Add product to price list
    public void addPriceListProduct(Product product, float price){
        PriceListProduct priceListProduct = new PriceListProduct(product, this, price);
        priceList.add(priceListProduct);
    }

    //Calculate distance to the shop
    public float getDistance(double currentLatitude, double currentLongitude){
        float[] distances = new float[1];
        Location.distanceBetween(currentLatitude, currentLongitude, this.latitude, this.longitude, distances);

        float distance = distances[0];
        return distance;
    }

}
