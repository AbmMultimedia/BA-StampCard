package dk.ba.bastampcard.model;

/**
 * Created by Anders on 03-11-2014.
 */
public class User {
    private int id;
    private String name;
    private int stamps;

    //Constructor
    public User(String name) {
        this.name = name;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStamps() {
        return stamps;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
