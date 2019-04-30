package DAO;

import model.ProductDTO;

public class ProductDAO implements IProductDAO {

    private final String dbURL = "ec2-52-30-211-3.eu-west-1.compute.amazonaws.com";
    private final String dbName = "s180943";
    private final String dbPass = "UXZTadQzbPrlIosGCZYNF";

    //Create
    @Override
    public void createProduct(ProductDTO pro) throws DALException {

    }

    //Read
    @Override
    public ProductDTO[] readAllProducts() throws DALException {
        return new ProductDTO[0];
    }

    //Update
    @Override
    public void updateProductInfo(ProductDTO pro) throws DALException {

    }

    //MarkAsFinished
    @Override
    public void markAsFinished(int id) throws DALException {

    }

    //Delete
    @Override
    public void deleteProduct(int id) throws DALException {

    }
}
