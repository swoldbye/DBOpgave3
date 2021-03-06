package DTO;

import java.io.Serializable;

public class CommodityDTO implements Serializable, ICommodityDTO {

    private int batch_id;
    private int ingredient_id;
    private double quantity;
    private boolean is_leftover;
    private String commodity_manufacturer;
    private String ingredient_name;

    public CommodityDTO(){}

    public CommodityDTO(int batch_id, int ingredient_id, double quantity, boolean is_leftover, String commodity_manufacturer) {
        this.batch_id = batch_id;
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
        this.is_leftover = is_leftover;
        this.commodity_manufacturer = commodity_manufacturer;
    }

    public CommodityDTO(int batch_id, int ingredient_id, double quantity, boolean is_leftover,
                        String commodity_manufacturer, String ingredient_name) {
        this.batch_id = batch_id;
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
        this.is_leftover = is_leftover;
        this.commodity_manufacturer = commodity_manufacturer;
        this.ingredient_name = ingredient_name;
    }

    public int getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(int batch_id) {
        this.batch_id = batch_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingrdient_id) {
        this.ingredient_id = ingrdient_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public boolean isIs_leftover() {
        return is_leftover;
    }

    public void setIs_leftover(boolean is_leftover) {
        this.is_leftover = is_leftover;
    }

    public String getCommodity_manufacturer() {
        return commodity_manufacturer;
    }

    public void setCommodity_manufacturer(String commodity_manufacturer) {
        this.commodity_manufacturer = commodity_manufacturer;
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
                ", ingrdient_id=" + ingredient_id +
                ", quantity=" + quantity +
                ", is_leftover=" + is_leftover +
                ", commodity_manufacturer='" + commodity_manufacturer + '\'' +
                ", ingredient_name='" + ingredient_name + '\'' +
                '}';
    }
}
