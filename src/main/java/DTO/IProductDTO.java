package DTO;

import java.sql.Date;
import java.util.List;

public interface IProductDTO {
    public int getID();
    public int getRecipeID();
    public int getOrderedBy();
    public int getQuantity();
    public List<IUserDTO> getWorkers();
    public List<ICommodityDTO> getCommodities();
    public Date getDate();
}
