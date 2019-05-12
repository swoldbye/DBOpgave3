package DAL;

import DTO.IProductDTO;
import DTO.IUserDTO;
import DTO.ProductDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IProductDAO {
    boolean createProduct(IProductDTO pro) throws DALException;     //Creates a new product
//    void createProduct(IProductDTO pro) throws DALException;     //Creates a new product
//    void createProductionLines(IProductDTO pro, Connection con) throws DALException;
//    void createCommodityLines(IProductDTO pro, Connection con) throws SQLException;
    List<IProductDTO> getAllProducts() throws DALException;         //Returns list of all products
    IProductDTO getProduct(int id) throws DALException;   //Returns a single Product
    boolean updateProductInfo(IProductDTO pro, List<IUserDTO> oldUsers) throws DALException; //Update the information of a Product
    void markAsFinished(int id) throws DALException;            //Mark production as finished //TODO implement in database
    void deleteProduct(int id) throws DALException;             //Delete
}