package DTO;

public interface IIngridientDTO {

    int getIngredient_id();

    void setIngredient_id(int ingredient_id);

    String getIngredient_name();

    void setIngredient_name(String ingredient_name);

    boolean getNeeds_refill();

    void setNeeds_refill(boolean needs_refill);

    String toString();

}
