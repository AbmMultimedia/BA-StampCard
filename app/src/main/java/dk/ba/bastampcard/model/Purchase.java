package dk.ba.bastampcard.model;

import java.util.Date;

/**
 * Created by Anders on 03-11-2014.
 */
public class Purchase {

    public static final int STAMP_RATIO = 20;
    private int id;
    private PriceListProduct priceListProduct;
    private Shop shop;
    private User user;
    private int confirmationCode;
    private int quantity;
    private Date date;

    public Purchase(PriceListProduct priceListProduct, Shop shop, User user, int confirmationCode, int quantity, Date date) {
        this.priceListProduct = priceListProduct;
        this.shop = shop;
        this.user = user;
        this.confirmationCode = confirmationCode;
        this.quantity = quantity;
        this.date = date;
    }

    //Getters
    public int getId() {
        return id;
    }

    public PriceListProduct getPriceListProduct() {
        return priceListProduct;
    }

    public Shop getShop() {
        return shop;
    }

    public User getUser() {
        return user;
    }

    public float getValue() {
        float price = this.priceListProduct.getPrice();
        float value = price * quantity;
        return value;
    }

    public Date getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getConfirmationCode() {
        return confirmationCode;
    }

}
