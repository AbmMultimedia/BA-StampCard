package dk.ba.bastampcard.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
    private List<PriceListProduct> priceList;

    //Constructor
    public Shop(String name, String address, String postalCode, String city){
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;

        priceList = new ArrayList<PriceListProduct>();
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

    //Add product to price list
    public void addPriceListProduct(Product product, float price){
        PriceListProduct priceListProduct = new PriceListProduct(product, this, price);
        priceList.add(priceListProduct);
    }

    public float getDistance(Context context, double currentlatitude, double currentLongitude) throws IOException {
        float distance = 2000;

        return distance;
    }

}
