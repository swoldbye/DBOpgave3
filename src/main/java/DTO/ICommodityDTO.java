package DTO;

public interface ICommodityDTO {

    int getBatch_id();

    void setBatch_id(int batch_id);

    int getIngredient_id();

    void setIngredient_id(int ingrdient_id);

    double getQuantity();

    void setQuantity(double quantity);

    boolean isIs_leftover();

    void setIs_leftover(boolean is_leftover);

    String getCommodity_manufacturer();

    void setCommodity_manufacturer(String commodity_manufacturer);

    String getIngredient_name();

    void setIngredient_name(String ingredient_name);

    String toString();

}
