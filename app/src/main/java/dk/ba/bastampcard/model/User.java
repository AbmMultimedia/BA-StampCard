package dk.ba.bastampcard.model;

/**
 * Created by Anders on 03-11-2014.
 */
public class User {
    private int id;
    private String name;
    private String userName;

    //Constructor
    public User(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public int getStamps() {
        /* TODO Calculate numbers of stamps from the value of all purchase*/
        return 5;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
