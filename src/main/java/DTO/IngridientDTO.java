package DTO;

import DTO.IIngridientDTO;

public class IngridientDTO implements IIngridientDTO {

    private int ingredient_id;
    private String ingredient_name;
    private boolean needs_refill;



    //Constructor for getting an ingredient from the DAL.

    public IngridientDTO(){}

    public IngridientDTO(int id, String name, boolean refill ){

        ingredient_id = id;
        ingredient_name = name;
        needs_refill = refill;

    }


    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public boolean getNeeds_refill() {
        return needs_refill;
    }

    public void setNeeds_refill(boolean needs_refill) {
        this.needs_refill = needs_refill;
    }

    public String toString(){
        String str = "ID: "+ingredient_id+", Name: "+ ingredient_name+", Needs refill: "+ingredient_name;
        return str;
    }
}
