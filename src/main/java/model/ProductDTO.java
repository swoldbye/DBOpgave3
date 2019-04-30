package model;

import java.util.Date;

public class ProductDTO {
    private int id, recipe, orderedBy;
    private int[] workers;
    private Date date;

    ProductDTO(int id, int recipe, int orderedBy, int[] workers, Date date) {   //Retrieve ProductDTO from
        this.id = id;
        this.recipe = recipe;
        this.orderedBy = orderedBy;
        this.workers = workers;
        this.date = date;
    }
    ProductDTO(int id, int recipe, int orderedBy, int[] workers) {              //Create ProductDTO to insert into database with new date
        this.id = id;
        this.recipe = recipe;
        this.orderedBy = orderedBy;
        this.workers = workers;
        this.date = new Date();
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
    public int[] getWorkersIDs() {
        return workers;
    }
    public Date getDate() {
        return date;
    }
}
