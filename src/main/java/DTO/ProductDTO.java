package DTO;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class ProductDTO implements IProductDTO {
    private int id, recipeID, orderedBy, quantity;
    private String name;
    private List<IUserDTO> workers;
    private List<ICommodityDTO> commodities;
    private Date date;
    private boolean manufactured;   //FixMe Control this works with three states (NULL, false, true)

    public ProductDTO(int id, String name, int recipeID, int orderedBy, int quantity, List<IUserDTO> workers, List<ICommodityDTO> commodities, Date date, boolean manufactured) {   //Retrieve ProductDTO from database
        this.name = name;
        this.id = id;
        this.recipeID = recipeID;
        this.orderedBy = orderedBy;
        this.quantity = quantity;
        this.workers = workers;
        this.commodities = commodities;
        this.date = date;
        this.manufactured = manufactured;
    }
    public ProductDTO(int id, String name, int recipeID, int orderedBy, int quantity, List<IUserDTO> workers, List<ICommodityDTO> commodities, boolean manufactured) {              //Create ProductDTO to insert into database with current date
        this.name = name;
        this.id = id;
        this.recipeID = recipeID;
        this.orderedBy = orderedBy;
        this.quantity = quantity;
        this.workers = workers;
        this.commodities = commodities;
        this.date = new Date(Calendar.getInstance().getTime().getTime());   //FixMe Kontroller at dette virker
        this.manufactured = manufactured;
    }

    public int getID() {
        return id;
    }
    public int getRecipeID() {
        return recipeID;
    }
    public int getOrderedBy() {
        return orderedBy;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getName() {
        return name;
    }
    public List<IUserDTO> getWorkers() {
        return workers;
    }
    public List<ICommodityDTO> getCommodities() {
        return commodities;
    }
    public Date getDate() {
        return date;
    }
    public boolean isManufactured() {
        return manufactured;
    }
}
