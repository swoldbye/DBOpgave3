package DAL;

import DTO.ProductDTO;

public interface IProductDAO {
    void createProduct(ProductDTO pro) throws DALException;     //Creates a new product
    ProductDTO[] readAllProducts() throws DALException;         //Returns list of all products
    void updateProductInfo(ProductDTO pro) throws DALException; //Update the information of a Product
    void markAsFinished(int id) throws DALException;            //Mark production as finished
    void deleteProduct(int id) throws DALException;             //Delete
}