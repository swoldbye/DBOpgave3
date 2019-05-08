package DTO;

import java.sql.Date;
import java.util.List;

public interface IRecipeDTO {

    int getRecipe_id();

    void setRecipe_id(int recipe_id);

    String getRecipe_name();

    void setRecipe_name(String recipe_name);

    Date getRegistration_date();

    void setRegistration_date(Date registration_date);

    int getStorage_time();

    void setStorage_time(int storage_time);

    List<IIngridient_lineDTO> getIngredient_line();

    void setIngredient_line(List<IIngridient_lineDTO> ingredient_line);

    String toString();

    void addIngredient_line(IIngridient_lineDTO object);
}
