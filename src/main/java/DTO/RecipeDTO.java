package DTO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class RecipeDTO implements IRecipeDTO{

    private int recipe_id;
    private String recipe_name;
    private Date registration_date;
    private int storage_time;
    private List<IIngredient_lineDTO> ingredient_line = new ArrayList<>();

    public RecipeDTO(){}

    public RecipeDTO(int recipe_id, String recipe_name, Date registration_date, int storage_time) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
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

    public List<IIngredient_lineDTO> getIngredient_line() {
        return ingredient_line;
    }

    public void setIngredient_line(List<IIngredient_lineDTO> ingredient_line) {
        this.ingredient_line = ingredient_line;
    }

    public void addIngredient_line(IIngredient_lineDTO line){
        this.ingredient_line.add(line);
    }

    public boolean removeIngredient_line(IIngredient_lineDTO line){
        return this.ingredient_line.remove(line);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        String mainData = "RecipeDTO{" +
                "recipe_id=" + recipe_id +
                ", recipe_name='" + recipe_name + '\'' +
                ", registration_date=" + registration_date +
                ", storage_time=" + storage_time +'}';

        str.append(mainData);

        str.append("Ingredience; ");

                for(int i = 0; i<ingredient_line.size();i++){
                    str.append(" "+ ingredient_line.get(1).toString());
                }

                return str.toString();
    }
}
