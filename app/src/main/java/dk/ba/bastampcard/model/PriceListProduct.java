package dk.ba.bastampcard.model;

/**
 * Created by Anders on 03-11-2014.
 */
public class PriceListProduct {
    private int id;
    private float price;
    private Product product;
    private Shop shop;

    public PriceListProduct(Product product, Shop shop, float price) {
        this.product = product;
        this.shop = shop;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    public float getPrice() {
        return price;
    }
    public Product getProduct() {
        return product;
    }
}
