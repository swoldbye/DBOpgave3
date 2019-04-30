package DAL;

import IngridientDTO.IIngridientDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IngridientDAO implements IIngridientDAO {

    public IngridientDAO(){};

    private Connection createConnection() throws SQLException {
        return  DriverManager.getConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s180943?"
                + "UXZTadQzbPrlIosGCZYNF");
    }


    public void createIngridient(IIngridientDTO ingridient){

        try {

            String sql = "INSERT INTO ingredient(ingredient_id,ingredient_name,needs_refill)" + "VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ingridient.getIngredient_id());
            statement.setString(2, ingridient.getIngredient_name());
            statement.setBoolean(3, ingridient.getNeeds_refill());
            statement.executeUpdate();

        }catch (SQLException e){e.getErrorCode();}

    }

    public void updateIngridient(int ingridient_id){





    }

    public void deleteIngridient(int ingridient_id){}

   public void updateRefill(int ingridient_id){}








}
