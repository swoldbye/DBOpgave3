package DAL;

import DTO.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    private DBConnection db = new DBConnection();

    //Create
    @Override
    public boolean createProduct(IProductDTO pro) throws DALException {
        try(Connection con = db.createConnection()) {
            String query = "INSERT INTO product VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement preStatement = con.prepareStatement(query);

            preStatement.setInt(1, pro.getID());
            preStatement.setInt(2, pro.getOrderedBy());
            preStatement.setInt(3, pro.getRecipeID());
            preStatement.setInt(4, pro.getQuantity());
            preStatement.setDate(5, pro.getDate());
            preStatement.setBoolean(6, pro.isManufactured());

            preStatement.execute();

            if(!pro.getWorkers().isEmpty()) {
                createProductionLines(pro, con);
            }
            createCommodityLines(pro, con);
            return true;
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override //FixMe Should be made private if not used outside of creating product && ACID?
    public void createProductionLines(IProductDTO pro, Connection con) throws DALException {
        try {
            String query = "INSERT INTO production(?, ?)";
            PreparedStatement preStatement = con.prepareStatement(query);
            int proID = pro.getID();

            for(IUserDTO user : pro.getWorkers()) {
                preStatement.setInt(1, user.getID());
                preStatement.setInt(2, proID);
                preStatement.execute();
            }

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override //FixMe Should be made private if not used outside of creating product
    public void createCommodityLines(IProductDTO pro, Connection con) throws SQLException {
        String query = "INSERT INTO commodity_line VALUES(?, ?)";
        PreparedStatement preStatement = con.prepareStatement(query);

        for(ICommodityDTO com : pro.getCommodities()) {
            preStatement.setInt(1, pro.getID());
            preStatement.setInt(2, com.getBatch_id());
            preStatement.execute();
        }
    }

    //Read
    @Override
    public List<IProductDTO> getAllProducts() throws DALException {    //TODO Split into readProducts methods for manufactured and not-yet-manufactured instances of product?
        List<IProductDTO> products = new ArrayList<>();

        try(Connection con = db.createConnection()) {
//            ResultSet rsProducts = con.prepareStatement("SELECT product.*, recipe.recipe_name FROM product JOIN recipe ON product.recipe_id = recipe.recipe_id;").executeQuery();
            ResultSet rsProducts = con.prepareStatement("SELECT product.* FROM product").executeQuery();

            while(rsProducts.next()) {
                int proID = rsProducts.getInt(1);
                List<IUserDTO> users = getProductWorkers(proID, con);
                List<ICommodityDTO> commodities = getProductCommodities(proID, con);
                IRecipeDAO recipeDAO = new RecipeDAO();

                IProductDTO product = new ProductDTO(
                        proID,                                                          //ID
//                        rsProducts.getString(7),    //Name of product
                        recipeDAO.getRecipeName(rsProducts.getInt(2), con), //Name of product
                        rsProducts.getInt(2),                               //Recipe ID
                        rsProducts.getInt(3),                               //Orderers ID
                        rsProducts.getInt(4),                               //Quantity of product
                        users,                                                          //Laborants working on it
                        commodities,                                                    //Commodities used
                        rsProducts.getDate(5),                              //Date of production
                        rsProducts.getBoolean(6)                            //Manufactured
                );
                products.add(product);
//                products.add(getProduct(rsProducts, con));
            }

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }

        return products;
    }

    @Override
    public IProductDTO getProduct(int id) throws DALException {
        IProductDTO product = null;
        try (Connection con = db.createConnection()){
            PreparedStatement preStatement = con.prepareStatement("SELECT * FROM product WHERE batch_id = ?");
            preStatement.setInt(1, id);
            ResultSet rsProducts = preStatement.executeQuery();

            if(rsProducts.next()) {
                int proID = rsProducts.getInt(1);
                List<IUserDTO> users = getProductWorkers(proID, con);
                List<ICommodityDTO> commodities = getProductCommodities(proID, con);
                IRecipeDAO recipeDAO = new RecipeDAO();

                product = new ProductDTO(
                        proID,                                                          //ID
    //                        rsProducts.getString(7),    //Name of product
                        recipeDAO.getRecipeName(rsProducts.getInt(2), con), //Name of product
                        rsProducts.getInt(2),                               //Recipe ID
                        rsProducts.getInt(3),                               //Orderers ID
                        rsProducts.getInt(4),                               //Quantity of product
                        users,                                                          //Laborants working on it
                        commodities,                                                    //Commodities used
                        rsProducts.getDate(5),                              //Date of production
                        rsProducts.getBoolean(6)                            //Manufactured
                );
            }
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
        return product;
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

    private List<ICommodityDTO> getProductCommodities(int productID, Connection con) throws DALException {
        List<ICommodityDTO> commodities = null;
        try {
            ICommodityDAO commodityDAO = new CommodityDAO();
            commodities = new ArrayList<>();

            PreparedStatement preStatement = con.prepareStatement("SELECT commodity_batch_id FROM commodity_line WHERE product_batch_id = ?");
            preStatement.setInt(1, productID);
            ResultSet rsCommodities = preStatement.executeQuery();

            while(rsCommodities.next()) {
                ICommodityDTO commodity = commodityDAO.getCommmodity(rsCommodities.getInt(1));
                commodities.add(commodity);
            }
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
        return commodities;
    }

    //Update
    @Override
    public void updateProductInfo(IProductDTO pro, List<IUserDTO> oldUsers) throws DALException {
        try(Connection con = db.createConnection()) {
            String query = "UPDATE product SET ordered_by = ?, recipe_id = ?, quantity = ?, production_date = ? WHERE batch_id = ?"; //TODO Put "finished_production = ?," into query when db updated
            PreparedStatement preStatement = con.prepareStatement(query);

            preStatement.setInt(1, pro.getOrderedBy());
            preStatement.setInt(2, pro.getRecipeID());
            preStatement.setInt(3, pro.getQuantity());
            preStatement.setDate(4, pro.getDate());
            preStatement.execute();
            //Update workers
            updateLaborants(pro, oldUsers, con);
        } catch(SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    private void updateLaborants(IProductDTO newPro, List<IUserDTO> oldUsers, Connection con) throws SQLException {
        PreparedStatement deleteStatement = null, insertStatement = null;
        List<IUserDTO> usersToBeDeleted, usersToBeInserted;
        try{
            con.setAutoCommit(false);
            deleteStatement = con.prepareStatement("DELETE * FROM production WHERE laborant_id = ? AND product_id = ?");
            insertStatement = con.prepareStatement("INSERT INTO production VALUES (?,?)");

            List<IUserDTO> newUsersTemp = newPro.getWorkers();
            newUsersTemp.removeAll(oldUsers);
            usersToBeInserted = newUsersTemp;
            oldUsers.removeAll(newPro.getWorkers());
            usersToBeDeleted = oldUsers;

            updateProductionLines(deleteStatement, newPro.getID(), usersToBeDeleted, con);
            updateProductionLines(insertStatement, newPro.getID(), usersToBeInserted, con);
        } catch(SQLException e) {
            if(con != null) {
                con.rollback();
            }
            throw e;
        } finally {
            if(deleteStatement != null) {
                deleteStatement.close(); //FixMe This is good practice, instead of waiting for garbage collector to come by
            }
            if(insertStatement != null) {
                insertStatement.close(); //FixMe This is good practice, instead of waiting for garbage collector to come by
            }
            con.setAutoCommit(true);
        }
    }

    private void updateProductionLines(PreparedStatement statement, int proID, List<IUserDTO> users, Connection con) throws SQLException {
        for(IUserDTO user : users) {
            statement.setInt(1, user.getID());
            statement.setInt(2, proID);
            statement.execute();
            con.commit();
        }
    }

        //MarkAsFinished
    @Override //TODO Should set date to current date aswell
    public void markAsFinished(int id) throws DALException {
        try(Connection con = db.createConnection()) {
            String query = "UPDATE product SET manufactured = ? WHERE batch_id = ?";
            PreparedStatement preStatement = con.prepareStatement(query);
            preStatement.setBoolean(1, true);
            preStatement.setInt(2, id);
        } catch(SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    //Delete
    @Override
    public void deleteProduct(int id) throws DALException {
        try(Connection con = db.createConnection()) {
            String query = "DELETE FROM product WHERE product_id = ?";
            PreparedStatement preStatement = con.prepareStatement(query);
            preStatement.setInt(1, id);
            preStatement.execute();
        } catch(SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}