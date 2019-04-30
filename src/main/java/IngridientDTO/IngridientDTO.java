package IngridientDTO;

public class IngridientDTO implements IIngridientDTO {

    private int ingredient_id;
    private String ingredient_name;
    private boolean needs_refill;

    public IngridientDTO(int id, String name, boolean gotIt ){

        ingredient_id = id;
        ingredient_name = name;
        needs_refill = gotIt;

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

    public boolean isNeeds_refill() {
        return needs_refill;
    }

    public void setNeeds_refill(boolean needs_refill) {
        this.needs_refill = needs_refill;
    }
}
