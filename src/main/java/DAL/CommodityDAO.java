package DAL;

import DTO.CommodityDTO;
import DTO.ICommodityDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommodityDAO implements ICommodityDAO {

    DBConnection dbConnection = new DBConnection();

    /**
     * Creates a row for the commodity table
     *
     * @param commodity
     * @throws DALException
     */
    public void createCommodity(ICommodityDTO commodity) throws DALException {

        try(Connection connection = dbConnection.createConnection()) {

            String query = "INSERT INTO commodity VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,commodity.getBatch_id());
            preparedStatement.setInt(2,commodity.getIngrdient_id());
            preparedStatement.setInt(3,commodity.getQuantity());
            preparedStatement.setBoolean(4,commodity.isIs_leftover());
            preparedStatement.setBoolean(5,commodity.isManufacture());
            preparedStatement.executeUpdate();

        }
        catch (SQLException e){
           throw new DALException(e.getMessage());
           //System.out.println(e);
        }
    }

    /**
     * Joins commodity and and ingredient tables (to get the ingredient name for the batch,
     * and returns a commodity with a certain batch id
     *
     * @param ingredient_id
     * @return
     * @throws DALException
     */
    public ICommodityDTO getCommmodity(int ingredient_id) throws DALException {

        try(Connection connection = dbConnection.createConnection()) {

            // SELECT batch_id, commodity.ingredient_id, ingredient_name, quantity, is_leftover, manufacture FROM commodity LEFT JOIN ingredient ON commodity.ingredient_id

            String query = "SELECT commodity.*,ingredient_name FROM commodity JOIN ingredient ON commodity.ingredient_id = ingredient.ingredient_id WHERE ingredient.ingredient_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,ingredient_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                ICommodityDTO commodity1 = new CommodityDTO();
                commodity1.setBatch_id(resultSet.getInt(1));
                commodity1.setIngrdient_id(resultSet.getInt(2));
                commodity1.setQuantity(resultSet.getInt(3));
                commodity1.setIs_leftover(resultSet.getBoolean(4));
                commodity1.setManufacture(resultSet.getBoolean(5));
                commodity1.setIngredient_name(resultSet.getString(6));
                return commodity1;
            }
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
            //System.out.println(e);
        }
        return null;
    }

    /**
     * Joins commodity with ingredient to get the name of the commodity batch, and returns all commodities
     *
     * @return
     * @throws DALException
     */
    public List<ICommodityDTO> getCommodityList() throws DALException{

        List<ICommodityDTO> commodities = new ArrayList<>();

        try(Connection connection = dbConnection.createConnection()) {

            // SELECT batch_id, commodity.ingredient_id, ingredient_name, quantity, is_leftover, manufacture FROM commodity LEFT JOIN ingredient ON commodity.ingredient_id

            String query = "SELECT commodity.*,ingredient_name FROM commodity JOIN ingredient ON commodity.ingredient_id = ingredient.ingredient_id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                ICommodityDTO commodity1 = new CommodityDTO();
                commodity1.setBatch_id(resultSet.getInt(1));
                commodity1.setIngrdient_id(resultSet.getInt(2));
                commodity1.setQuantity(resultSet.getInt(3));
                commodity1.setIs_leftover(resultSet.getBoolean(4));
                commodity1.setManufacture(resultSet.getBoolean(5));
                commodity1.setIngredient_name(resultSet.getString(6));
                commodities.add(commodity1);
            }
            return commodities;
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
            //System.out.println(e);
        }
        //return null;
    }

    /**
     * Updates a certain commodity in the commodity table
     *
     * @param commodity
     * @throws DALException
     */
    public void updateCommodity(ICommodityDTO commodity) throws DALException{

        try(Connection connection = dbConnection.createConnection()){

            String query = "UPDATE commodity SET quantity = ?, is_leftover = ?, manufacture = ? WHERE batch_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,commodity.getQuantity());
            preparedStatement.setBoolean(2,commodity.isIs_leftover());
            preparedStatement.setBoolean(3,commodity.isManufacture());
            preparedStatement.setInt(4,commodity.getBatch_id());
            preparedStatement.executeUpdate();

        }
        catch (SQLException e) {
            throw new DALException(e.getMessage());
            //System.out.println(e);
        }
    }

    /**
     * Deletes a commodity with a specific batch id from the database
     *
     * @param batch_id
     * @throws DALException
     */
    public void deleteCommodity(int batch_id) throws DALException{

        try(Connection connection = dbConnection.createConnection()){

            String query = "DELETE FROM commodity WHERE batch_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,batch_id);
            preparedStatement.executeUpdate();

        }
        catch (SQLException e){
            System.out.println(e);
        }

    }



}
//class test{
//    public static void main(String[] args) {
//
//        ICommodityDAO comDAO = new CommodityDAO();
//
//        List<ICommodityDTO> list = new ArrayList<>();
//
//        ICommodityDTO com = new CommodityDTO();
////        com.setBatch_id(12);
////        com.setIngrdient_id(12);
////        com.setIs_leftover(false);
////        com.setQuantity(8);
////        com.setManufacture(false);
//
//        try {
//            //comDAO.createCommodity(com);
////            com = comDAO.getCommmodity(12);
////            System.out.println(com);
////            com = comDAO.getCommmodity(1);
////            System.out.println(com);
////            com = comDAO.getCommmodity(2);
////            System.out.println(com);
//
////            list = comDAO.getCommodityList();
////            for(int i=0;i<list.size();i++){
////                System.out.println(list.get(i));
////            }
//
//            comDAO.deleteCommodity(12);
//
//
//        }
//        catch (Exception e){
//            System.out.println(e);
//        }
//
//
//
//
//    }
//}
