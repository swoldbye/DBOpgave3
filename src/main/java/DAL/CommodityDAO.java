package DAL;

import DTO.ICommodityDTO;
import DTO.CommodityDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CommodityDAO implements ICommodityDAO {

    DBConnection dbConnection = new DBConnection();

    public void createCommodity(ICommodityDTO commodity) throws DALException {

        try(Connection connection = dbConnection.createConnection()) {

            String query = "INSERT INTO commodity VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //preparedStatement.setInt(1);

        }
        catch (SQLException e){
           throw new DALException("Something is wrong");
        }
    }





    public ICommodityDTO getCommmodity(int batch_id) {
        return null;
    }


    public List<ICommodityDTO> getCommodityList(){

        return null;
    }

    public void UpdateCommodity() {

    }

    public void DeleteCommodity() {

    }

}
