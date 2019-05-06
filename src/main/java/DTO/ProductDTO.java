package DTO;

import java.sql.Date;
import java.util.Calendar;

public class ProductDTO {
    private int id, recipe, orderedBy, quantity;
    private User[] workers;
    private Date date;

    ProductDTO(int id, int recipe, int orderedBy, int quantity, User[] workers, Date date) {   //Retrieve ProductDTO from
        this.id = id;
        this.recipe = recipe;
        this.orderedBy = orderedBy;
        this.quantity = quantity;
        this.workers = workers;
        this.date = date;
    }
    ProductDTO(int id, int recipe, int orderedBy, int quantity, User[] workers) {              //Create ProductDTO to insert into database with new date
        this.id = id;
        this.recipe = recipe;
        this.orderedBy = orderedBy;
        this.quantity = quantity;
        this.workers = workers;
        this.date = new Date(Calendar.getInstance().getTime().getTime());   //FixMe Kontroller at dette virker
    }

    public int getID() {
        return id;
    }
    public int getRecipeID() {
        return recipe;
    }
    public int getOrderedBy() {
        return orderedBy;
    }
    public int getQuantity() {
        return quantity;
    }
    public User[] getWorkersIDs() {
        return workers;
    }
    public Date getDate() {
        return date;
    }
}
