package DAL;

import DTO.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDAO implements IProductDAO {
    private DBConnection db = new DBConnection();

    //Create

    /**
     * Inserts product information into the database
     *
     * @param pro   Instance of ProductDTO
     * @return      True if product has been inserted into database, else false
     * @throws      DALException
     */
    @Override
    public boolean createProduct(IProductDTO pro) throws DALException {
        Connection con = db.createConnection();
        try {
            db.toggleAutoCommit(false);
            String query = "INSERT INTO product VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preStatement = con.prepareStatement(query);

            preStatement.setInt(1, pro.getID());
            preStatement.setInt(2, pro.getOrderedBy());
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
            db.toggleAutoCommit(true);
            db.killConnection();
        }
    }

    /**
     * Helper method: Inserts the connection between product batch and recipe into the database
     *
     * @param pro   Instance of ProductDTO
     * @param con   Connection to database
     * @throws      SQLException
     */
    private void createProductRecipeConnection(IProductDTO pro, Connection con) throws SQLException {
        String query = "INSERT INTO product_recipe VALUES(?, ?)";
        PreparedStatement preStatement = con.prepareStatement(query);
        preStatement.setInt(1, pro.getRecipeID());
        preStatement.setInt(2, pro.getID());
        preStatement.executeUpdate();
    }

    /**
     * Helper method: Inserts the connection between product batch and workers into the database
     *
     * @param pro   Instance of ProductDTO
     * @param con   Connection to database
     * @throws      SQLException
     */
    private void createProductionLines(IProductDTO pro, Connection con) throws SQLException {
        String query = "INSERT INTO production VALUES(?, ?)";
        PreparedStatement preStatement = con.prepareStatement(query);
        int proID = pro.getID();

        for(IUserDTO user : pro.getWorkers()) {
            preStatement.setInt(1, user.getUserId());
            preStatement.setInt(2, proID);
            preStatement.executeUpdate();
        }
    }

    /**
     * Helper method: Inserts the connections between product batch and its commodity batches into the database
     *
     * @param pro   Instance of ProductDTO
     * @param con   Connection to database
     * @throws      SQLException
     */
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

    /**
     * Returns a list of ProductDTO to be shown in upper layers
     *
     * @return  List of ProdutDTO
     * @throws  DALException
     */
    @Override
    public List<IProductDTO> getAllProducts() throws DALException {    //TODO Split into readProducts methods for manufactured and not-yet-manufactured instances of product?
        List<IProductDTO> products = new ArrayList<>();

        try(Connection con = db.createConnection()) {
            ResultSet rsProducts = con.prepareStatement(
                    "SELECT product.*, product_recipe.recipe_id, recipe.recipe_name FROM product \\n\" +\n" +
                    " \"INNER JOIN product_recipe ON product.batch_id = product_recipe.batch_id \\n\" +\n" +
                    "\"INNER JOIN recipe ON product_recipe.recipe_id = recipe.recipe_id\\n\" +\n").executeQuery();

            while(rsProducts.next()) {
                int proID = rsProducts.getInt(1);
                products.add(retrieveProduct(con, rsProducts, proID));
            }
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
        return products;
    }

    /**
     * Returns an instance of ProductDTO to be handled in upper layers
     *
     * @param id    ID of product
     * @return      Instance of ProductDTO
     * @throws      DALException
     */
    @Override
    public IProductDTO getProduct(int id) throws DALException {
        IProductDTO product = null;
        try (Connection con = db.createConnection()){
            PreparedStatement preStatement = con.prepareStatement(
                    "SELECT product.*, product_recipe.recipe_id, recipe.recipe_name FROM product \n" +
                    "INNER JOIN product_recipe ON product.batch_id = product_recipe.batch_id \n" +
                    "INNER JOIN recipe ON product_recipe.recipe_id = recipe.recipe_id\n" +
                    "WHERE product.batch_id = ?;");
            preStatement.setInt(1, id);
            ResultSet rsProducts = preStatement.executeQuery();

            if(rsProducts.next()) {
                int proID = rsProducts.getInt(1);
                product = retrieveProduct(con, rsProducts, proID);
            }
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
        return product;
    }

    /**
     * Helper method: Retrives the product. Can be called from a loop to collect more information from already
     * queried ResultSet
     *
     * @param con           Connection to database
     * @param rsProducts    Already queried ResultSet
     * @param proID         ID of product
     * @return              Instance of ProductDTO
     * @throws              DALException
     * @throws              SQLException
     */
    private IProductDTO retrieveProduct(Connection con, ResultSet rsProducts, int proID) throws DALException, SQLException {
        IProductDTO product;
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
        return product;
    }

    /**
     * Helper method: Retrieves a list of workers on a product from database
     *
     * @param productID ID of product
     * @param con       Connection to database
     * @return          List of UserDTO
     * @throws          DALException
     */
    private List<IUserDTO> getProductWorkers(int productID, Connection con) throws DALException {
        try {
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
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    /**
     * Helper method: Retrieves list of commodity batches associated with a product
     *
     * @param productID ID of product
     * @param con       Connection to database
     * @return          List of CommodityDTO
     * @throws          DALException
     */
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
    /**
     * Updates the information of a product in the database from a new ProductDTO. Will rewrite information even if it
     * is the same. Uses a list of UserDTO to delete old connections between workers and product batch
     *
     * @param pro       Instance of ProductDTO
     * @param oldUsers  List of UserDTO
     * @return          True if update succesfull, else false
     * @throws          DALException
     */
    @Override
    public boolean updateProductInfo(IProductDTO pro, List<IUserDTO> oldUsers) throws DALException {
        Connection con = db.createConnection();
        try {
            db.toggleAutoCommit(false);
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
            db.toggleAutoCommit(true);
            db.killConnection();
        }
    }

    /**
     * Helper method: Updates the connection between workers and product batch. Will only delete those workers who
     * doesn't work on the batch any further and create connections if new workers are added
     *
     * @param newPro    The new ProductDTO
     * @param oldUsers  List of previous workers
     * @param con       Connection to database
     * @throws          SQLException
     */
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

            updateProductionLines(deleteStatement, newPro.getID(), usersToBeDeleted);
            updateProductionLines(insertStatement, newPro.getID(), usersToBeInserted);
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

    /**
     * Helper method: Updates the connection between product batch and worker from previous given PreparedStatement
     *
     * @param statement     Query which can either delete or insert
     * @param proID         ID of product
     * @param users         List of workers
     * @throws              SQLException
     */
    private void updateProductionLines(PreparedStatement statement, int proID, List<IUserDTO> users) throws SQLException {
        for(IUserDTO user : users) {
            statement.setInt(1, user.getUserId());
            statement.setInt(2, proID);
            statement.executeUpdate();
        }
    }

    //MarkAsFinished
    /**
     * Marks a product as finished
     *
     * @param id    ID of product
     * @throws      DALException
     */
    @Override //TODO Should set date to current date aswell //TODO Make able to choose one more variable for "Placed order, Working on, Done". Possibly change from boolean
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
    /**
     * Deletes a product from the database
     *
     * @param id    ID of product
     * @throws      DALException
     */
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
    }}