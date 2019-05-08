//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//import DAL.UserDAO;
//import DAL.IUserDAO;

import DAL.CommodityDAO;
import DAL.DALException;
import DAL.ICommodityDAO;
import DTO.CommodityDTO;
import DTO.ICommodityDTO;

public class CommodityDAOTest{

    ICommodityDAO iCommodityDAO = new CommodityDAO();

    @org.junit.Test
    public void test() throws DALException{
        try {

            ICommodityDTO commodityDTO = new CommodityDTO();



        }
        catch (Exception e){
            throw new DALException(e.getLocalizedMessage());
        }
    }


}