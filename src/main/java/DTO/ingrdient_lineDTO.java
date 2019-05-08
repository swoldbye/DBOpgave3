package DTO;

public class ingrdient_lineDTO implements IIngridient_lineDTO {

    private int ingredient_id;
    private double quantity;
    private String Ingredient_name;

    public ingrdient_lineDTO() {
    }

    public ingrdient_lineDTO(int ingredient_id, double quantity) {
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
    }

    public ingrdient_lineDTO(double quantity, String name) {
        this.quantity = quantity;
        this.Ingredient_name = name;
    }
    public ingrdient_lineDTO(int ingredient_id, double quantity, String ingredient_name) {
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
        Ingredient_name = ingredient_name;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getIngredient_name() {
        return Ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        Ingredient_name = ingredient_name;
    }

}
