package dk.ba.bastampcard.model;

import java.util.Date;

/**
 * Created by Anders on 03-11-2014.
 */
public class Purchase {
    private int id;
    private Product product;
    private Shop shop;
    private User user;
    private int confirmationCode;
    private float value;
    private Date date;

    public Purchase(Product product, Shop shop, User user, int confirmationCode, float value, Date date) {
        this.product = product;
        this.shop = shop;
        this.user = user;
        this.confirmationCode = confirmationCode;
        this.value = value;
        this.date = date;
    }

    //Getters
    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Shop getShop() {
        return shop;
    }

    public User getUser() {
        return user;
    }

    public float getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public int getConfirmationCode() {
        return confirmationCode;
    }

}
