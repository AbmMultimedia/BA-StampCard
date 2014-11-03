package dk.ba.bastampcard.model;

/**
 * Created by Anders on 03-11-2014.
 */
public class PriceListProduct {
    private float price;
    private Product product;

    public PriceListProduct(Product product, float price) {
        this.product = product;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }
}
