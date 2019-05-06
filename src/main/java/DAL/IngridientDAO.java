package DAL;

import IngridientDTO.IIngridientDTO;
import IngridientDTO.IngridientDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IngridientDAO implements IIngridientDAO {

    private DBConnection conn = new DBConnection();

    public IngridientDAO(){}

    private Connection createConnection() throws SQLException {
        return  DriverManager.getConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s180943?"
                + "UXZTadQzbPrlIosGCZYNF");
    }


    public void createIngridient(IIngridientDTO ingridient){

        try(Connection connection = conn.createConnection()) {

            String sql = "INSERT INTO ingredient(ingredient_id,ingredient_name,needs_refill)" + "VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ingridient.getIngredient_id());
            statement.setString(2, ingridient.getIngredient_name());
            statement.setBoolean(3, ingridient.getNeeds_refill());
            statement.executeUpdate();

        }catch (SQLException e){e.getErrorCode();}

    }

    public void updateIngridient(IIngridientDTO ingridient){
        try(Connection connection = conn.createConnection()) {

            String sql = "UPDATE ingredient SET ingridient_name = ?, needs_refill=?  WHERE ingredient_id = '"+ingridient.getIngredient_id()+"'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(2, ingridient.getIngredient_name());
            statement.setBoolean(3, ingridient.getNeeds_refill());
            statement.executeUpdate();

        }catch (SQLException e){e.getErrorCode();}


    }

    public void deleteIngridient(int ingridient_id){}

   public void updateRefill(int ingridient_id){}


    public static void main(String[] args) {
        IIngridientDTO ingridien = new IngridientDTO(1,"dsadsadasdsfdsfsdf",true);

        IngridientDAO one = new IngridientDAO();

        //one.createIngridient(ingridien);

        ingridien.setIngredient_name("AAAAAA");
        ingridien.setNeeds_refill(false);
        one.updateIngridient(ingridien);

        System.out.println(1332%11);
    }

}
