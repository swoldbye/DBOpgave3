package DAL;

import DTO.ProductDTO;

import java.sql.*;

public class ProductDAO implements IProductDAO {
    private DBConnection db = new DBConnection();

    //Create
    @Override
    public void createProduct(ProductDTO pro) throws DALException {
        try(Connection con = db.createConnection()) {
            String query = "INSERT INTO Product VALUES(?,?,?,?,?)";
            PreparedStatement proStatement = con.prepareStatement(query);

            proStatement.setInt(1, pro.getID());
            proStatement.setInt(2, pro.getOrderedBy());
            proStatement.setInt(3, pro.getRecipeID());
            proStatement.setInt(4, pro.getQuantity());
            proStatement.setDate(5, pro.getDate());

            proStatement.execute();

            createProductionLines(pro.getWorkersIDs());

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    private void createProductionLines(User[] workers) {

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