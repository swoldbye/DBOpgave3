package DTO;

public interface ICommodityDTO {

    int getBatch_id();

    void setBatch_id(int batch_id);

    int getIngrdient_id();

    void setIngrdient_id(int ingrdient_id);

    int getQuantity();

    void setQuantity(int quantity);

    boolean isIs_leftover();

    void setIs_leftover(boolean is_leftover);

    String getCommodity_manufacturer();

    void setCommodity_manufacturer(String commodity_manufacturer);

    String getIngredient_name();

    void setIngredient_name(String ingredient_name);

}
