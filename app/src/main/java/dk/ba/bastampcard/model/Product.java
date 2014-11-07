package dk.ba.bastampcard.model;

/**
 * Created by Anders on 03-11-2014.
 */
public class Product {
    private int id;
    private String name;

    //Constructor
    public Product(String name, int scale) {
        this.name = name;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
