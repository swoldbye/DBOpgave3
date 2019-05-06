package DTO;

import java.sql.Date;

public class RecipeDTO {

    private int recipe_id;
    private String recipe_name;
    private int quantity;
    private Date registration_date;
    private int storage_time;

    public RecipeDTO() {}

    public RecipeDTO(int recipe_id, String recipe_name, int quantity, Date registration_date, int storage_time) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.quantity = quantity;
        this.registration_date = registration_date;
        this.storage_time = storage_time;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    /** Returns the storage time in number of months */
    public int getStorage_time() {
        return storage_time;
    }

    /** Sets the storage time in number of months */
    public void setStorage_time(int storage_time) {
        this.storage_time = storage_time;
    }
}
