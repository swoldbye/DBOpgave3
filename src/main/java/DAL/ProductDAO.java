package DAO;

import model.ProductDTO;

import java.sql.*;

public class ProductDAO implements IProductDAO {

    private final String dbURL = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s180943?";
    private final String dbName = "s180943";
    private final String dbPass = "UXZTadQzbPrlIosGCZYNF";

    private Connection getConnection() throws DALException {
        try {
            return DriverManager.getConnection(dbURL, dbName, dbPass);
        } catch(SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
    //Create

    @Override
    public void createProduct(ProductDTO pro) throws DALException {
        try(Connection con = getConnection()) {
            String query = "INSERT INTO Product VALUES(?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(query);

            //TODO Set into statement
            statement.setInt(1, pro.getID());


            statement.execute();

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
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