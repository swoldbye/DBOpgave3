package DTO;

import java.sql.Date;

public interface IRecipeDTO {

    int getRecipe_id();

    void setRecipe_id(int recipe_id);

    String getRecipe_name();

    void setRecipe_name(String recipe_name);

    Date getRegistration_date();

    void setRegistration_date(Date registration_date);

    int getStorage_time();

    void setStorage_time(int storage_time);

    String getIngredient_name();

    void setIngredient_name(String ingredient_name);

    double getQuantity();

    void setQuantity(double quantity);

    String toString();

}
