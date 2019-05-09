package DAL;

import DTO.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDAO implements IProductDAO {
    private DBConnection db = new DBConnection();

    //Create
    @Override   //TODO ACID
    public boolean createProduct(IProductDTO pro) throws DALException {
//        try(Connection con = db.createConnection()) {
        Connection con = db.createConnection();
        try {
//            con.setAutoCommit(false);
            db.toggleAutoCommit();
            String query = "INSERT INTO product VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preStatement = con.prepareStatement(query);

            preStatement.setInt(1, pro.getID());
            preStatement.setInt(2, pro.getOrderedBy());
//            preStatement.setInt(3, pro.getRecipeID());
            preStatement.setInt(3, pro.getQuantity());
            preStatement.setDate(4, pro.getDate());
            preStatement.setBoolean(5, pro.isManufactured());

            preStatement.execute();

            if(!pro.getWorkers().isEmpty()) {
                createProductionLines(pro, con);
            }
            createCommodityLines(pro, con);
            createProductRecipeConnection(pro, con);
            con.commit();
            return true;
        } catch (SQLException e) {
            try {
                if(con != null) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                throw new DALException(e1.getMessage());
            }
            throw new DALException(e.getMessage());
        } finally {
//            try {
            db.toggleAutoCommit();
            db.killConnection();
//                con.setAutoCommit(true);
//            } catch (SQLException e) {
//                throw new DALException(e.getMessage());
//            }
        }
    }

    private void createProductRecipeConnection(IProductDTO pro, Connection con) throws SQLException {
        String query = "INSERT INTO product_recipe VALUES(?, ?)";
        PreparedStatement preStatement = con.prepareStatement(query);
        preStatement.setInt(1, pro.getRecipeID());
        preStatement.setInt(2, pro.getID());
        preStatement.executeUpdate();
    }

    private void createProductionLines(IProductDTO pro, Connection con) throws SQLException {
        String query = "INSERT INTO production VALUES(?, ?)";
        PreparedStatement preStatement = con.prepareStatement(query);
        int proID = pro.getID();

        for(IUserDTO user : pro.getWorkers()) {
            preStatement.setInt(1, user.getID());
            preStatement.setInt(2, proID);
            preStatement.executeUpdate();
        }
    }

    private void createCommodityLines(IProductDTO pro, Connection con) throws SQLException {
        String query = "INSERT INTO commodity_line VALUES(?, ?)";
        PreparedStatement preStatement = con.prepareStatement(query);

        for(ICommodityDTO com : pro.getCommodities()) {
            preStatement.setInt(1, pro.getID());
            preStatement.setInt(2, com.getBatch_id());
            preStatement.executeUpdate();
        }
    }

    //Read
    @Override
    public List<IProductDTO> getAllProducts() throws DALException {    //TODO Split into readProducts methods for manufactured and not-yet-manufactured instances of product?
        List<IProductDTO> products = new ArrayList<>();

        try(Connection con = db.createConnection()) {
            ResultSet rsProducts = con.prepareStatement("SELECT product.* FROM product").executeQuery();

            while(rsProducts.next()) {
                int proID = rsProducts.getInt(1);
                List<IUserDTO> users = getProductWorkers(proID, con);
                List<ICommodityDTO> commodities = getProductCommodities(proID, con);
                IRecipeDAO recipeDAO = new RecipeDAO();

                IProductDTO product = new ProductDTO(
                        proID,                                                          //ID
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
//            PreparedStatement preStatement = con.prepareStatement("SELECT * FROM product WHERE batch_id = ?");
            PreparedStatement preStatement = con.prepareStatement(
                    "SELECT product.*, product_recipe.recipe_id, recipe.recipe_name FROM product \n" +
                    "INNER JOIN product_recipe ON product.batch_id = product_recipe.batch_id \n" +
                    "INNER JOIN recipe ON product_recipe.recipe_id = recipe.recipe_id\n" +
                    "WHERE product.batch_id = ?;");
            preStatement.setInt(1, id);
            ResultSet rsProducts = preStatement.executeQuery();

            if(rsProducts.next()) {
                int proID = rsProducts.getInt(1);
                List<IUserDTO> users = getProductWorkers(proID, con);
                List<ICommodityDTO> commodities = getProductCommodities(proID, con);
                IRecipeDAO recipeDAO = new RecipeDAO();

                product = new ProductDTO(
                        proID,                                                          //ID
                        rsProducts.getString(7),                            //Name of product
                        rsProducts.getInt(6),                               //Recipe ID
                        rsProducts.getInt(2),                               //Orderers ID
                        rsProducts.getInt(3),                               //Quantity of product
                        users,                                                          //Laborants working on it
                        commodities,                                                    //Commodities used
                        rsProducts.getDate(4),                              //Date of production
                        rsProducts.getBoolean(5)                            //Manufactured //TODO Fix 3rd possibility
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
    public boolean updateProductInfo(IProductDTO pro, List<IUserDTO> oldUsers) throws DALException {
//        try(Connection con = db.createConnection()) {
        Connection con = db.createConnection();
        try {
            db.toggleAutoCommit();
            String query = "UPDATE product SET ordered_by = ?, quantity = ?, production_date = ?, manufactured = ? WHERE batch_id = ?"; //TODO Put "manufactured = ?," into query when db updated
            PreparedStatement preStatement = con.prepareStatement(query);

            preStatement.setInt(1, pro.getOrderedBy());
            preStatement.setInt(2, pro.getQuantity());
            preStatement.setDate(3, pro.getDate());
            preStatement.setBoolean(4, pro.isManufactured());
            preStatement.setInt(5, pro.getID());
            preStatement.execute();
            //Update workers
            updateLaborants(pro, oldUsers, con);
            con.commit();
            return true;
        } catch(SQLException e) {
            try {
                if(con != null) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                throw new DALException(e1.getMessage());
            }
            throw new DALException(e.getMessage());
        } finally {
            db.toggleAutoCommit();
            db.killConnection();
        }
    }

    private void updateLaborants(IProductDTO newPro, List<IUserDTO> oldUsers, Connection con) throws SQLException {
        PreparedStatement deleteStatement = null, insertStatement = null;
        List<IUserDTO> usersToBeDeleted, usersToBeInserted;
        try{
            con.setAutoCommit(false);
            deleteStatement = con.prepareStatement("DELETE * FROM production WHERE laborant_id = ? AND product_id = ?");
            insertStatement = con.prepareStatement("INSERT INTO production VALUES(?,?)");

            List<IUserDTO> newUsersTemp = newPro.getWorkers();
            newUsersTemp.removeAll(oldUsers);
            usersToBeInserted = newUsersTemp;
            oldUsers.removeAll(newPro.getWorkers());
            usersToBeDeleted = oldUsers;

            updateProductionLines(deleteStatement, newPro.getID(), usersToBeDeleted, con);
            updateProductionLines(insertStatement, newPro.getID(), usersToBeInserted, con);
            con.commit();
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
        }
    }

    private void updateProductionLines(PreparedStatement statement, int proID, List<IUserDTO> users, Connection con) throws SQLException {
        for(IUserDTO user : users) {
            statement.setInt(1, user.getID());
            statement.setInt(2, proID);
            statement.execute();
            statement.executeUpdate();
        }
    }

        //MarkAsFinished
    @Override //TODO Should set date to current date aswell
    public void markAsFinished(int id) throws DALException {
        try(Connection con = db.createConnection()) {
            String query = "UPDATE product SET production_date = ?, manufactured = ? WHERE batch_id = ?";
            PreparedStatement preStatement = con.prepareStatement(query);
            Date currDate = new Date(Calendar.getInstance().getTime().getTime());
            preStatement.setDate(1, currDate);
            preStatement.setBoolean(2, true);
            preStatement.setInt(3, id);
            preStatement.executeUpdate();
        } catch(SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    //Delete
    @Override
    public void deleteProduct(int id) throws DALException {
        try(Connection con = db.createConnection()) {
            String query = "DELETE FROM product WHERE batch_id = ?";
            PreparedStatement preStatement = con.prepareStatement(query);
            preStatement.setInt(1, id);
            preStatement.execute();
        } catch(SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}