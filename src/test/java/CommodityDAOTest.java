/*
import static org.junit.Assert.*;
import DAL.CommodityDAO;
import DAL.DALException;
import DAL.ICommodityDAO;
import DTO.CommodityDTO;
import DTO.ICommodityDTO;

import java.util.List;

public class CommodityDAOTest{

    ICommodityDAO iCommodityDAO = new CommodityDAO();

    @org.junit.Test
    public void test() throws DALException{
        try {

            ICommodityDTO testCommodityDTO = new CommodityDTO(50,1,
                    5.2,true,"homecook","Sildenafil");

            iCommodityDAO.createCommodity(testCommodityDTO);
            ICommodityDTO returnedCommodity = iCommodityDAO.getCommmodity(50);
            assertEquals(testCommodityDTO.toString(),returnedCommodity.toString());

            List<ICommodityDTO> AllCommodities = iCommodityDAO.getCommodityList();
            boolean found = false;
            for (ICommodityDTO commodity: AllCommodities){
                if (commodity.getBatch_id() == testCommodityDTO.getBatch_id()){
                    assertEquals(testCommodityDTO.toString(),commodity.toString());
                    found = true;
                }
            }
            if (!found){
                fail();
            }

            testCommodityDTO.setQuantity(6.4);
            testCommodityDTO.setIs_leftover(false);
            testCommodityDTO.setCommodity_manufacturer("Hjemme hos Andreas");
            iCommodityDAO.updateCommodity(testCommodityDTO);

            returnedCommodity = iCommodityDAO.getCommmodity(50);
            assertEquals(testCommodityDTO.toString(),returnedCommodity.toString());

            iCommodityDAO.deleteCommodity(50);
            for (ICommodityDTO commodity : iCommodityDAO.getCommodityList()){
                if (testCommodityDTO.getBatch_id() == commodity.getBatch_id()){
                    fail();
                }
            }
        }
        catch (DALException e){
            e.printStackTrace();
            fail();
        }
    }


}*/