package DTO;

import java.io.Serializable;

public class CommodityDTO implements Serializable, ICommodityDTO {

    int batch_id;
    int ingrdient_id;
    int quantity;
    boolean is_leftover;
    boolean manufacture;
    String ingredient_name;

    public CommodityDTO(){}

    public CommodityDTO(int batch_id, int ingrdient_id, int quantity, boolean is_leftover, boolean manufacture, String ingredient_name) {
        this.batch_id = batch_id;
        this.ingrdient_id = ingrdient_id;
        this.quantity = quantity;
        this.is_leftover = is_leftover;
        this.manufacture = manufacture;
        this.ingredient_name = ingredient_name;
    }

    public int getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(int batch_id) {
        this.batch_id = batch_id;
    }

    public int getIngrdient_id() {
        return ingrdient_id;
    }

    public void setIngrdient_id(int ingrdient_id) {
        this.ingrdient_id = ingrdient_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isIs_leftover() {
        return is_leftover;
    }

    public void setIs_leftover(boolean is_leftover) {
        this.is_leftover = is_leftover;
    }

    public boolean isManufacture() {
        return manufacture;
    }

    public void setManufacture(boolean manufacture) {
        this.manufacture = manufacture;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    @Override
    public String toString() {
        return "CommodityDTO{" +
                "batch_id=" + batch_id +
                ", ingrdient_id=" + ingrdient_id +
                ", quantity=" + quantity +
                ", is_leftover=" + is_leftover +
                ", manufacture=" + manufacture +
                ", ingredient_name='" + ingredient_name + '\'' +
                '}';
    }
}
