package DAL;

import DTO.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    private DBConnection db = new DBConnection();

    //Create
    @Override
    public void createProduct(IProductDTO pro) throws DALException {
        try(Connection con = db.createConnection()) {
            String query = "INSERT INTO product VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preStatement = con.prepareStatement(query);

            preStatement.setInt(1, pro.getID());
            preStatement.setInt(2, pro.getOrderedBy());
            preStatement.setInt(3, pro.getRecipeID());
            preStatement.setInt(4, pro.getQuantity());
            preStatement.setDate(5, pro.getDate());

            preStatement.execute();

            createCommodityLines(pro, con);

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override //FixMe Should be called outside createProduct, hence the opportunity to setup Products without workers?
    public void createProductionLines(IProductDTO pro) throws DALException {
        try(Connection con = db.createConnection()){
            String query = "INSERT INTO production(?, ?)";
            PreparedStatement preStatement = con.prepareStatement(query);

            for(IUserDTO user : pro.getWorkers()) {
                preStatement.setInt(1, user.getID());
                preStatement.setInt(2, pro.getID());
                preStatement.execute();
            }

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override //FixMe Should be made private if not used outside of creating product
    public void createCommodityLines(IProductDTO pro, Connection con) throws SQLException {
        String query = "INSERT INTO commodity_line (?, ?)";
        PreparedStatement preStatement = con.prepareStatement(query);

        for(ICommodityDTO com : pro.getCommodities()) {
            preStatement.setInt(1, pro.getID());
            preStatement.setInt(2, com.getID());
            preStatement.execute();
        }
    }

    //Read
    @Override
    public List<IProductDTO> readAllProducts() throws DALException {
        List<IProductDTO> products = new ArrayList<>();

        try(Connection con = db.createConnection()) {
            ResultSet rsProducts = con.prepareStatement("SELECT * FROM product").executeQuery();

            while(rsProducts.next()) {
                int proID = rsProducts.getInt(1);
                List<IUserDTO> users = getProductWorkers(proID, con);
                List<ICommodityDTO> commodities = getProductCommodities(proID, con);

                IProductDTO product = new ProductDTO(
                        proID,                              //ID
                        rsProducts.getInt(2),   //Recipe Number
                        rsProducts.getInt(3),   //Orderers ID
                        rsProducts.getInt(4),   //Quantity of product
                        users,                              //Laborants working on it
                        commodities,                        //Commodities used
                        rsProducts.getDate(5)   //Date of production
                );
                products.add(product);
            }

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }

        return products;
    }

    private List<IUserDTO> getProductWorkers(int productID, Connection con) throws SQLException {
        IUserDAO userDAO = new UserDAO();
        List<IUserDTO> users = new ArrayList<>();

        PreparedStatement preStatement = con.prepareStatement("SELECT laborant_id FROM production WHERE product_id = ?");
        preStatement.setInt(1, productID);
        ResultSet rsUsers = preStatement.executeQuery();

        while(rsUsers.next()) {
            IUserDTO user = userDAO.getUser(rsUsers.getInt(1));
            users.add(user);
        }
        return users;
    }

    private List<ICommodityDTO> getProductCommodities(int productID, Connection con) throws SQLException {
        ICommodityDAO commodityDAO = new CommodityDAO();
        List<ICommodityDTO> commodities = new ArrayList<>();

        PreparedStatement preStatement = con.prepareStatement("SELECT commodity_batch_id FROM commodity_line WHERE product_batch_id = ?");
        preStatement.setInt(1, productID);
        ResultSet rsCommodities = preStatement.executeQuery();

        while(rsCommodities.next()) {
            ICommodityDTO commodity = commodityDAO.getCommmodity(rsCommodities.getInt(1));
            commodities.add(commodity);
        }
        return commodities;
    }

    //Update
    @Override //TODO
    public void updateProductInfo(IProductDTO pro) throws DALException {

    }

    //MarkAsFinished
    @Override //TODO
    public void markAsFinished(int id) throws DALException {

    }

    //Delete
    @Override //TODO
    public void deleteProduct(int id) throws DALException {

    }
}